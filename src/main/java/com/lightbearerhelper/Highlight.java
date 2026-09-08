package com.lightbearerhelper;

import java.awt.Color;

/**
 * A resolved highlight for one item: how to draw it and in which colour.
 */
public final class Highlight
{
	private final HighlightStyle style;
	private final Color color;

	public Highlight(HighlightStyle style, Color color)
	{
		this.style = style;
		this.color = color;
	}

	public HighlightStyle getStyle()
	{
		return style;
	}

	public Color getColor()
	{
		return color;
	}
}
