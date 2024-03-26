package org.unlaxer.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.unlaxer.CodePointIndex;
import org.unlaxer.Source;

public class SlicerTest {

	@Test
	public void test() {
		
		{
			Slicer slicer = Slicer.of("abcba");
			assertEquals("abcba", slicer.step(1).get().toString());
		}
		{
			Slicer slicer = Slicer.of("play");
			assertEquals("yalp", slicer.step(-1).get().toString());
		}
		{
			Slicer slicer = Slicer.of("abcba");
			Source source = slicer.get();
			assertEquals("bcba", slicer.begin(new CodePointIndex(1,source)).get().toString());
		}
		{
			Slicer slicer = Slicer.of("abcba");
			assertEquals("aca", slicer.step(2).get().toString());
		}
		{
			Slicer slicer = Slicer.of("abcba");
			Source source = slicer.get();
			assertEquals("b", slicer.begin(new CodePointIndex(1,source)).end(new CodePointIndex(2,source)).get().toString());
		}
		{
			Slicer slicer = Slicer.of("play");
			Source source = slicer.get();
			assertEquals("y", slicer.begin(new CodePointIndex(-1,source)).get().toString());
		}
		{
			Slicer slicer = Slicer.of("play");
			Source source = slicer.get();
			assertEquals("ay", slicer.begin(new CodePointIndex(-2,source)).get().toString());
		}
		{
			Slicer slicer = Slicer.of("play");
			Source source = slicer.get();
			assertEquals("la", slicer.begin(new CodePointIndex(-3,source)).end(new CodePointIndex(-1,source)).get().toString());
		}
	}
}
