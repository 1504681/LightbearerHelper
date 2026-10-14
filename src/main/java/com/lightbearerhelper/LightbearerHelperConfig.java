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
		+ "Toxic blowpipe*\nEldritch nightmare staff\nVolatile nightmare staff\nKeris partisan of the sun";

	Color DEFAULT_COLOR = new Color(0xB3, 0xA1, 0x6C, 0xC8);
	String DEFAULT_PRAYER_RESTORES = "Prayer potion*\nSuper restore*\nTears of Elidinis*";

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
		return 2000;
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

	@Range(min = 1, max = 100)
	@Units(Units.PERCENT)
	@ConfigItem(
		keyName = "specReadyPercent",
		name = "Spec ready at",
		description = "Special attack energy that counts as ready for the spec orb and spec item highlights. The ring swap itself always waits for 100%",
		position = 2,
		section = generalSection
	)
	default int specReadyPercent()
	{
		return 100;
	}

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
		return DEFAULT_COLOR;
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
		keyName = "lightbearerFill",
		name = "Fill",
		description = "Tint the item itself with a translucent colour",
		position = 2,
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
		description = "Opacity of the item tint",
		position = 3,
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
		position = 4,
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
		position = 5,
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
		position = 6,
		section = lightbearerSection
	)
	default boolean highlightWornRing()
	{
		return false;
	}

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
		return DEFAULT_COLOR;
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
		keyName = "ringFill",
		name = "Fill",
		description = "Tint the item itself with a translucent colour",
		position = 3,
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
		description = "Opacity of the item tint",
		position = 4,
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
		position = 5,
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
		position = 6,
		section = ringsSection
	)
	default boolean ringPulse()
	{
		return false;
	}

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
		keyName = "specItemsAlways",
		name = "Even without the ring swap",
		description = "Highlight spec items whenever spec is ready, not only while waiting to swap rings. Turn off to tie them to the Lightbearer swap",
		position = 1,
		section = specItemsSection
	)
	default boolean specItemsAlways()
	{
		return true;
	}

	@ConfigItem(
		keyName = "specItemList",
		name = "Spec items",
		description = "Special attack items to highlight when spec is full. One per line, * wildcards allowed, e.g. Dragon dagger*. End a line in a percentage to set that weapon's spec cost for Only when affordable, e.g. Dragon claws 50%",
		position = 2,
		section = specItemsSection
	)
	default String specItemList()
	{
		return DEFAULT_SPEC_ITEMS;
	}

	@ConfigItem(
		keyName = "specAffordable",
		name = "Only when affordable",
		description = "Highlight a spec weapon once your spec energy covers its own special attack, instead of waiting for Spec ready at. Costs come from a built-in table; a line in the list can set one, e.g. Dragon claws 50%. Weapons not in the table use Spec ready at. The spec orb follows the weapons you carry",
		position = 3,
		section = specItemsSection
	)
	default boolean specAffordable()
	{
		return false;
	}

	@ConfigItem(
		keyName = "specAdrenaline",
		name = "Halve costs on Liquid adrenaline",
		description = "While the Tombs of Amascut liquid adrenaline is active, special attacks cost half, so count them as affordable at half the energy",
		position = 4,
		section = specItemsSection
	)
	default boolean specAdrenaline()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
		keyName = "lowPrayerColor",
		name = "Short of prayer colour",
		description = "Colour for a spec weapon whose special also costs prayer points you don't have, the Keris partisan of the sun's 50. Spec is ready but the special won't go off. The orb keeps its own colour. Also used for the prayer restores below",
		position = 5,
		section = specItemsSection
	)
	default Color lowPrayerColor()
	{
		return new Color(0xA8, 0x50, 0xE8, 0xC8);
	}

	@ConfigItem(
		keyName = "restoresOnLowPrayer",
		name = "Light prayer restores when short",
		description = "While a spec weapon you carry is ready but short of prayer points, highlight the prayer restores in your inventory in the same colour",
		position = 6,
		section = specItemsSection
	)
	default boolean restoresOnLowPrayer()
	{
		return false;
	}

	@ConfigItem(
		keyName = "prayerRestoreList",
		name = "Prayer restores",
		description = "What counts as a prayer restore. One per line, * wildcards allowed",
		position = 7,
		section = specItemsSection
	)
	default String prayerRestoreList()
	{
		return DEFAULT_PRAYER_RESTORES;
	}

	@ConfigItem(
		keyName = "healOnlyWhenHurt",
		name = "Keris of the sun only when hurt",
		description = "Only highlight the Keris partisan of the sun, whose special is a heal, while your hitpoints are below the value below",
		position = 8,
		section = specItemsSection
	)
	default boolean healOnlyWhenHurt()
	{
		return true;
	}

	@Range(min = 1, max = 99)
	@ConfigItem(
		keyName = "healBelowHp",
		name = "Hurt means below",
		description = "Hitpoints under which the Keris partisan of the sun counts as worth using",
		position = 9,
		section = specItemsSection
	)
	default int healBelowHp()
	{
		return 40;
	}

	@ConfigItem(
		keyName = "healAlwaysMark",
		name = "Always mark Keris of the sun",
		description = "Keep the Keris partisan of the sun marked in the colour below at all times, so it's found at a glance. The spec colours take over while its special is ready",
		position = 10,
		section = specItemsSection
	)
	default boolean healAlwaysMark()
	{
		return false;
	}

	@Alpha
	@ConfigItem(
		keyName = "healMarkColor",
		name = "Keris mark colour",
		description = "Colour of the standing mark on the Keris partisan of the sun",
		position = 11,
		section = specItemsSection
	)
	default Color healMarkColor()
	{
		return new Color(0xF0, 0xA0, 0x30, 0xC8);
	}

	@ConfigItem(
		keyName = "specSkipIfWielded",
		name = "Skip when spec weapon wielded",
		description = "Don't highlight spec items in the inventory while a listed spec weapon is already equipped",
		position = 12,
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
		position = 13,
		section = specItemsSection
	)
	default Color specItemColor()
	{
		return DEFAULT_COLOR;
	}

	@ConfigItem(
		keyName = "specItemOutline",
		name = "Outline",
		description = "Draw a coloured outline around the item sprite",
		position = 14,
		section = specItemsSection
	)
	default boolean specItemOutline()
	{
		return true;
	}

	@ConfigItem(
		keyName = "specItemFill",
		name = "Fill",
		description = "Tint the item itself with a translucent colour",
		position = 15,
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
		description = "Opacity of the item tint",
		position = 16,
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
		position = 17,
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
		position = 18,
		section = specItemsSection
	)
	default boolean specItemPulse()
	{
		return false;
	}

	@ConfigItem(
		keyName = "orbEnabled",
		name = "Highlight spec orb",
		description = "Decorate the special attack orb once spec reaches the threshold below",
		position = 0,
		section = orbSection
	)
	default boolean orbEnabled()
	{
		return true;
	}


	@ConfigItem(
		keyName = "orbAlways",
		name = "Keep showing after swap",
		description = "Keep the orb highlight going even once a listed ring (and, if spec items are enabled, a spec weapon) is equipped",
		position = 1,
		section = orbSection
	)
	default boolean orbAlways()
	{
		return false;
	}

	@ConfigItem(
		keyName = "orbRequireSpecItem",
		name = "Only with a spec item",
		description = "Only highlight the orb while one of the listed spec items is in your inventory or equipped, so it stays quiet on skilling trips",
		position = 2,
		section = orbSection
	)
	default boolean orbRequireSpecItem()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
		keyName = "orbColor",
		name = "Colour",
		description = "Colour of the aura, outline and fill. The alpha sets the peak strength",
		position = 3,
		section = orbSection
	)
	default Color orbColor()
	{
		return DEFAULT_COLOR;
	}

	@ConfigItem(
		keyName = "orbAura",
		name = "Aura",
		description = "Glowing halo around the outside of the orb",
		position = 4,
		section = orbSection
	)
	default boolean orbAura()
	{
		return false;
	}

	@Range(min = 2, max = 30)
	@ConfigItem(
		keyName = "orbAuraSize",
		name = "Aura size",
		description = "How far the aura extends beyond the orb edge, in pixels",
		position = 5,
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
		position = 6,
		section = orbSection
	)
	default boolean orbOutline()
	{
		return true;
	}

	@ConfigItem(
		keyName = "orbFill",
		name = "Fill",
		description = "Tint the inside of the orb",
		position = 7,
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
		position = 8,
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
		position = 9,
		section = orbSection
	)
	default boolean orbPulse()
	{
		return false;
	}

	@Range(min = 200, max = 3000)
	@Units(Units.MILLISECONDS)
	@ConfigItem(
		keyName = "orbPeriodMs",
		name = "Pulse period",
		description = "Time for one full pulse (normal -> colour -> normal)",
		position = 10,
		section = orbSection
	)
	default int orbPeriodMs()
	{
		return 2000;
	}

	@ConfigItem(
		keyName = "orbPulseMode",
		name = "Pulse mode",
		description = "Smooth fade or hard blink",
		position = 11,
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
		position = 12,
		section = orbSection
	)
	default boolean tintOrbText()
	{
		return false;
	}
}
