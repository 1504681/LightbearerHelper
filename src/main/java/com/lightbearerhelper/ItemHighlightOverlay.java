package com.lightbearerhelper;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.game.ItemManager;
import net.runelite.client.util.AsyncBufferedImage;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.ui.overlay.WidgetItemOverlay;

public class ItemHighlightOverlay extends WidgetItemOverlay
{
	private static final int UNDERLINE_HEIGHT = 2;

	private final ItemManager itemManager;
	private final LightbearerHelperPlugin plugin;
	private final Map<Long, BufferedImage> outlineCache = new HashMap<>();
	private final Map<Long, BufferedImage> fillCache = new HashMap<>();

	@Inject
	public ItemHighlightOverlay(ItemManager itemManager, LightbearerHelperPlugin plugin)
	{
		this.itemManager = itemManager;
		this.plugin = plugin;
		showOnInventory();
		showOnEquipment();
	}

	@Override
	public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem widgetItem)
	{
		Highlight highlight = plugin.getHighlight(itemId, isEquipmentInterface(widgetItem));
		if (highlight == null)
		{
			return;
		}
		Rectangle bounds = widgetItem.getCanvasBounds();
		if (bounds == null)
		{
			return;
		}

		float factor = highlight.isPulse() ? plugin.itemPulseFactor() : 1f;
		if (factor <= 0f)
		{
			return;
		}
		Composite oldComposite = graphics.getComposite();
		if (factor < 1f)
		{
			graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, factor));
		}

		Color color = highlight.getColor();
		if (highlight.isFill())
		{
			BufferedImage fill = getFill(itemId, widgetItem.getQuantity(), highlight.getFillColor());
			if (fill != null)
			{
				graphics.drawImage(fill, bounds.x, bounds.y, null);
			}
		}
		if (highlight.isOutline())
		{
			BufferedImage outline = getOutline(itemId, widgetItem.getQuantity(), color);
			if (outline != null)
			{
				graphics.drawImage(outline, bounds.x, bounds.y, null);
			}
		}
		if (highlight.isUnderline())
		{
			graphics.setColor(color);
			graphics.fillRect(bounds.x, bounds.y + bounds.height, bounds.width, UNDERLINE_HEIGHT);
		}

		graphics.setComposite(oldComposite);
	}

	void invalidateCache()
	{
		outlineCache.clear();
		fillCache.clear();
	}

	private static boolean isEquipmentInterface(WidgetItem widgetItem)
	{
		Widget widget = widgetItem.getWidget();
		return widget != null && WidgetUtil.componentToInterface(widget.getId()) == InterfaceID.WORNITEMS;
	}

	private BufferedImage getFill(int itemId, int quantity, Color color)
	{
		long key = cacheKey(itemId, quantity, color);
		BufferedImage image = fillCache.get(key);
		if (image == null)
		{
			AsyncBufferedImage sprite = itemManager.getImage(itemId, quantity, false);
			image = ImageUtil.fillImage(sprite, color);
			fillCache.put(key, image);
			// the sprite is normally ready by now, but if it wasn't, redo the fill once it is
			sprite.onLoaded(() -> fillCache.remove(key));
		}
		return image;
	}

	private BufferedImage getOutline(int itemId, int quantity, Color color)
	{
		long key = cacheKey(itemId, quantity, color);
		BufferedImage image = outlineCache.get(key);
		if (image == null)
		{
			image = itemManager.getItemOutline(itemId, quantity, color);
			if (image != null)
			{
				outlineCache.put(key, image);
			}
		}
		return image;
	}

	private static long cacheKey(int itemId, int quantity, Color color)
	{
		return ((long) itemId << 40) | ((long) (quantity & 0xFF) << 32) | (color.getRGB() & 0xFFFFFFFFL);
	}
}
