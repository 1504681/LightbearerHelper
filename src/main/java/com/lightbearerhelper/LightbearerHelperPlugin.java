package com.lightbearerhelper;

import com.google.inject.Provides;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.api.VarPlayer;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.api.events.BeforeRender;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
	name = "Lightbearer Helper",
	description = "Highlights when to swap the Lightbearer for your other rings and pulses the spec orb when spec is ready",
	tags = {"lightbearer", "ring", "spec", "special attack", "swap", "highlight", "ultor", "bellator", "magus", "venator"}
)
public class LightbearerHelperPlugin extends Plugin
{
	// keep in sync with build.gradle
	public static final String VERSION = "1.0.1";

	private static final Logger log = LoggerFactory.getLogger(LightbearerHelperPlugin.class);

	private static final String LIGHTBEARER_NAME = "lightbearer";

	private static final int KIND_LIGHTBEARER = 1;
	private static final int KIND_RING = 2;
	private static final int KIND_SPEC = 4;

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private LightbearerHelperConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ItemHighlightOverlay itemHighlightOverlay;

	@Inject
	private SpecOrbOverlay specOrbOverlay;

	@Inject
	private ItemManager itemManager;

	private ItemMatcher ringMatcher = new ItemMatcher("");
	private ItemMatcher specMatcher = new ItemMatcher("");
	private final Map<Integer, Integer> kindCache = new HashMap<>();

	private volatile HighlightState.Mode mode = HighlightState.Mode.IDLE;
	private volatile boolean orbHighlightActive;
	private volatile boolean specItemsActive;
	private int wornRingId = -1;
	private int wornWeaponId = -1;

	// spec orb text colour before we touched it, null when we haven't
	private Integer originalOrbTextColor;

