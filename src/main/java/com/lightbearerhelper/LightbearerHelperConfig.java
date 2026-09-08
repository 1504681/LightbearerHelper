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

	// ---------------------------------------------------------------- sections

	@ConfigSection(
		name = "Lightbearer",
		description = "Highlight the Lightbearer while special attack is below 100%",
		position = 0
	)
	String lightbearerSection = "lightbearer";

	@ConfigSection(
		name = "Other rings",
		description = "Rings to highlight once special attack is at 100% and the Lightbearer is still worn",
		position = 1
	)
	String ringsSection = "rings";

	@ConfigSection(
		name = "Spec items",
		description = "Special attack weapons to highlight once special attack is at 100%",
		position = 2
	)
	String specItemsSection = "specItems";

	@ConfigSection(
		name = "Spec orb",
		description = "Pulse the special attack orb colour once special attack is at 100%",
		position = 3
	)
	String orbSection = "orb";

	// ---------------------------------------------------------------- lightbearer

	@ConfigItem(
		keyName = "lightbearerStyle",
		name = "Highlight style",
		description = "How the Lightbearer is highlighted in your inventory while spec is regenerating",
		position = 0,
		section = lightbearerSection
	)
	default HighlightStyle lightbearerStyle()
	{
		return HighlightStyle.OUTLINE;
	}

	@Alpha
	@ConfigItem(
		keyName = "lightbearerColor",
		name = "Highlight colour",
		description = "Colour used for the Lightbearer highlight",
		position = 1,
		section = lightbearerSection
	)
	default Color lightbearerColor()
	{
		return new Color(0, 255, 0, 255);
	}

	@ConfigItem(
		keyName = "highlightWornRing",
		name = "Mark worn ring to swap",
		description = "Also highlight the ring in the worn equipment tab that should be swapped out",
		position = 2,
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

	@ConfigItem(
		keyName = "ringStyle",
		name = "Highlight style",
		description = "How the other rings are highlighted in your inventory when spec is full",
		position = 1,
		section = ringsSection
	)
	default HighlightStyle ringStyle()
	{
		return HighlightStyle.OUTLINE;
	}

	@Alpha
	@ConfigItem(
		keyName = "ringColor",
		name = "Highlight colour",
		description = "Colour used for the other-ring highlight",
		position = 2,
		section = ringsSection
	)
	default Color ringColor()
	{
		return new Color(0, 255, 255, 255);
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

	@ConfigItem(
		keyName = "specItemStyle",
		name = "Highlight style",
		description = "How spec items are highlighted",
		position = 3,
		section = specItemsSection
	)
	default HighlightStyle specItemStyle()
	{
		return HighlightStyle.BOX;
	}

	@Alpha
	@ConfigItem(
		keyName = "specItemColor",
		name = "Highlight colour",
		description = "Colour used for the spec item highlight",
		position = 4,
		section = specItemsSection
	)
	default Color specItemColor()
	{
		return new Color(255, 165, 0, 255);
	}

	// ---------------------------------------------------------------- spec orb

	@ConfigItem(
		keyName = "pulseOrb",
		name = "Pulse spec orb",
		description = "Oscillate the special attack orb between its normal colour and the pulse colour when spec is full and no listed ring is worn",
		position = 0,
		section = orbSection
	)
	default boolean pulseOrb()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
		keyName = "orbColor",
		name = "Pulse colour",
		description = "Colour the orb pulses towards. The alpha controls how strong the tint gets at its peak",
		position = 1,
		section = orbSection
	)
	default Color orbColor()
	{
		return new Color(255, 0, 0, 170);
	}

	@Range(min = 200, max = 3000)
	@Units(Units.MILLISECONDS)
	@ConfigItem(
		keyName = "orbPeriodMs",
		name = "Pulse period",
		description = "Time for one full pulse (normal -> colour -> normal)",
		position = 2,
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
		position = 3,
		section = orbSection
	)
	default OrbPulseMode orbPulseMode()
	{
		return OrbPulseMode.SMOOTH;
	}

	@ConfigItem(
		keyName = "tintOrbText",
		name = "Tint orb text",
		description = "Also fade the spec percentage text towards the pulse colour",
		position = 4,
		section = orbSection
	)
	default boolean tintOrbText()
	{
		return false;
	}
}
