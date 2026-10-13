package com.lightbearerhelper;

import static com.lightbearerhelper.HighlightState.Mode.IDLE;
import static com.lightbearerhelper.HighlightState.Mode.WANT_LIGHTBEARER;
import static com.lightbearerhelper.HighlightState.Mode.WANT_OTHER_RING;
import static com.lightbearerhelper.HighlightState.SpecStatus.LOW_PRAYER;
import static com.lightbearerhelper.HighlightState.SpecStatus.NOT_READY;
import static com.lightbearerhelper.HighlightState.SpecStatus.READY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class HighlightStateTest
{
	@Test
	public void specFullThreshold()
	{
		assertTrue(HighlightState.isSpecFull(1000));
		assertFalse(HighlightState.isSpecFull(999));
		assertFalse(HighlightState.isSpecFull(0));
	}

	@Test
	public void regeneratingWithoutLightbearerWantsLightbearer()
	{
		assertEquals(WANT_LIGHTBEARER, HighlightState.resolve(false, false, false));
		// wearing an "other" ring while spec regenerates still means swap to lightbearer
		assertEquals(WANT_LIGHTBEARER, HighlightState.resolve(false, false, true));
	}

	@Test
	public void regeneratingWithLightbearerIsIdle()
	{
		assertEquals(IDLE, HighlightState.resolve(false, true, false));
	}

	@Test
	public void fullSpecWithLightbearerWantsOtherRing()
	{
		assertEquals(WANT_OTHER_RING, HighlightState.resolve(true, true, false));
	}

	@Test
	public void fullSpecWithNoRingOrUnlistedRingWantsOtherRing()
	{
		assertEquals(WANT_OTHER_RING, HighlightState.resolve(true, false, false));
	}

	@Test
	public void fullSpecWithListedRingIsIdle()
	{
		assertEquals(IDLE, HighlightState.resolve(true, false, true));
	}

	@Test
	public void specReadyThreshold()
	{
		assertFalse(HighlightState.isSpecReady(999, 100));
		assertTrue(HighlightState.isSpecReady(1000, 100));
		assertFalse(HighlightState.isSpecReady(499, 50));
		assertTrue(HighlightState.isSpecReady(500, 50));
		// out of range percentages are clamped
		assertTrue(HighlightState.isSpecReady(10, 0));
		assertFalse(HighlightState.isSpecReady(999, 250));
	}

	@Test
	public void orbStopsAfterSwapUnlessKeptOn()
	{
		assertTrue(HighlightState.orbActive(true, false, false));
		assertFalse(HighlightState.orbActive(true, false, true));
		assertTrue(HighlightState.orbActive(true, true, true));
		assertFalse(HighlightState.orbActive(false, true, true));
	}

	private static final SpecCosts.Cost CLAWS = SpecCosts.of("Dragon claws");
	private static final SpecCosts.Cost KERIS = SpecCosts.of("Keris partisan of the sun");
	private static final HighlightState.SpecRules BY_COST = new HighlightState.SpecRules(100, true, true, 40);
	private static final HighlightState.SpecRules BY_THRESHOLD = new HighlightState.SpecRules(100, false, true, 40);

	private static HighlightState.Vitals vitals(int spec, int prayer, int hp, boolean adrenaline)
	{
		return new HighlightState.Vitals(spec, prayer, hp, adrenaline);
	}

	@Test
	public void weaponCostDecidesWhenAsked()
	{
		assertEquals(READY, HighlightState.specStatus(CLAWS, BY_COST, vitals(500, 99, 99, false)));
		assertEquals(NOT_READY, HighlightState.specStatus(CLAWS, BY_COST, vitals(490, 99, 99, false)));
		// off: the ready threshold rules, as before
		assertEquals(NOT_READY, HighlightState.specStatus(CLAWS, BY_THRESHOLD, vitals(990, 99, 99, false)));
		assertEquals(READY, HighlightState.specStatus(CLAWS, BY_THRESHOLD, vitals(1000, 99, 99, false)));
	}

	@Test
	public void unknownWeaponFallsBackToTheThreshold()
	{
		HighlightState.SpecRules rules = new HighlightState.SpecRules(60, true, true, 0);
		assertEquals(NOT_READY, HighlightState.specStatus(null, rules, vitals(590, 99, 99, false)));
		assertEquals(READY, HighlightState.specStatus(null, rules, vitals(600, 99, 99, false)));
	}

	@Test
	public void liquidAdrenalineHalvesTheCost()
	{
		assertEquals(READY, HighlightState.specStatus(CLAWS, BY_COST, vitals(250, 99, 99, true)));
		assertEquals(NOT_READY, HighlightState.specStatus(CLAWS, BY_COST, vitals(240, 99, 99, true)));
		// keris 75% -> 37.5%
		assertEquals(READY, HighlightState.specStatus(KERIS, BY_COST, vitals(375, 99, 10, true)));
		assertEquals(NOT_READY, HighlightState.specStatus(KERIS, BY_COST, vitals(370, 99, 10, true)));
		// not when turned off, and never for the plain threshold
		HighlightState.SpecRules noAdrenaline = new HighlightState.SpecRules(100, true, false, 40);
		assertEquals(NOT_READY, HighlightState.specStatus(CLAWS, noAdrenaline, vitals(250, 99, 99, true)));
		assertEquals(NOT_READY, HighlightState.specStatus(CLAWS, BY_THRESHOLD, vitals(500, 99, 99, true)));
	}

	@Test
	public void kerisNeedsPrayerAndAWound()
	{
		assertEquals(READY, HighlightState.specStatus(KERIS, BY_COST, vitals(750, 50, 39, false)));
		assertEquals(LOW_PRAYER, HighlightState.specStatus(KERIS, BY_COST, vitals(750, 49, 39, false)));
		assertEquals(NOT_READY, HighlightState.specStatus(KERIS, BY_COST, vitals(740, 49, 39, false)));
		// healthy: no highlight at all, whatever the prayer
		assertEquals(NOT_READY, HighlightState.specStatus(KERIS, BY_COST, vitals(1000, 99, 40, false)));
		assertEquals(NOT_READY, HighlightState.specStatus(KERIS, BY_COST, vitals(1000, 10, 99, false)));
		// the hp rule off
		HighlightState.SpecRules always = new HighlightState.SpecRules(100, true, true, 0);
		assertEquals(READY, HighlightState.specStatus(KERIS, always, vitals(1000, 99, 99, false)));
		// prayer and hp still count with the threshold rule
		assertEquals(LOW_PRAYER, HighlightState.specStatus(KERIS, BY_THRESHOLD, vitals(1000, 20, 20, false)));
		assertEquals(NOT_READY, HighlightState.specStatus(KERIS, BY_THRESHOLD, vitals(1000, 99, 99, false)));
	}

	@Test
	public void betterPrefersReadyThenLowPrayer()
	{
		assertEquals(READY, HighlightState.better(LOW_PRAYER, READY));
		assertEquals(READY, HighlightState.better(READY, NOT_READY));
		assertEquals(LOW_PRAYER, HighlightState.better(NOT_READY, LOW_PRAYER));
		assertEquals(NOT_READY, HighlightState.better(NOT_READY, NOT_READY));
	}
}
