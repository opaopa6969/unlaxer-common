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

	public boolean match(CodePointIndex position){
		return position.ge(startIndexInclusive.getPosition()) 
		    && position.lt(endIndexExclusive.getPosition());
	}
	
	public boolean smallerThan(CodePointIndex position){
		return position.ge(endIndexExclusive.getPosition());
	}
	
	public boolean biggerThan(CodePointIndex position){
		return position.lt(startIndexInclusive.getPosition());
	}
	
	public boolean smallerThan(CursorRange other){
		return other.startIndexInclusive.getPosition().ge(endIndexExclusive.getPosition());
	}
	
	public boolean biggerThan(CursorRange other){
		return other.endIndexExclusive.getPosition().le(startIndexInclusive.getPosition());
	}
	
	public RangesRelation relation(CursorRange other){
		CodePointIndex otherStart = other.startIndexInclusive.getPosition();
		CodePointIndex otherEnd= other.endIndexExclusive.getPosition();
		if(startIndexInclusive.getPosition().eq(otherStart) && 
		    endIndexExclusive.getPosition().eq(otherEnd)){
			
			return RangesRelation.equal;
			
		}else if(startIndexInclusive.getPosition().ge(otherStart) && 
		    endIndexExclusive.getPosition().le(otherEnd)){
			
			return RangesRelation.outer;
			
		}else if(startIndexInclusive.getPosition().le(otherStart) && 
		    endIndexExclusive.getPosition().ge(otherEnd)){
			
			return RangesRelation.inner;
			
		}else if(startIndexInclusive.getPosition().ge(otherEnd) || 
		    endIndexExclusive.getPosition().le(otherStart)){
			
			return RangesRelation.notCrossed;
		}
		return RangesRelation.crossed;
	}
	
	public IntStream asIntStream() {
		return IntStream.range(startIndexInclusive.getPosition().value(), 
		    endIndexExclusive.getPosition().value());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + endIndexExclusive.getPosition().value();
		result = prime * result + startIndexInclusive.getPosition().value();
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
		if (endIndexExclusive.getPosition().value() != other.endIndexExclusive.getPosition().value())
			return false;
		if (startIndexInclusive.getPosition().value() != other.startIndexInclusive.getPosition().value())
			return false;
		return true;
	}
	
	
	@Override
	public int compareTo(CursorRange other) {
		int value = startIndexInclusive.getPosition().value() - other.startIndexInclusive.getPosition().value();
		if(value == 0) {
			return endIndexExclusive.getPosition().value() - other.endIndexExclusive.getPosition().value();
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
	
//	public CursorRange plus(CodePointOffset codePointOffset) {
//	  lineNumerの更新も行う
//	  position in lineはそもそもsubシーケンスでの扱いをどうするか？subシーケンスの中で全体的に新たなものになるか。
//	  rootは良いとしてもparentの中での扱いはどうするか？
//	}
}