	@Provides
	LightbearerHelperConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(LightbearerHelperConfig.class);
	}

	@Override
	protected void startUp()
	{
		rebuildMatchers();
		overlayManager.add(itemHighlightOverlay);
		overlayManager.add(specOrbOverlay);
		clientThread.invoke(this::recompute);
		log.info("Lightbearer Helper started");
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(itemHighlightOverlay);
		overlayManager.remove(specOrbOverlay);
		clientThread.invoke(this::restoreOrbText);
		mode = HighlightState.Mode.IDLE;
		orbHighlightActive = false;
		specItemsActive = false;
		wornRingId = -1;
		wornWeaponId = -1;
		kindCache.clear();
		itemHighlightOverlay.invalidateCache();
		log.info("Lightbearer Helper stopped");
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!LightbearerHelperConfig.GROUP.equals(event.getGroup()))
		{
			return;
		}
		rebuildMatchers();
		itemHighlightOverlay.invalidateCache();
		clientThread.invoke(this::recompute);
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		if (event.getVarpId() == VarPlayer.SPECIAL_ATTACK_PERCENT)
		{
			recompute();
		}
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		int id = event.getContainerId();
		if (id == InventoryID.EQUIPMENT.getId() || id == InventoryID.INVENTORY.getId())
		{
			recompute();
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOGGED_IN)
		{
			recompute();
		}
		else if (event.getGameState() == GameState.LOGIN_SCREEN || event.getGameState() == GameState.HOPPING)
		{
			mode = HighlightState.Mode.IDLE;
			orbHighlightActive = false;
			specItemsActive = false;
		}
	}

	@Subscribe
	public void onBeforeRender(BeforeRender event)
	{
		if (orbHighlightActive && config.tintOrbText())
		{
			tintOrbText();
		}
		else if (originalOrbTextColor != null)
		{
			restoreOrbText();
		}
	}

	private void rebuildMatchers()
	{
		ringMatcher = new ItemMatcher(config.ringList());
		specMatcher = new ItemMatcher(config.specItemList());
		kindCache.clear();
	}

	private void recompute()
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			mode = HighlightState.Mode.IDLE;
			orbHighlightActive = false;
			specItemsActive = false;
			return;
		}

		int specVarp = client.getVarpValue(VarPlayer.SPECIAL_ATTACK_PERCENT);
		boolean specFull = HighlightState.isSpecFull(specVarp);

		ItemContainer equipment = client.getItemContainer(InventoryID.EQUIPMENT);
		wornRingId = slotItemId(equipment, EquipmentInventorySlot.RING);
		wornWeaponId = slotItemId(equipment, EquipmentInventorySlot.WEAPON);

		boolean wornIsListedRing = isListedRing(wornRingId);
		boolean haveSpecItem = isSpecItem(wornWeaponId) || containsSpecItem(client.getItemContainer(InventoryID.INVENTORY));
		mode = HighlightState.resolve(specFull, isLightbearer(wornRingId), wornIsListedRing);

		// swap is done once the ring is on, plus a spec weapon if we're highlighting those too
		boolean swapDone = wornIsListedRing && (!config.highlightSpecItems() || isSpecItem(wornWeaponId));
		boolean specReady = HighlightState.isSpecReady(specVarp, config.specReadyPercent());
		orbHighlightActive = config.orbEnabled()
			&& (haveSpecItem || !config.orbRequireSpecItem())
			&& HighlightState.orbActive(specReady, config.orbAlways(), swapDone);

		boolean specItemsWanted = config.specItemsAlways() ? specReady : mode == HighlightState.Mode.WANT_OTHER_RING;
		specItemsActive = config.highlightSpecItems() && specItemsWanted
			&& !(config.specSkipIfWielded() && isSpecItem(wornWeaponId));
	}

	private boolean containsSpecItem(ItemContainer container)
	{
		if (container == null)
		{
			return false;
		}
		for (Item item : container.getItems())
		{
			if (isSpecItem(item.getId()))
			{
				return true;
			}
		}
		return false;
	}

	private static int slotItemId(ItemContainer container, EquipmentInventorySlot slot)
	{
		if (container == null)
		{
			return -1;
		}
		Item item = container.getItem(slot.getSlotIdx());
		return item == null ? -1 : item.getId();
	}

	public HighlightState.Mode getMode()
	{
		return mode;
	}

	public boolean isOrbHighlightActive()
	{
		return orbHighlightActive;
	}

	public float orbPulseFactor()
	{
		if (!config.orbPulse())
		{
			return 1f;
		}
		return config.orbPulseMode().intensity(System.currentTimeMillis(), config.orbPeriodMs());
	}

	public float itemPulseFactor()
	{
		return config.itemPulseMode().intensity(System.currentTimeMillis(), config.itemPulsePeriodMs());
	}

	// what to draw for this item, null for nothing. equipmentTab = worn equipment interface rather than inventory
	public Highlight getHighlight(int itemId, boolean equipmentTab)
	{
		if (equipmentTab)
		{
			if (config.highlightWornRing() && wornRingId != -1 && itemId == wornRingId)
			{
				if (mode == HighlightState.Mode.WANT_LIGHTBEARER)
				{
					return lightbearerHighlight();
				}
				if (mode == HighlightState.Mode.WANT_OTHER_RING)
				{
					return ringHighlight();
				}
			}
			return null;
		}
		if (mode == HighlightState.Mode.WANT_LIGHTBEARER && isLightbearer(itemId))
		{
			return lightbearerHighlight();
		}
		if (mode == HighlightState.Mode.WANT_OTHER_RING && isListedRing(itemId))
		{
			return ringHighlight();
		}
		if (specItemsActive && isSpecItem(itemId))
		{
			return specItemHighlight();
		}
		return null;
	}

	private Highlight lightbearerHighlight()
	{
		return new Highlight(config.lightbearerColor(), config.lightbearerOutline(),
			config.lightbearerFill(), config.lightbearerFillOpacity(), config.lightbearerUnderline(), config.lightbearerPulse());
	}

	private Highlight ringHighlight()
	{
		return new Highlight(config.ringColor(), config.ringOutline(),
			config.ringFill(), config.ringFillOpacity(), config.ringUnderline(), config.ringPulse());
	}

	private Highlight specItemHighlight()
	{
		return new Highlight(config.specItemColor(), config.specItemOutline(),
			config.specItemFill(), config.specItemFillOpacity(), config.specItemUnderline(), config.specItemPulse());
	}

	public boolean isLightbearer(int itemId)
	{
		return (kind(itemId) & KIND_LIGHTBEARER) != 0;
	}

	public boolean isListedRing(int itemId)
	{
		return (kind(itemId) & KIND_RING) != 0;
	}

	public boolean isSpecItem(int itemId)
	{
		return (kind(itemId) & KIND_SPEC) != 0;
	}

	private int kind(int itemId)
	{
		if (itemId <= 0)
		{
			return 0;
		}
		Integer cached = kindCache.get(itemId);
		if (cached != null)
		{
			return cached;
		}
		int flags = 0;
		String name = itemName(itemId);
		if (itemId == ItemID.LIGHTBEARER || LIGHTBEARER_NAME.equalsIgnoreCase(name))
		{
			flags |= KIND_LIGHTBEARER;
		}
		if (ringMatcher.matches(name))
		{
			flags |= KIND_RING;
		}
		if (specMatcher.matches(name))
		{
			flags |= KIND_SPEC;
		}
		kindCache.put(itemId, flags);
		return flags;
	}

	private String itemName(int itemId)
	{
		ItemComposition composition = itemManager.getItemComposition(itemId);
		if (composition == null)
		{
			return null;
		}
		String name = composition.getName();
		return name == null || "null".equals(name) ? null : name;
	}

	private void tintOrbText()
	{
		Widget text = client.getWidget(InterfaceID.Orbs.SPECENERGY_TEXT);
		if (text == null)
		{
			return;
		}
		if (originalOrbTextColor == null)
		{
			originalOrbTextColor = text.getTextColor();
		}
		float intensity = orbPulseFactor();
		Color from = new Color(originalOrbTextColor);
		Color to = config.orbColor();
		int r = lerp(from.getRed(), to.getRed(), intensity);
		int g = lerp(from.getGreen(), to.getGreen(), intensity);
		int b = lerp(from.getBlue(), to.getBlue(), intensity);
		text.setTextColor((r << 16) | (g << 8) | b);
	}

	private void restoreOrbText()
	{
		if (originalOrbTextColor == null)
		{
			return;
		}
		Widget text = client.getWidget(InterfaceID.Orbs.SPECENERGY_TEXT);
		if (text != null)
		{
			text.setTextColor(originalOrbTextColor);
		}
		originalOrbTextColor = null;
	}

	private static int lerp(int a, int b, float t)
	{
		return Math.max(0, Math.min(255, Math.round(a + (b - a) * t)));
	}
}
