package org.unlaxer.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.unlaxer.util.Slicer;

public class SlicerTest {

	@Test
	public void test() {
		
		{
			Slicer slicer = new Slicer("abcba");
			assertEquals("abcba", slicer.step(1).get());
		}
		{
			Slicer slicer = new Slicer("play");
			assertEquals("yalp", slicer.step(-1).get());
		}
		{
			Slicer slicer = new Slicer("abcba");
			assertEquals("bcba", slicer.begin(1).get());
		}
		{
			Slicer slicer = new Slicer("abcba");
			assertEquals("aca", slicer.step(2).get());
		}
		{
			Slicer slicer = new Slicer("abcba");
			assertEquals("b", slicer.begin(1).end(2).get());
		}
		{
			Slicer slicer = new Slicer("play");
			assertEquals("y", slicer.begin(-1).get());
		}
		{
			Slicer slicer = new Slicer("play");
			assertEquals("ay", slicer.begin(-2).get());
		}
		{
			Slicer slicer = new Slicer("play");
			assertEquals("la", slicer.begin(-3).end(-1).get());
		}
	}
}
