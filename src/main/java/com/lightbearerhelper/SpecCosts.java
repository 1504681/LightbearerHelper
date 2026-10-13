package com.lightbearerhelper;

import net.runelite.client.util.WildcardMatcher;

// what a special attack costs, from the wiki. Looked up by item name, first pattern that fits wins
public final class SpecCosts
{
	public static final class Cost
	{
		private final int specPercent;
		private final int prayerPoints;
		// a heal: only worth it while hurt
		private final boolean heals;

		Cost(int specPercent, int prayerPoints, boolean heals)
		{
			this.specPercent = specPercent;
			this.prayerPoints = prayerPoints;
			this.heals = heals;
		}

		public int getSpecPercent()
		{
			return specPercent;
		}

		public int getPrayerPoints()
		{
			return prayerPoints;
		}

		public boolean isHeal()
		{
			return heals;
		}

		Cost withSpecPercent(int percent)
		{
			return new Cost(percent, prayerPoints, heals);
		}
	}

	private static final Cost KERIS_OF_THE_SUN = new Cost(75, 50, true);

	// lowercase wildcard pattern, cost. Specific variants before their general pattern
	private static final Object[][] TABLE = {
		{"keris partisan of the sun", KERIS_OF_THE_SUN},
		{"keris partisan of corruption", 75},
		{"granite maul (ornate handle)", 50},
		{"granite maul*", 60},
		{"magic shortbow (i)", 50},
		{"magic shortbow", 55},
		{"magic longbow", 35},
		{"magic comp bow", 35},
		{"dragon claws*", 50},
		{"burning claws*", 35},
		{"rune claws", 25},
		{"dragon dagger*", 25},
		{"abyssal dagger*", 25},
		{"dragon halberd", 30},
		{"crystal halberd*", 30},
		{"noxious halberd", 50},
		{"dragon warhammer*", 50},
		{"elder maul*", 50},
		{"armadyl godsword*", 50},
		{"bandos godsword*", 50},
		{"saradomin godsword*", 50},
		{"zamorak godsword*", 50},
		{"ancient godsword*", 50},
		{"saradomin sword", 100},
		{"saradomin's blessed sword", 65},
		{"voidwaker*", 50},
		{"dragon longsword*", 25},
		{"dragon mace*", 25},
		{"dragon sword*", 40},
		{"dragon scimitar*", 55},
		{"dragon 2h sword*", 60},
		{"dragon battleaxe*", 100},
		{"dragon spear*", 25},
		{"zamorakian spear", 25},
		{"zamorakian hasta", 25},
		{"dragon knife*", 25},
		{"dragon thrownaxe", 25},
		{"rune thrownaxe", 10},
		{"abyssal whip*", 50},
		{"abyssal tentacle*", 50},
		{"abyssal bludgeon*", 50},
		{"osmumten's fang*", 25},
		{"arclight", 50},
		{"emberlight", 50},
		{"darklight", 50},
		{"ancient mace", 100},
		{"barrelchest anchor*", 50},
		{"bone dagger*", 75},
		{"brine sabre", 75},
		{"excalibur", 100},
		{"statius's warhammer*", 35},
		{"vesta's longsword*", 25},
		{"vesta's spear*", 50},
		{"morrigan's throwing axe*", 50},
		{"morrigan's javelin*", 50},
		{"dinh's bulwark", 50},
		{"ursine chainmace*", 50},
		{"webweaver bow*", 50},
		{"accursed sceptre*", 50},
		{"dual macuahuitl*", 25},
		{"eclipse atlatl*", 50},
		{"blue moon spear*", 50},
		{"tonalztics of ralos*", 50},
		{"eye of ayak*", 50},
		{"sunspear*", 50},
		{"arkan blade*", 30},
		{"soulflame horn*", 25},
		{"dawnbringer", 35},
		{"toxic blowpipe*", 50},
		{"blazing blowpipe*", 50},
		{"rosewood blowpipe*", 25},
		{"zaryte crossbow*", 75},
		{"armadyl crossbow*", 50},
		{"dragon crossbow*", 60},
		{"dorgeshuun crossbow", 75},
		{"dark bow*", 55},
		{"scorching bow*", 25},
		{"seercull", 100},
		{"heavy ballista*", 65},
		{"light ballista*", 65},
		{"eldritch nightmare staff", 75},
		{"volatile nightmare staff", 55},
		{"staff of the dead", 100},
		{"toxic staff of the dead", 100},
		{"staff of light", 100},
		{"staff of balance", 100},
		{"purging staff", 25},
		{"crystal dagger (perfected)", 25},
		{"fang of the hound*", 50},
		{"infernal tecpatl*", 50},
		{"thunder khopesh*", 50},
		{"crimson kisten*", 50},
		{"the dogsword*", 50},
		{"granite hammer", 60},
		{"dragon axe*", 100},
		{"dragon felling axe*", 100},
		{"dragon pickaxe*", 100},
		{"dragon harpoon*", 100},
		{"infernal axe*", 100},
		{"infernal pickaxe*", 100},
		{"infernal harpoon*", 100},
		{"crystal axe*", 100},
		{"crystal pickaxe*", 100},
		{"crystal harpoon*", 100},
		{"3rd age axe", 100},
		{"3rd age pickaxe", 100},
	};

	private SpecCosts()
	{
	}

	// null when the weapon isn't in the table
	public static Cost of(String itemName)
	{
		if (itemName == null)
		{
			return null;
		}
		String name = itemName.toLowerCase();
		for (Object[] row : TABLE)
		{
			if (WildcardMatcher.matches((String) row[0], name))
			{
				Object cost = row[1];
				return cost instanceof Cost ? (Cost) cost : new Cost((Integer) cost, 0, false);
			}
		}
		return null;
	}

	// the table's entry, with the spec percentage replaced when the list gives one
	public static Cost of(String itemName, Integer listedPercent)
	{
		Cost cost = of(itemName);
		if (listedPercent == null)
		{
			return cost;
		}
		return cost == null ? new Cost(listedPercent, 0, false) : cost.withSpecPercent(listedPercent);
	}
}
