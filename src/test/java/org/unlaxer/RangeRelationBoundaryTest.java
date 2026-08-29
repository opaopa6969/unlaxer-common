package org.unlaxer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.TreeSet;

import org.junit.Test;

public class RangeRelationBoundaryTest {

	@Test
	public void relationAllBranches() {
		Range target = new Range(1, 3);
		assertEquals(RangesRelation.equal, target.relation(new Range(1, 3)));
		assertEquals(RangesRelation.inner, target.relation(new Range(2, 3)));
		assertEquals(RangesRelation.outer, target.relation(new Range(0, 5)));
		assertEquals(RangesRelation.crossed, target.relation(new Range(2, 5)));
		assertEquals(RangesRelation.notCrossed, target.relation(new Range(0, 1)));
		assertEquals(RangesRelation.notCrossed, target.relation(new Range(3, 5)));
	}

	@Test
	public void relationIsAsymmetricBetweenInnerAndOuter() {
		// implementation naming: "outer" means `this` is contained by other,
		// "inner" means `this` contains other (counter-intuitive but pinned here).
		Range contained = new Range(1, 3);
		Range containing = new Range(0, 5);
		assertEquals(RangesRelation.outer, contained.relation(containing));
		assertEquals(RangesRelation.inner, containing.relation(contained));
	}

	@Test
	public void matchBoundary() {
		Range range = new Range(1, 3);
		assertFalse(range.match(0));
		assertTrue(range.match(1));
		assertTrue(range.match(2));
		assertFalse(range.match(3));
	}

	@Test
	public void equalsAndHashCode() {
		Range first = new Range(1, 3);
		Range second = new Range(1, 3);
		Range third = new Range(1, 2);
		assertTrue(first.equals(second));
		assertEquals(first.hashCode(), second.hashCode());
		assertFalse(first.equals(third));
	}

	@Test
	public void compareToOrdersByStartThenEndAndDeduplicates() {
		TreeSet<Range> ranges = new TreeSet<Range>();
		ranges.add(new Range(5, 7));
		ranges.add(new Range(1, 3));
		ranges.add(new Range(1, 2));
		ranges.add(new Range(1, 3));
		Iterator<Range> iterator = ranges.iterator();
		assertEquals(new Range(1, 2), iterator.next());
		assertEquals(new Range(1, 3), iterator.next());
		assertEquals(new Range(5, 7), iterator.next());
		assertFalse(iterator.hasNext());
		assertEquals(3, ranges.size());
	}
}
