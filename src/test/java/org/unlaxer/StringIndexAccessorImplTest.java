package org.unlaxer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringIndexAccessorImplTest {

	@Test
	public void indexOfStringFindsSubstring() {
		StringIndexAccessorImpl accessor = new StringIndexAccessorImpl("hello world");
		assertEquals(6, accessor.indexOf("world", 0));
	}

	@Test
	public void indexOfStringFindsSingleCharSubsequentOccurrence() {
		StringIndexAccessorImpl accessor = new StringIndexAccessorImpl("hello world");
		assertEquals(7, accessor.indexOf("o", 5));
	}

	@Test
	public void indexOfStringReturnsMinusOneWhenNotFound() {
		StringIndexAccessorImpl accessor = new StringIndexAccessorImpl("hello world");
		assertEquals(-1, accessor.indexOf("xyz", 0));
	}

	@Test
	public void lastIndexOfStringFindsSubstring() {
		StringIndexAccessorImpl accessor = new StringIndexAccessorImpl("hello world");
		assertEquals(6, accessor.lastIndexOf("world", 10));
	}

	@Test
	public void lastIndexOfStringFindsLastOccurrence() {
		StringIndexAccessorImpl accessor = new StringIndexAccessorImpl("hello world hello");
		assertEquals(12, accessor.lastIndexOf("hello", 17));
	}

	@Test
	public void lastIndexOfStringReturnsMinusOneWhenNotFound() {
		StringIndexAccessorImpl accessor = new StringIndexAccessorImpl("hello world");
		assertEquals(-1, accessor.lastIndexOf("xyz", 10));
	}

	@Test
	public void indexOfStringMatchesJavaStringSemantics() {
		String source = "abcabcabc";
		StringIndexAccessorImpl accessor = new StringIndexAccessorImpl(source);
		assertEquals(source.indexOf("abc", 1), accessor.indexOf("abc", 1));
		assertEquals(source.indexOf("abc", 4), accessor.indexOf("abc", 4));
		assertEquals(source.indexOf("abc", 8), accessor.indexOf("abc", 8));
	}

	@Test
	public void lastIndexOfStringMatchesJavaStringSemantics() {
		String source = "abcabcabc";
		StringIndexAccessorImpl accessor = new StringIndexAccessorImpl(source);
		assertEquals(source.lastIndexOf("abc", 8), accessor.lastIndexOf("abc", 8));
		assertEquals(source.lastIndexOf("abc", 5), accessor.lastIndexOf("abc", 5));
		assertEquals(source.lastIndexOf("abc", 2), accessor.lastIndexOf("abc", 2));
	}
}
