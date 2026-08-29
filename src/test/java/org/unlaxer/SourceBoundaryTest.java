package org.unlaxer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class SourceBoundaryTest {

	@Test
	public void emptyRootSourceIsNotPresentAndIsEmpty() {
		StringSource empty = StringSource.createRootSource("");
		assertFalse(empty.isPresent());
		assertTrue(empty.isEmpty());
		assertEquals(0, empty.codePointLength().value());
	}

	@Test
	public void zeroLengthSubSourceIsEmptyAndNotPresent() {
		StringSource root = StringSource.createRootSource("abc");
		Source zero = root.subSource(new CodePointIndex(0), new CodePointIndex(0));
		assertEquals("", zero.sourceAsString());
		assertFalse(zero.isPresent());
		assertTrue(zero.isEmpty());
		assertEquals(0, zero.codePointLength().value());
	}

	@Test
	public void subSourceEqualStartAndEndIsZeroLength() {
		StringSource root = StringSource.createRootSource("abc");
		Source zeroAtEnd = root.subSource(new CodePointIndex(3), new CodePointIndex(3));
		assertEquals("", zeroAtEnd.sourceAsString());
		assertEquals(0, zeroAtEnd.codePointLength().value());
	}

	@Test
	public void subSourceSpansSingleSurrogatePairAsOneCodePoint() {
		String text = "a𪛊b";
		StringSource root = StringSource.createRootSource(text);
		assertEquals(3, root.codePointLength().value());

		Source surrogate = root.subSource(new CodePointIndex(1), new CodePointIndex(2));
		assertEquals("𪛊", surrogate.sourceAsString());
		assertEquals(1, surrogate.codePointLength().value());
		assertTrue(surrogate.isPresent());
	}

	@Test
	public void peekAtSurrogatePairBoundaryReturnsOneCodePoint() {
		StringSource root = StringSource.createRootSource("a𪛊b");
		Source peeked = root.peek(new CodePointIndex(1), new CodePointLength(1));
		assertEquals("𪛊", peeked.sourceAsString());
		assertEquals(1, peeked.codePointLength().value());
	}

	@Test
	public void linesAsSourcePreservesSurrogatePairOnFirstLine() {
		StringSource root = StringSource.createRootSource("a𪛊\nb");
		List<Source> lines = root.linesAsSource().collect(Collectors.toList());
		assertEquals(2, lines.size());
		assertTrue(lines.get(0).sourceAsString().startsWith("a𪛊"));
		assertEquals("b", lines.get(1).sourceAsString());
	}
}
