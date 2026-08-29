package org.unlaxer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.unlaxer.RangesRelation;

public class NameCacheIdentityTest {

	@Test
	public void ofStringReturnsSameInstanceForEqualString() {
		Name first = Name.of("foo");
		Name second = Name.of("foo");
		assertTrue("Name.of(String) must return cached identical instance", first == second);
		assertTrue(first.equals(second));
		assertEquals(first.hashCode(), second.hashCode());
	}

	@Test
	public void ofStringReturnsDistinctInstanceForDifferentString() {
		Name first = Name.of("foo");
		Name second = Name.of("bar");
		assertFalse(first == second);
		assertNotEquals(first, second);
		assertEquals("foo", first.getName());
		assertEquals("foo", first.getSimpleName());
	}

	@Test
	public void ofClassReturnsSameInstanceForEqualClass() {
		Name first = Name.of(String.class);
		Name second = Name.of(String.class);
		assertTrue("Name.of(Class) must return cached identical instance", first == second);
		assertEquals(String.class.getName(), first.getName());
		assertEquals(String.class.getSimpleName(), first.getSimpleName());
	}

	@Test
	public void ofClassAndStringReturnsSameInstanceForEqualArguments() {
		Name first = Name.of(String.class, "x");
		Name second = Name.of(String.class, "x");
		assertTrue("Name.of(Class, String) must return cached identical instance", first == second);
		assertEquals(String.class.getName() + "(x)", first.getName());
	}

	@Test
	public void ofEnumReturnsSameInstanceForEqualEnum() {
		Name first = Name.of(RangesRelation.crossed);
		Name second = Name.of(RangesRelation.crossed);
		assertTrue("Name.of(Enum) must return cached identical instance", first == second);
	}

	@Test
	public void ofClassAndStringDistinguishedFromBareOfClass() {
		Name bare = Name.of(String.class);
		Name composite = Name.of(String.class, "x");
		assertNotEquals(bare, composite);
		assertNotEquals(bare.getName(), composite.getName());
	}
}
