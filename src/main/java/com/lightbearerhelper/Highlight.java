package com.lightbearerhelper;

import java.awt.Color;

/**
 * A resolved highlight for one item: which of outline / fill / underline to draw, in which colour, and whether
 * the whole thing pulses.
 */
public final class Highlight
{
	private final Color color;
	private final boolean outline;
	private final boolean fill;
	private final int fillOpacityPercent;
	private final boolean underline;
	private final boolean pulse;

	public Highlight(Color color, boolean outline, boolean fill, int fillOpacityPercent,
		boolean underline, boolean pulse)
	{
		this.color = color;
		this.outline = outline;
		this.fill = fill;
		this.fillOpacityPercent = Math.max(0, Math.min(100, fillOpacityPercent));
		this.underline = underline;
		this.pulse = pulse;
	}

	public Color getColor()
	{
		return color;
	}

	public boolean isOutline()
	{
		return outline;
	}

	public boolean isFill()
	{
		return fill;
	}

	public int getFillOpacityPercent()
	{
		return fillOpacityPercent;
	}

	public boolean isUnderline()
	{
		return underline;
	}

	public boolean isPulse()
	{
		return pulse;
	}

	/** Fill colour with the configured opacity applied on top of the colour's own alpha. */
	public Color getFillColor()
	{
		int alpha = Math.round(color.getAlpha() * fillOpacityPercent / 100f);
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, Math.min(255, alpha)));
	}
}
