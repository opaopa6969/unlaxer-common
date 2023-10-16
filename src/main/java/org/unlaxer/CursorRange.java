package org.unlaxer;

import java.util.stream.IntStream;

public class CursorRange implements Comparable<CursorRange>{
	
	public final Cursor startIndexInclusive;
	public final Cursor endIndexExclusive;
	
	public CursorRange(Cursor startIndexInclusive, Cursor endIndexExclusive) {
		super();
		this.startIndexInclusive = startIndexInclusive;
		this.endIndexExclusive = endIndexExclusive;
	}
	public CursorRange(Cursor startIndexInclusive) {
		super();
		this.startIndexInclusive = startIndexInclusive;
		this.endIndexExclusive = startIndexInclusive;
	}
	public CursorRange() {
		super();
		this.startIndexInclusive = new CursorImpl();
		this.endIndexExclusive = new CursorImpl();
	}
	
	public final Cursor startIndexInclusive() {
		return startIndexInclusive;
	}
	
	public final Cursor endIndexExclusive() {
		return endIndexExclusive;
	}
	
	public boolean isSingle() {
		return startIndexInclusive.getPosition() == endIndexExclusive.getPosition();
	}

	public boolean match(int position){
		return position >=startIndexInclusive.getPosition() && position < endIndexExclusive.getPosition();
	}
	
	public boolean smallerThan(int position){
		return position >= endIndexExclusive.getPosition();
	}
	
	public boolean biggerThan(int position){
		return position < startIndexInclusive.getPosition();
	}
	
	public boolean smallerThan(CursorRange other){
		return other.startIndexInclusive.getPosition() >=  endIndexExclusive.getPosition();
	}
	
	public boolean biggerThan(CursorRange other){
		return other.endIndexExclusive.getPosition() <= startIndexInclusive.getPosition();
	}
	
	public RangesRelation relation(CursorRange other){
		int otherStart = other.startIndexInclusive.getPosition();
		int otherEnd= other.endIndexExclusive.getPosition();
		if(startIndexInclusive.getPosition() == otherStart && endIndexExclusive.getPosition() == otherEnd){
			
			return RangesRelation.equal;
			
		}else if(startIndexInclusive.getPosition() >= otherStart && endIndexExclusive.getPosition() <= otherEnd){
			
			return RangesRelation.outer;
			
		}else if(startIndexInclusive.getPosition() <= otherStart && endIndexExclusive.getPosition() >= otherEnd){
			
			return RangesRelation.inner;
			
		}else if(startIndexInclusive.getPosition() >= otherEnd || endIndexExclusive.getPosition() <= otherStart){
			
			return RangesRelation.notCrossed;
		}
		return RangesRelation.crossed;
	}
	
	public IntStream stream() {
		return IntStream.range(startIndexInclusive.getPosition(), endIndexExclusive.getPosition());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + endIndexExclusive.getPosition();
		result = prime * result + startIndexInclusive.getPosition();
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CursorRange other = (CursorRange) obj;
		if (endIndexExclusive.getPosition() != other.endIndexExclusive.getPosition())
			return false;
		if (startIndexInclusive.getPosition() != other.startIndexInclusive.getPosition())
			return false;
		return true;
	}
	
	
	@Override
	public int compareTo(CursorRange other) {
		int value = startIndexInclusive.getPosition() - other.startIndexInclusive.getPosition();
		if(value == 0) {
			return endIndexExclusive.getPosition() - other.endIndexExclusive.getPosition();
		}
		return value;
	}
	@Override
	public String toString() {
		return isSingle() ?
			"["+startIndexInclusive.toString()+"]":
			"["+startIndexInclusive.toString()+","+endIndexExclusive.toString()+"]";
				
	}
	public static CursorRange invalidRange() {
		return invalidRange;
	}
	
	static final CursorRange invalidRange = new CursorRange();
	
	public Range toRange() {
	   return new Range(startIndexInclusive.getPosition() , endIndexExclusive.getPosition());
	}
}