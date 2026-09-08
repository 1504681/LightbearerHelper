package com.lightbearerhelper;

/**
 * How an item is highlighted in the inventory / equipment interface.
 */
public enum HighlightStyle
{
	/** Coloured outline around the item sprite. */
	OUTLINE("Outline"),
	/** Rectangle drawn around the inventory slot. */
	BOX("Box"),
	/** Translucent fill of the inventory slot. */
	FILL("Fill"),
	/** Coloured bar under the inventory slot. */
	UNDERLINE("Underline");

	private final String label;

	HighlightStyle(String label)
	{
		this.label = label;
	}

	@Override
	public String toString()
	{
		return label;
	}
}
