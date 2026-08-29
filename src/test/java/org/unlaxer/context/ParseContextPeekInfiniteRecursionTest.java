package org.unlaxer.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.unlaxer.CodePointIndex;
import org.unlaxer.Source;
import org.unlaxer.StringSource;

public class ParseContextPeekInfiniteRecursionTest {

	@Test(expected = StackOverflowError.class)
	public void peekWithTwoCodePointIndexOverflows() {
		StringSource source = StringSource.createRootSource("abc");
		ParseContext parseContext = new ParseContext(source);

		parseContext.peek(new CodePointIndex(0), new CodePointIndex(2));
	}

	@Test
	public void peekWithCodePointIndexAndLengthDelegatesCorrectly() {
		StringSource source = StringSource.createRootSource("abc");
		ParseContext parseContext = new ParseContext(source);

		Source peeked = parseContext.peek(new CodePointIndex(0), new org.unlaxer.CodePointLength(2));

		assertEquals("ab", peeked.sourceAsString());
	}

	@Test
	public void peekLastDelegatesCorrectly() {
		StringSource source = StringSource.createRootSource("abc");
		ParseContext parseContext = new ParseContext(source);

		Source peeked = parseContext.peekLast(new CodePointIndex(2), new org.unlaxer.CodePointLength(1));

		assertEquals("b", peeked.sourceAsString());
	}

	@Test
	public void documentationOfCurrentBehavior() {
		try {
			StringSource source = StringSource.createRootSource("abc");
			ParseContext parseContext = new ParseContext(source);

			parseContext.peek(new CodePointIndex(0), new CodePointIndex(2));

			fail("expected StackOverflowError due to self-recursion in ParseContext.peek(CodePointIndex, CodePointIndex)");
		} catch (StackOverflowError expected) {
			return;
		}
	}
}
