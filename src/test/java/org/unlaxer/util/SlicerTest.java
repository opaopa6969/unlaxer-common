package org.unlaxer.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.unlaxer.CodePointIndex;

public class SlicerTest {

	@Test
	public void test() {
		
		{
			Slicer slicer = new Slicer("abcba");
			assertEquals("abcba", slicer.step(1).get().toString());
		}
		{
			Slicer slicer = new Slicer("play");
			assertEquals("yalp", slicer.step(-1).get().toString());
		}
		{
			Slicer slicer = new Slicer("abcba");
			assertEquals("bcba", slicer.begin(new CodePointIndex(1)).get().toString());
		}
		{
			Slicer slicer = new Slicer("abcba");
			assertEquals("aca", slicer.step(2).get().toString());
		}
		{
			Slicer slicer = new Slicer("abcba");
			assertEquals("b", slicer.begin(new CodePointIndex(1)).end(new CodePointIndex(2)).get().toString());
		}
		{
			Slicer slicer = new Slicer("play");
			assertEquals("y", slicer.begin(new CodePointIndex(-1)).get().toString());
		}
		{
			Slicer slicer = new Slicer("play");
			assertEquals("ay", slicer.begin(new CodePointIndex(-2)).get().toString());
		}
		{
			Slicer slicer = new Slicer("play");
			assertEquals("la", slicer.begin(new CodePointIndex(-3)).end(new CodePointIndex(-1)).get().toString());
		}
	}
}
