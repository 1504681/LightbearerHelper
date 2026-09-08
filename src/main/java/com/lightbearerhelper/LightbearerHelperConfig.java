package com.lightbearerhelper;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;
import net.runelite.client.config.Units;

@ConfigGroup(LightbearerHelperConfig.GROUP)
public interface LightbearerHelperConfig extends Config
{
	String GROUP = "lightbearerhelper";

	String DEFAULT_RINGS = "Ultor ring\nBellator ring\nMagus ring\nVenator ring\nBerserker ring*\nRing of suffering*";

	String DEFAULT_SPEC_ITEMS = "Dragon claws\nBurning claws\nVoidwaker\nBandos godsword\nArmadyl godsword\nSaradomin godsword\n"
		+ "Dragon warhammer\nElder maul\nDragon dagger*\nAbyssal dagger*\nDragon halberd\nZaryte crossbow\n"
		+ "Toxic blowpipe*\nEldritch nightmare staff\nVolatile nightmare staff";

	/** Light blue shared by the Lightbearer and spec item highlights. */
	Color LIGHT_BLUE = new Color(80, 200, 255, 255);

	// ---------------------------------------------------------------- sections

	@ConfigSection(
		name = "General",
		description = "Settings shared by all item highlights",
		position = 0
	)
	String generalSection = "general";

	@ConfigSection(
		name = "Lightbearer",
		description = "Highlight the Lightbearer while special attack is below 100%",
		position = 1
	)
	String lightbearerSection = "lightbearer";

	@ConfigSection(
		name = "Other rings",
		description = "Rings to highlight once special attack is at 100% and the Lightbearer is still worn",
		position = 2
	)
	String ringsSection = "rings";

	@ConfigSection(
		name = "Spec items",
		description = "Special attack weapons to highlight once special attack is at 100%",
		position = 3
	)
	String specItemsSection = "specItems";

	@ConfigSection(
		name = "Spec orb",
		description = "Decorate the special attack orb once special attack is at 100%",
		position = 4
	)
	String orbSection = "orb";

	// ---------------------------------------------------------------- general

	@Range(min = 200, max = 3000)
	@Units(Units.MILLISECONDS)
	@ConfigItem(
		keyName = "itemPulsePeriodMs",
		name = "Item pulse period",
		description = "Time for one full pulse of an item highlight that has Pulse enabled",
		position = 0,
		section = generalSection
	)
	default int itemPulsePeriodMs()
	{
		return 800;
	}

	@ConfigItem(
		keyName = "itemPulseMode",
		name = "Item pulse mode",
		description = "Smooth fade or hard blink for pulsing item highlights",
		position = 1,
		section = generalSection
	)
	default OrbPulseMode itemPulseMode()
	{
		return OrbPulseMode.SMOOTH;
	}

	// ---------------------------------------------------------------- lightbearer

	@Alpha
	@ConfigItem(
		keyName = "lightbearerColor",
		name = "Colour",
		description = "Colour used for the Lightbearer highlight",
		position = 0,
		section = lightbearerSection
	)
	default Color lightbearerColor()
	{
		return LIGHT_BLUE;
	}

	@ConfigItem(
		keyName = "lightbearerOutline",
		name = "Outline",
		description = "Draw a coloured outline around the item sprite",
		position = 1,
		section = lightbearerSection
	)
	default boolean lightbearerOutline()
	{
		return true;
	}

	@ConfigItem(
		keyName = "lightbearerBox",
		name = "Box",
		description = "Draw a rectangle around the inventory slot",
		position = 2,
		section = lightbearerSection
	)
	default boolean lightbearerBox()
	{
		return false;
	}

	@ConfigItem(
		keyName = "lightbearerFill",
		name = "Fill",
		description = "Fill the inventory slot with a translucent colour",
		position = 3,
		section = lightbearerSection
	)
	default boolean lightbearerFill()
	{
		return false;
	}

	@Range(min = 0, max = 100)
	@Units(Units.PERCENT)
	@ConfigItem(
		keyName = "lightbearerFillOpacity",
		name = "Fill opacity",
		description = "Opacity of the slot fill",
		position = 4,
		section = lightbearerSection
	)
	default int lightbearerFillOpacity()
	{
		return 30;
	}

