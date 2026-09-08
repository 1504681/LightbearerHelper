package com.lightbearerhelper;

// the decisions, kept away from the client so they can be tested
public final class HighlightState
{
	public static final int SPEC_FULL = 1000;

	public enum Mode
	{
		IDLE,
		WANT_LIGHTBEARER,
		WANT_OTHER_RING
	}

	private HighlightState()
	{
	}

	public static boolean isSpecFull(int specialAttackPercentVarp)
	{
		return specialAttackPercentVarp >= SPEC_FULL;
	}

	// varp is 0..1000, threshold is a percentage
	public static boolean orbActive(int specialAttackPercentVarp, int thresholdPercent, boolean keepAfterSwap, boolean swapDone)
	{
		int threshold = Math.max(1, Math.min(100, thresholdPercent)) * 10;
		if (specialAttackPercentVarp < threshold)
		{
			return false;
		}
		return keepAfterSwap || !swapDone;
	}

	public static Mode resolve(boolean specFull, boolean wornIsLightbearer, boolean wornIsListedRing)
	{
		if (!specFull)
		{
			return wornIsLightbearer ? Mode.IDLE : Mode.WANT_LIGHTBEARER;
		}
		return wornIsListedRing ? Mode.IDLE : Mode.WANT_OTHER_RING;
	}
}
