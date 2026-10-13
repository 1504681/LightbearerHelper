package com.lightbearerhelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class SpecCostsTest
{
	@Test
	public void knownWeapons()
	{
		assertEquals(50, SpecCosts.of("Dragon claws").getSpecPercent());
		assertEquals(25, SpecCosts.of("Dragon dagger(p++)").getSpecPercent());
		assertEquals(75, SpecCosts.of("Zaryte crossbow").getSpecPercent());
		assertEquals(50, SpecCosts.of("Toxic blowpipe (empty)").getSpecPercent());
		assertEquals(100, SpecCosts.of("Dragon pickaxe").getSpecPercent());
	}

	@Test
	public void variantsBeforeTheirGeneralPattern()
	{
		assertEquals(50, SpecCosts.of("Granite maul (ornate handle)").getSpecPercent());
		assertEquals(60, SpecCosts.of("Granite maul").getSpecPercent());
		assertEquals(50, SpecCosts.of("Magic shortbow (i)").getSpecPercent());
		assertEquals(55, SpecCosts.of("Magic shortbow").getSpecPercent());
	}

	@Test
	public void kerisOfTheSunCostsPrayerAndHeals()
	{
		SpecCosts.Cost keris = SpecCosts.of("Keris partisan of the sun");
		assertEquals(75, keris.getSpecPercent());
		assertEquals(50, keris.getPrayerPoints());
		assertTrue(keris.isHeal());

		SpecCosts.Cost breach = SpecCosts.of("Keris partisan of corruption");
		assertEquals(0, breach.getPrayerPoints());
		assertFalse(breach.isHeal());
	}

	@Test
	public void unknownIsNull()
	{
		assertNull(SpecCosts.of("Rune scimitar"));
		assertNull(SpecCosts.of("Bronze dagger"));
		assertNull(SpecCosts.of(null));
	}

	@Test
	public void listedPercentOverridesTheTableButKeepsTheRest()
	{
		SpecCosts.Cost keris = SpecCosts.of("Keris partisan of the sun", 40);
		assertEquals(40, keris.getSpecPercent());
		assertEquals(50, keris.getPrayerPoints());
		assertTrue(keris.isHeal());

		SpecCosts.Cost custom = SpecCosts.of("Bronze dagger", 30);
		assertEquals(30, custom.getSpecPercent());
		assertEquals(0, custom.getPrayerPoints());

		assertNull(SpecCosts.of("Bronze dagger", null));
	}
}
