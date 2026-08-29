package org.unlaxer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SourcePeekBoundaryTest {

	@Test
	public void peekAtExactlyEndIndexWithZeroLengthReturnsEmpty() {
		StringSource root = StringSource.createRootSource("abc");

		Source peeked = root.peek(new CodePointIndex(3), new CodePointLength(0));

		assertEquals("", peeked.sourceAsString());
		assertEquals(0, peeked.codePointLength().value());
	}

	@Test(expected = StringIndexOutOfBoundsException.class)
	public void documentsBugPeekBeyondEndWithZeroLengthThrows() {
		StringSource root = StringSource.createRootSource("abc");

		root.peek(new CodePointIndex(10), new CodePointLength(0));
	}

	@Test(expected = StringIndexOutOfBoundsException.class)
	public void documentsBugPeekBeyondEndWithPositiveLengthThrows() {
		StringSource root = StringSource.createRootSource("abc");

		root.peek(new CodePointIndex(10), new CodePointLength(5));
	}

	@Test
	public void peekStartingAtEndReturnsEmptyForPositiveLength() {
		StringSource root = StringSource.createRootSource("abc");

		Source peeked = root.peek(new CodePointIndex(3), new CodePointLength(2));

		assertEquals("", peeked.sourceAsString());
	}

	@Test
	public void peekLengthZeroAtStartReturnsEmpty() {
		StringSource root = StringSource.createRootSource("abc");

		Source peeked = root.peek(new CodePointIndex(0), new CodePointLength(0));

		assertEquals("", peeked.sourceAsString());
		assertEquals(0, peeked.codePointLength().value());
	}

	@Test
	public void peekLastReturnsLastNCodePoints() {
		StringSource root = StringSource.createRootSource("abcdef");

		Source peeked = root.peekLast(new CodePointIndex(3), new CodePointLength(3));

		assertEquals("abc", peeked.sourceAsString());
	}

	@Test
	public void peekLastWithZeroLengthReturnsEmpty() {
		StringSource root = StringSource.createRootSource("abc");

		Source peeked = root.peekLast(new CodePointIndex(2), new CodePointLength(0));

		assertEquals("", peeked.sourceAsString());
	}

	@Test
	public void peekLastClampsNegativeStartToZero() {
		StringSource root = StringSource.createRootSource("abc");

		Source peeked = root.peekLast(new CodePointIndex(1), new CodePointLength(5));

		assertEquals("a", peeked.sourceAsString());
	}

	@Test
	public void peekAcrossSurrogatePairBoundary() {
		StringSource root = StringSource.createRootSource("a𪛊b");

		Source peeked = root.peek(new CodePointIndex(2), new CodePointLength(1));

		assertEquals("b", peeked.sourceAsString());
	}

	@Test
	public void peekEndingExactlyAtSurrogatePair() {
		StringSource root = StringSource.createRootSource("a𪛊b");

		Source peeked = root.peek(new CodePointIndex(0), new CodePointLength(2));

		assertEquals("a𪛊", peeked.sourceAsString());
		assertEquals(2, peeked.codePointLength().value());
	}

	@Test
	public void peekLastReturnsOneCodePointForSurrogatePair() {
		StringSource root = StringSource.createRootSource("a𪛊b");

		Source peeked = root.peekLast(new CodePointIndex(2), new CodePointLength(2));

		assertEquals("a𪛊", peeked.sourceAsString());
		assertTrue(peeked.isPresent());
	}

	@Test
	public void peekEmptySourceAtStartReturnsEmpty() {
		StringSource root = StringSource.createRootSource("");

		assertEquals("", root.peek(new CodePointIndex(0), new CodePointLength(0)).sourceAsString());
	}
}
