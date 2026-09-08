package com.lightbearerhelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import org.junit.Test;

public class ItemMatcherTest
{
	@Test
	public void parsesLinesCommasBlanksAndComments()
	{
		ItemMatcher matcher = new ItemMatcher("Ultor ring\n\n# comment\n  Magus ring , Venator ring\r\n");
		assertEquals(Arrays.asList("ultor ring", "magus ring", "venator ring"), matcher.getPatterns());
	}

	@Test
	public void exactMatchIsCaseInsensitive()
	{
		ItemMatcher matcher = new ItemMatcher("Ultor ring");
		assertTrue(matcher.matches("Ultor ring"));
		assertTrue(matcher.matches("ULTOR RING"));
		assertFalse(matcher.matches("Ultor ring (i)"));
		assertFalse(matcher.matches("Ring"));
	}

	@Test
	public void wildcards()
	{
		ItemMatcher matcher = new ItemMatcher("Berserker ring*\n*dagger*\n*(i)");
		assertTrue(matcher.matches("Berserker ring"));
		assertTrue(matcher.matches("Berserker ring (i)"));
		assertTrue(matcher.matches("Dragon dagger(p++)"));
		assertTrue(matcher.matches("Abyssal dagger"));
		assertTrue(matcher.matches("Ring of suffering (i)"));
		assertFalse(matcher.matches("Dragon claws"));
	}

	@Test
	public void emptyAndNullNeverMatch()
	{
		assertTrue(new ItemMatcher("").isEmpty());
		assertTrue(new ItemMatcher(null).isEmpty());
		assertFalse(new ItemMatcher("").matches("Ultor ring"));
		assertFalse(new ItemMatcher("Ultor ring").matches(null));
		assertFalse(new ItemMatcher("Ultor ring").matches(""));
	}

	@Test
	public void defaultListsContainExpectedItems()
	{
		ItemMatcher rings = new ItemMatcher(LightbearerHelperConfig.DEFAULT_RINGS);
		assertTrue(rings.matches("Ultor ring"));
		assertTrue(rings.matches("Bellator ring"));
		assertTrue(rings.matches("Magus ring"));
		assertTrue(rings.matches("Venator ring"));
		assertTrue(rings.matches("Berserker ring (i)"));
		assertTrue(rings.matches("Ring of suffering (ri)"));
		assertFalse(rings.matches("Lightbearer"));

		ItemMatcher spec = new ItemMatcher(LightbearerHelperConfig.DEFAULT_SPEC_ITEMS);
		assertTrue(spec.matches("Dragon claws"));
		assertTrue(spec.matches("Dragon dagger(p++)"));
		assertTrue(spec.matches("Toxic blowpipe (empty)"));
		assertFalse(spec.matches("Abyssal whip"));
	}
}
