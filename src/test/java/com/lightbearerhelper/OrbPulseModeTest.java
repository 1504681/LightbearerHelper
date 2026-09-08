package com.lightbearerhelper;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class OrbPulseModeTest
{
	@Test
	public void smoothStartsAtZeroPeaksHalfwayAndReturns()
	{
		assertEquals(0f, OrbPulseMode.SMOOTH.intensity(0, 800), 1e-4);
		assertEquals(1f, OrbPulseMode.SMOOTH.intensity(400, 800), 1e-4);
		assertEquals(0f, OrbPulseMode.SMOOTH.intensity(800, 800), 1e-4);
		assertEquals(0.5f, OrbPulseMode.SMOOTH.intensity(200, 800), 1e-4);
	}

	@Test
	public void blinkIsOnForFirstHalfOnly()
	{
		assertEquals(1f, OrbPulseMode.BLINK.intensity(0, 800), 0f);
		assertEquals(1f, OrbPulseMode.BLINK.intensity(399, 800), 0f);
		assertEquals(0f, OrbPulseMode.BLINK.intensity(400, 800), 0f);
		assertEquals(0f, OrbPulseMode.BLINK.intensity(799, 800), 0f);
		assertEquals(1f, OrbPulseMode.BLINK.intensity(800, 800), 0f);
	}

	@Test
	public void tinyPeriodIsClamped()
	{
		// period below 100ms is treated as 100ms rather than dividing by something silly
		assertEquals(1f, OrbPulseMode.SMOOTH.intensity(50, 1), 1e-4);
	}
}
