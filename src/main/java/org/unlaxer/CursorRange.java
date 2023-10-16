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

	public boolean match(Index position){
		return position.value >=startIndexInclusive.getPosition().value 
		    && position.value < endIndexExclusive.getPosition().value;
	}
	
	public boolean smallerThan(Index position){
		return position.value >= endIndexExclusive.getPosition().value;
	}
	
	public boolean biggerThan(Index position){
		return position.value < startIndexInclusive.getPosition().value;
	}
	
	public boolean smallerThan(CursorRange other){
		return other.startIndexInclusive.getPosition().value >=  endIndexExclusive.getPosition().value;
	}
	
	public boolean biggerThan(CursorRange other){
		return other.endIndexExclusive.getPosition().value <= startIndexInclusive.getPosition().value;
	}
	
	public RangesRelation relation(CursorRange other){
		int otherStart = other.startIndexInclusive.getPosition().value;
		int otherEnd= other.endIndexExclusive.getPosition().value;
		if(startIndexInclusive.getPosition().value == otherStart && endIndexExclusive.getPosition().value == otherEnd){
			
			return RangesRelation.equal;
			
		}else if(startIndexInclusive.getPosition().value >= otherStart && endIndexExclusive.getPosition().value <= otherEnd){
			
			return RangesRelation.outer;
			
		}else if(startIndexInclusive.getPosition().value <= otherStart && endIndexExclusive.getPosition().value >= otherEnd){
			
			return RangesRelation.inner;
			
		}else if(startIndexInclusive.getPosition().value >= otherEnd || endIndexExclusive.getPosition().value  <= otherStart){
			
			return RangesRelation.notCrossed;
		}
		return RangesRelation.crossed;
	}
	
	public IntStream stream() {
		return IntStream.range(startIndexInclusive.getPosition().value, endIndexExclusive.getPosition().value);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + endIndexExclusive.getPosition().value;
		result = prime * result + startIndexInclusive.getPosition().value;
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
		if (endIndexExclusive.getPosition().value != other.endIndexExclusive.getPosition().value)
			return false;
		if (startIndexInclusive.getPosition().value != other.startIndexInclusive.getPosition().value)
			return false;
		return true;
	}
	
	
	@Override
	public int compareTo(CursorRange other) {
		int value = startIndexInclusive.getPosition().value  - other.startIndexInclusive.getPosition().value;
		if(value == 0) {
			return endIndexExclusive.getPosition().value - other.endIndexExclusive.getPosition().value;
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