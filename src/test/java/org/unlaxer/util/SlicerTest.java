package org.unlaxer.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.unlaxer.CodePointIndex;
import org.unlaxer.CodePointOffset;

public class SlicerTest {

	@Test
	public void test() {
	  
	  CodePointOffset codePointOffset = new CodePointOffset(0);
		
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
			assertEquals("bcba", slicer.begin(new CodePointIndex(1,codePointOffset)).get().toString());
		}
		{
			Slicer slicer = Slicer.of("abcba");
			assertEquals("aca", slicer.step(2).get().toString());
		}
		{
			Slicer slicer = Slicer.of("abcba");
			assertEquals("b", slicer.begin(new CodePointIndex(1,codePointOffset)).end(new CodePointIndex(2,codePointOffset)).get().toString());
		}
		{
			Slicer slicer = Slicer.of("play");
			assertEquals("y", slicer.begin(new CodePointIndex(-1,codePointOffset)).get().toString());
		}
		{
			Slicer slicer = Slicer.of("play");
			assertEquals("ay", slicer.begin(new CodePointIndex(-2,codePointOffset)).get().toString());
		}
		{
			Slicer slicer = Slicer.of("play");
			assertEquals("la", slicer.begin(new CodePointIndex(-3,codePointOffset)).end(new CodePointIndex(-1,codePointOffset)).get().toString());
		}
	}
}
