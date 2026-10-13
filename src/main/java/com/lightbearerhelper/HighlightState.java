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

	// what a spec weapon's highlight should say
	public enum SpecStatus
	{
		// not enough spec, or a heal while healthy: nothing drawn
		NOT_READY,
		READY,
		// spec is there but the prayer points the special needs aren't
		LOW_PRAYER
	}

	// what the player has right now
	public static final class Vitals
	{
		final int specVarp;
		final int prayerPoints;
		final int hitpoints;
		final boolean liquidAdrenaline;

		public Vitals(int specVarp, int prayerPoints, int hitpoints, boolean liquidAdrenaline)
		{
			this.specVarp = specVarp;
			this.prayerPoints = prayerPoints;
			this.hitpoints = hitpoints;
			this.liquidAdrenaline = liquidAdrenaline;
		}
	}

	// the settings that bear on a spec weapon
	public static final class SpecRules
	{
		final int readyPercent;
		final boolean useCost;
		final boolean halveOnAdrenaline;
		// heals only highlight below this many hitpoints, 0 for always
		final int healBelowHp;

		public SpecRules(int readyPercent, boolean useCost, boolean halveOnAdrenaline, int healBelowHp)
		{
			this.readyPercent = readyPercent;
			this.useCost = useCost;
			this.halveOnAdrenaline = halveOnAdrenaline;
			this.healBelowHp = healBelowHp;
		}
	}

	private HighlightState()
	{
	}

	public static boolean isSpecFull(int specialAttackPercentVarp)
	{
		return specialAttackPercentVarp >= SPEC_FULL;
	}

	// varp is 0..1000, threshold is a percentage
	public static boolean isSpecReady(int specialAttackPercentVarp, int thresholdPercent)
	{
		return specialAttackPercentVarp >= thresholdVarp(thresholdPercent);
	}

	private static int thresholdVarp(int thresholdPercent)
	{
		return Math.max(1, Math.min(100, thresholdPercent)) * 10;
	}

	// spec energy a weapon needs, in varp units. The weapon's own cost when asked for and known, else the ready threshold
	public static int specNeeded(SpecCosts.Cost cost, SpecRules rules, boolean liquidAdrenaline)
	{
		if (!rules.useCost || cost == null)
		{
			return thresholdVarp(rules.readyPercent);
		}
		int needed = Math.max(0, Math.min(100, cost.getSpecPercent())) * 10;
		return rules.halveOnAdrenaline && liquidAdrenaline ? needed / 2 : needed;
	}

	public static SpecStatus specStatus(SpecCosts.Cost cost, SpecRules rules, Vitals vitals)
	{
		if (vitals.specVarp < specNeeded(cost, rules, vitals.liquidAdrenaline))
		{
			return SpecStatus.NOT_READY;
		}
		if (cost != null && cost.isHeal() && rules.healBelowHp > 0 && vitals.hitpoints >= rules.healBelowHp)
		{
			return SpecStatus.NOT_READY;
		}
		if (cost != null && cost.getPrayerPoints() > 0 && vitals.prayerPoints < cost.getPrayerPoints())
		{
			return SpecStatus.LOW_PRAYER;
		}
		return SpecStatus.READY;
	}

	// READY beats LOW_PRAYER beats NOT_READY
	public static SpecStatus better(SpecStatus a, SpecStatus b)
	{
		if (a == SpecStatus.READY || b == SpecStatus.READY)
		{
			return SpecStatus.READY;
		}
		if (a == SpecStatus.LOW_PRAYER || b == SpecStatus.LOW_PRAYER)
		{
			return SpecStatus.LOW_PRAYER;
		}
		return SpecStatus.NOT_READY;
	}

	public static boolean orbActive(boolean specReady, boolean keepAfterSwap, boolean swapDone)
	{
		return specReady && (keepAfterSwap || !swapDone);
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