	@ConfigItem(
		keyName = "lightbearerUnderline",
		name = "Underline",
		description = "Draw a coloured bar under the inventory slot",
		position = 5,
		section = lightbearerSection
	)
	default boolean lightbearerUnderline()
	{
		return false;
	}

	@ConfigItem(
		keyName = "lightbearerPulse",
		name = "Pulse",
		description = "Pulse the highlight between the colour and transparent (see General for the speed)",
		position = 6,
		section = lightbearerSection
	)
	default boolean lightbearerPulse()
	{
		return false;
	}

	@ConfigItem(
		keyName = "highlightWornRing",
		name = "Mark worn ring to swap",
		description = "Also highlight the ring in the worn equipment tab that should be swapped out",
		position = 7,
		section = lightbearerSection
	)
	default boolean highlightWornRing()
	{
		return true;
	}

	// ---------------------------------------------------------------- other rings

	@ConfigItem(
		keyName = "ringList",
		name = "Rings",
		description = "Rings to highlight when spec is full. One per line, * wildcards allowed, e.g. Ultor ring or Ring of suffering*",
		position = 0,
		section = ringsSection
	)
	default String ringList()
	{
		return DEFAULT_RINGS;
	}

	@Alpha
	@ConfigItem(
		keyName = "ringColor",
		name = "Colour",
		description = "Colour used for the other-ring highlight",
		position = 1,
		section = ringsSection
	)
	default Color ringColor()
	{
		return new Color(0, 255, 255, 255);
	}

	@ConfigItem(
		keyName = "ringOutline",
		name = "Outline",
		description = "Draw a coloured outline around the item sprite",
		position = 2,
		section = ringsSection
	)
	default boolean ringOutline()
	{
		return true;
	}

	@ConfigItem(
		keyName = "ringBox",
		name = "Box",
		description = "Draw a rectangle around the inventory slot",
		position = 3,
		section = ringsSection
	)
	default boolean ringBox()
	{
		return false;
	}

	@ConfigItem(
		keyName = "ringFill",
		name = "Fill",
		description = "Fill the inventory slot with a translucent colour",
		position = 4,
		section = ringsSection
	)
	default boolean ringFill()
	{
		return false;
	}

	@Range(min = 0, max = 100)
	@Units(Units.PERCENT)
	@ConfigItem(
		keyName = "ringFillOpacity",
		name = "Fill opacity",
		description = "Opacity of the slot fill",
		position = 5,
		section = ringsSection
	)
	default int ringFillOpacity()
	{
		return 30;
	}

	@ConfigItem(
		keyName = "ringUnderline",
		name = "Underline",
		description = "Draw a coloured bar under the inventory slot",
		position = 6,
		section = ringsSection
	)
	default boolean ringUnderline()
	{
		return false;
	}

	@ConfigItem(
		keyName = "ringPulse",
		name = "Pulse",
		description = "Pulse the highlight between the colour and transparent (see General for the speed)",
		position = 7,
		section = ringsSection
	)
	default boolean ringPulse()
	{
		return false;
	}

	// ---------------------------------------------------------------- spec items

	@ConfigItem(
		keyName = "highlightSpecItems",
		name = "Highlight spec items",
		description = "Also highlight special attack weapons in your inventory when spec is full",
		position = 0,
		section = specItemsSection
	)
	default boolean highlightSpecItems()
	{
		return false;
	}

	@ConfigItem(
		keyName = "specItemList",
		name = "Spec items",
		description = "Special attack items to highlight when spec is full. One per line, * wildcards allowed, e.g. Dragon dagger*",
		position = 1,
		section = specItemsSection
	)
	default String specItemList()
	{
		return DEFAULT_SPEC_ITEMS;
	}

