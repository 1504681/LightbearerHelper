package com.lightbearerhelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.runelite.client.util.WildcardMatcher;

// one pattern per line or comma separated, * wildcards, case insensitive, # comments.
// a line may end in a spec cost, "Dragon claws 50%", which overrides the built-in table
public final class ItemMatcher
{
	private static final Pattern COST_SUFFIX = Pattern.compile("^(.*\\S)\\s+(\\d{1,3})%$");

	private final List<String> patterns;
	private final List<Integer> costs;

	public ItemMatcher(String listText)
	{
		List<String> parsedPatterns = new ArrayList<>();
		List<Integer> parsedCosts = new ArrayList<>();
		for (String line : parse(listText))
		{
			Matcher m = COST_SUFFIX.matcher(line);
			if (m.matches())
			{
				parsedPatterns.add(m.group(1));
				parsedCosts.add(Integer.parseInt(m.group(2)));
			}
			else
			{
				parsedPatterns.add(line);
				parsedCosts.add(null);
			}
		}
		this.patterns = Collections.unmodifiableList(parsedPatterns);
		this.costs = Collections.unmodifiableList(parsedCosts);
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
		return indexOf(itemName) >= 0;
	}

	// the spec cost given on the matching line, null when the line has none or nothing matches
	public Integer costOf(String itemName)
	{
		int index = indexOf(itemName);
		return index < 0 ? null : costs.get(index);
	}

	private int indexOf(String itemName)
	{
		if (itemName == null || itemName.isEmpty() || patterns.isEmpty())
		{
			return -1;
		}
		String name = itemName.toLowerCase();
		for (int i = 0; i < patterns.size(); i++)
		{
			if (WildcardMatcher.matches(patterns.get(i), name))
			{
				return i;
			}
		}
		return -1;
	}
}
