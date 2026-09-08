package com.lightbearerhelper;

/**
 * Waveform used to oscillate the spec orb colour.
 */
public enum OrbPulseMode
{
	/** Smooth fade between the normal orb and the chosen colour. */
	SMOOTH("Smooth"),
	/** Hard on/off blink. */
	BLINK("Blink");

	private final String label;

	OrbPulseMode(String label)
	{
		this.label = label;
	}

	/**
	 * Intensity of the chosen colour at a point in time.
	 *
	 * @param nowMs    current time in milliseconds
	 * @param periodMs full oscillation period (normal -> colour -> normal) in milliseconds
	 * @return 0.0 (normal orb, fully transparent overlay) .. 1.0 (chosen colour at its configured alpha)
	 */
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
