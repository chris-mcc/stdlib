package com.peterphi.std.io;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PropertyFileTest
{
	@Test
	public void testReadOnlyUnionOfPropertyFile()
	{
		PropertyFile a = new PropertyFile();
		a.set("a.file", "a");
		a.set("override-me", "a");

		PropertyFile b = new PropertyFile();
		b.set("b.file", "b");
		b.set("override-me", "b");

		PropertyFile joined = PropertyFile.readOnlyUnion(a, b);

		assertEquals("a", joined.get("a.file"));
		assertEquals("b", joined.get("b.file"));
		assertEquals("b", joined.get("override-me"));
	}
}