	@ConfigItem(
		keyName = "specSkipIfWielded",
		name = "Skip when spec weapon wielded",
		description = "Don't highlight spec items in the inventory while a listed spec weapon is already equipped",
		position = 2,
		section = specItemsSection
	)
	default boolean specSkipIfWielded()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
		keyName = "specItemColor",
		name = "Colour",
		description = "Colour used for the spec item highlight",
		position = 3,
		section = specItemsSection
	)
	default Color specItemColor()
	{
		return LIGHT_BLUE;
	}

	@ConfigItem(
		keyName = "specItemOutline",
		name = "Outline",
		description = "Draw a coloured outline around the item sprite",
		position = 4,
		section = specItemsSection
	)
	default boolean specItemOutline()
	{
		return true;
	}

	@ConfigItem(
		keyName = "specItemBox",
		name = "Box",
		description = "Draw a rectangle around the inventory slot",
		position = 5,
		section = specItemsSection
	)
	default boolean specItemBox()
	{
		return false;
	}

	@ConfigItem(
		keyName = "specItemFill",
		name = "Fill",
		description = "Fill the inventory slot with a translucent colour",
		position = 6,
		section = specItemsSection
	)
	default boolean specItemFill()
	{
		return false;
	}

	@Range(min = 0, max = 100)
	@Units(Units.PERCENT)
	@ConfigItem(
		keyName = "specItemFillOpacity",
		name = "Fill opacity",
		description = "Opacity of the slot fill",
		position = 7,
		section = specItemsSection
	)
	default int specItemFillOpacity()
	{
		return 30;
	}

	@ConfigItem(
		keyName = "specItemUnderline",
		name = "Underline",
		description = "Draw a coloured bar under the inventory slot",
		position = 8,
		section = specItemsSection
	)
	default boolean specItemUnderline()
	{
		return false;
	}

	@ConfigItem(
		keyName = "specItemPulse",
		name = "Pulse",
		description = "Pulse the highlight between the colour and transparent (see General for the speed)",
		position = 9,
		section = specItemsSection
	)
	default boolean specItemPulse()
	{
		return false;
	}

	// ---------------------------------------------------------------- spec orb

	@ConfigItem(
		keyName = "orbEnabled",
		name = "Highlight spec orb",
		description = "Decorate the special attack orb when spec is full and no listed ring is worn",
		position = 0,
		section = orbSection
	)
	default boolean orbEnabled()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
		keyName = "orbColor",
		name = "Colour",
		description = "Colour of the aura, outline and fill. The alpha sets the peak strength",
		position = 1,
		section = orbSection
	)
	default Color orbColor()
	{
		return new Color(255, 0, 0, 200);
	}

	@ConfigItem(
		keyName = "orbAura",
		name = "Aura",
		description = "Glowing halo around the outside of the orb",
		position = 2,
		section = orbSection
	)
	default boolean orbAura()
	{
		return true;
	}

	@Range(min = 2, max = 30)
	@ConfigItem(
		keyName = "orbAuraSize",
		name = "Aura size",
		description = "How far the aura extends beyond the orb edge, in pixels",
		position = 3,
		section = orbSection
	)
	default int orbAuraSize()
	{
		return 8;
	}

	@ConfigItem(
		keyName = "orbOutline",
		name = "Outline",
		description = "Ring drawn along the edge of the orb",
		position = 4,
		section = orbSection
	)
	default boolean orbOutline()
	{
		return false;
	}

	@ConfigItem(
		keyName = "orbFill",
		name = "Fill",
		description = "Tint the inside of the orb",
		position = 5,
		section = orbSection
	)
	default boolean orbFill()
	{
		return false;
	}

	@Range(min = 0, max = 100)
	@Units(Units.PERCENT)
	@ConfigItem(
		keyName = "orbFillOpacity",
		name = "Fill opacity",
		description = "Opacity of the orb tint",
		position = 6,
		section = orbSection
	)
	default int orbFillOpacity()
	{
		return 50;
	}

	@ConfigItem(
		keyName = "orbPulse",
		name = "Pulse",
		description = "Oscillate the orb decorations between the normal orb and the colour",
		position = 7,
		section = orbSection
	)
	default boolean orbPulse()
	{
		return true;
	}

	@Range(min = 200, max = 3000)
	@Units(Units.MILLISECONDS)
	@ConfigItem(
		keyName = "orbPeriodMs",
		name = "Pulse period",
		description = "Time for one full pulse (normal -> colour -> normal)",
		position = 8,
		section = orbSection
	)
	default int orbPeriodMs()
	{
		return 800;
	}

	@ConfigItem(
		keyName = "orbPulseMode",
		name = "Pulse mode",
		description = "Smooth fade or hard blink",
		position = 9,
		section = orbSection
	)
	default OrbPulseMode orbPulseMode()
	{
		return OrbPulseMode.SMOOTH;
	}

	@ConfigItem(
		keyName = "tintOrbText",
		name = "Tint orb text",
		description = "Also fade the spec percentage text towards the colour",
		position = 10,
		section = orbSection
	)
	default boolean tintOrbText()
	{
		return false;
	}
}
