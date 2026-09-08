package com.lightbearerhelper;

public enum OrbPulseMode
{
	SMOOTH("Smooth"),
	BLINK("Blink");

	private final String label;

	OrbPulseMode(String label)
	{
		this.label = label;
	}

	// 0 = nothing showing, 1 = full colour. periodMs is one whole cycle
	public float intensity(long nowMs, int periodMs)
	{
		int period = Math.max(100, periodMs);
		double t = (double) Math.floorMod(nowMs, (long) period) / period;
		switch (this)
		{
			case BLINK:
				return t < 0.5 ? 1f : 0f;
			case SMOOTH:
			default:
				return (float) (0.5 - 0.5 * Math.cos(2 * Math.PI * t));
		}
	}

	@Override
	public String toString()
	{
		return label;
	}
}
