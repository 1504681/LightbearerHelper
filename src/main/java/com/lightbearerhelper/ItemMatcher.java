package com.lightbearerhelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.runelite.client.util.WildcardMatcher;

// one pattern per line or comma separated, * wildcards, case insensitive, # comments
public final class ItemMatcher
{
	private final List<String> patterns;

	public ItemMatcher(String listText)
	{
		this.patterns = Collections.unmodifiableList(parse(listText));
	}

	public static List<String> parse(String listText)
	{
		List<String> result = new ArrayList<>();
		if (listText == null)
		{
			return result;
		}
		for (String raw : listText.split("[\\r\\n,]+"))
		{
			String line = raw.trim();
			if (line.isEmpty() || line.startsWith("#"))
			{
				continue;
			}
			result.add(line.toLowerCase());
		}
		return result;
	}

	public List<String> getPatterns()
	{
		return patterns;
	}

	public boolean isEmpty()
	{
		return patterns.isEmpty();
	}

	public boolean matches(String itemName)
	{
		if (itemName == null || itemName.isEmpty() || patterns.isEmpty())
		{
			return false;
		}
		String name = itemName.toLowerCase();
		for (String pattern : patterns)
		{
			if (WildcardMatcher.matches(pattern, name))
			{
				return true;
			}
		}
		return false;
	}
}
