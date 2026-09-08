package com.lightbearerhelper;

/**
 * Pure decision logic for what the plugin should be nagging about. Kept free of RuneLite
 * client types so it can be unit tested.
 */
public final class HighlightState
{
	/** Special attack energy varp value that means 100%. */
	public static final int SPEC_FULL = 1000;

	public enum Mode
	{
		/** Nothing to do. */
		IDLE,
		/** Spec is regenerating and the Lightbearer is not worn: highlight it. */
		WANT_LIGHTBEARER,
		/** Spec is full and no listed ring is worn: highlight the other rings (and spec items / orb). */
		WANT_OTHER_RING
	}

	private HighlightState()
	{
	}

	public static boolean isSpecFull(int specialAttackPercentVarp)
	{
		return specialAttackPercentVarp >= SPEC_FULL;
	}

	/**
	 * @param specFull           special attack energy is at 100%
	 * @param wornIsLightbearer  the ring slot holds a Lightbearer
	 * @param wornIsListedRing   the ring slot holds one of the configured "other rings"
	 */
	public static Mode resolve(boolean specFull, boolean wornIsLightbearer, boolean wornIsListedRing)
	{
		if (!specFull)
		{
			return wornIsLightbearer ? Mode.IDLE : Mode.WANT_LIGHTBEARER;
		}
		return wornIsListedRing ? Mode.IDLE : Mode.WANT_OTHER_RING;
	}
}
