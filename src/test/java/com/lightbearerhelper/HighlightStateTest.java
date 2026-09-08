package com.lightbearerhelper;

import static com.lightbearerhelper.HighlightState.Mode.IDLE;
import static com.lightbearerhelper.HighlightState.Mode.WANT_LIGHTBEARER;
import static com.lightbearerhelper.HighlightState.Mode.WANT_OTHER_RING;
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
}
