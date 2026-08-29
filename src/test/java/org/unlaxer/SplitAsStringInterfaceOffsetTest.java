package org.unlaxer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SplitAsStringInterfaceOffsetTest {

	@Test
	public void splitPreservesContentOrder() {
		Source root = StringSource.createRootSource("AA-BB-CC");

		Source[] parts = root.splitAsStringInterface("-");

		assertEquals(3, parts.length);
		assertEquals("AA", parts[0].sourceAsString());
		assertEquals("BB", parts[1].sourceAsString());
		assertEquals("CC", parts[2].sourceAsString());
	}

	@Test
	public void splitAssignsMonotonicallyIncreasingOffsets() {
		Source root = StringSource.createRootSource("AA-BB-CC");

		Source[] parts = root.splitAsStringInterface("-");

		assertEquals(3, parts.length);

		CodePointOffset offset0 = parts[0].offsetFromRoot();
		CodePointOffset offset1 = parts[1].offsetFromRoot();
		CodePointOffset offset2 = parts[2].offsetFromRoot();

		assertEquals(new CodePointOffset(0), offset0);
		assertEquals(new CodePointOffset(3), offset1);
		assertEquals(new CodePointOffset(6), offset2);
	}

	@Test
	public void splitWithRegexThatSpansMultipleChars() {
		Source root = StringSource.createRootSource("AA--BB--CC");

		Source[] parts = root.splitAsStringInterface("--");

		assertEquals(3, parts.length);
		assertEquals("AA", parts[0].sourceAsString());
		assertEquals("BB", parts[1].sourceAsString());
		assertEquals("CC", parts[2].sourceAsString());

		assertEquals(new CodePointOffset(0), parts[0].offsetFromRoot());
		assertEquals(new CodePointOffset(4), parts[1].offsetFromRoot());
		assertEquals(new CodePointOffset(8), parts[2].offsetFromRoot());
	}

	@Test
	public void splitWithLeadingDelimiter() {
		Source root = StringSource.createRootSource("-AA-BB");

		Source[] parts = root.splitAsStringInterface("-");

		assertEquals(3, parts.length);
		assertEquals("", parts[0].sourceAsString());
		assertEquals("AA", parts[1].sourceAsString());
		assertEquals("BB", parts[2].sourceAsString());

		assertEquals(new CodePointOffset(0), parts[0].offsetFromRoot());
		assertEquals(new CodePointOffset(1), parts[1].offsetFromRoot());
		assertEquals(new CodePointOffset(4), parts[2].offsetFromRoot());
	}

	@Test
	public void splitWithTrailingDelimiterDropsTrailingEmpty() {
		Source root = StringSource.createRootSource("AA-BB-");

		Source[] parts = root.splitAsStringInterface("-");

		assertEquals(2, parts.length);
		assertEquals("AA", parts[0].sourceAsString());
		assertEquals("BB", parts[1].sourceAsString());
	}

	@Test(expected = NullPointerException.class)
	public void documentsBugSplitOnEmptyInputThrowsNullPointerException() {
		Source root = StringSource.createRootSource("");

		root.splitAsStringInterface("-");
	}

	@Test
	public void splitPreservesSurrogatePairBoundaries() {
		Source root = StringSource.createRootSource("😀-😂");

		Source[] parts = root.splitAsStringInterface("-");

		assertEquals(2, parts.length);
		assertEquals("😀", parts[0].sourceAsString());
		assertEquals("😂", parts[1].sourceAsString());

		assertEquals(new CodePointOffset(0), parts[0].offsetFromRoot());
		assertEquals(new CodePointOffset(2), parts[1].offsetFromRoot());
	}
}
