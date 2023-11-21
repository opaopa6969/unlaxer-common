package org.unlaxer;

import java.util.stream.IntStream;

import org.unlaxer.Cursor.EndExclusiveCursor;
import org.unlaxer.Cursor.StartInclusiveCursor;
import org.unlaxer.Source.SourceKind;

public class CursorRange implements Comparable<CursorRange>{
	
	public final StartInclusiveCursor startIndexInclusive;
	public final EndExclusiveCursor endIndexExclusive;
	
	public CursorRange(StartInclusiveCursor startIndexInclusive, EndExclusiveCursor endIndexExclusive) {
		super();
		this.startIndexInclusive = startIndexInclusive;
		this.endIndexExclusive = endIndexExclusive;
	}
	public CursorRange(StartInclusiveCursor startIndexInclusive) {
		super();
		this.startIndexInclusive = startIndexInclusive;
		this.endIndexExclusive = new EndExclusiveCursorImpl(startIndexInclusive);
	}
	
	public CursorRange(SourceKind sourceKind, PositionResolver positionResolver) {
		super();
		this.startIndexInclusive = new StartInclusiveCursorImpl(sourceKind , positionResolver);
		this.endIndexExclusive = new EndExclusiveCursorImpl(sourceKind , positionResolver);
	}
	
	public static CursorRange of(
	    CodePointIndex startIndexInclusive,
      CodePointIndex endIndexExclusive,
      CodePointOffset offsetFromRoot,
      SourceKind sourceKind,
      PositionResolver positionResolver
	    ) {
	  return new CursorRange(
        new StartInclusiveCursorImpl(sourceKind,positionResolver,startIndexInclusive,offsetFromRoot),
        new EndExclusiveCursorImpl(sourceKind,positionResolver,endIndexExclusive,offsetFromRoot)
	  );
	  
	}
	
	public final StartInclusiveCursor startIndexInclusive() {
		return startIndexInclusive;
	}
	
	public final EndExclusiveCursor endIndexExclusive() {
		return endIndexExclusive;
	}
	
	public boolean isSingle() {
		return startIndexInclusive.position() == endIndexExclusive.position();
	}

	public boolean match(CodePointIndex position){
		return position.ge(startIndexInclusive.position()) 
		    && position.lt(endIndexExclusive.position());
	}

	
  public boolean lt(CodePointIndex position){
	  return position.ge(endIndexExclusive.position());
	}

	public boolean lessThan(CodePointIndex position){
		return position.ge(endIndexExclusive.position());
	}
	
	public boolean graterThan(CodePointIndex position){
		return position.lt(startIndexInclusive.position());
	}
	
  public boolean gt(CodePointIndex position){
    return position.lt(startIndexInclusive.position());
	}
	
	public boolean lessThan(CursorRange other){
		return other.startIndexInclusive.position().ge(endIndexExclusive.position());
	}
	
  public boolean lt(CursorRange other){
	  return other.startIndexInclusive.position().ge(endIndexExclusive.position());
	}
	
  public boolean gt(CursorRange other){
    return other.endIndexExclusive.position().le(startIndexInclusive.position());
  }
  
	public boolean graterThan(CursorRange other){
		return other.endIndexExclusive.position().le(startIndexInclusive.position());
	}
	
	public RangesRelation relation(CursorRange other){
		CodePointIndex otherStart = other.startIndexInclusive.position();
		CodePointIndex otherEnd= other.endIndexExclusive.position();
		if(startIndexInclusive.position().eq(otherStart) && 
		    endIndexExclusive.position().eq(otherEnd)){
			
			return RangesRelation.equal;
			
		}else if(startIndexInclusive.position().ge(otherStart) && 
		    endIndexExclusive.position().le(otherEnd)){
			
			return RangesRelation.outer;
			
		}else if(startIndexInclusive.position().le(otherStart) && 
		    endIndexExclusive.position().ge(otherEnd)){
			
			return RangesRelation.inner;
			
		}else if(startIndexInclusive.position().ge(otherEnd) || 
		    endIndexExclusive.position().le(otherStart)){
			
			return RangesRelation.notCrossed;
		}
		return RangesRelation.crossed;
	}
	
	public IntStream asIntStream() {
		return IntStream.range(startIndexInclusive.position().value(), 
		    endIndexExclusive.position().value());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + endIndexExclusive.position().value();
		result = prime * result + startIndexInclusive.position().value();
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
		if (endIndexExclusive.position().value() != other.endIndexExclusive.position().value())
			return false;
		if (startIndexInclusive.position().value() != other.startIndexInclusive.position().value())
			return false;
		return true;
	}
	
	
	@Override
	public int compareTo(CursorRange other) {
		int value = startIndexInclusive.position().value() - other.startIndexInclusive.position().value();
		if(value == 0) {
			return endIndexExclusive.position().value() - other.endIndexExclusive.position().value();
		}
		return value;
	}
	@Override
	public String toString() {
		return isSingle() ?
			"["+startIndexInclusive.toString()+"]":
			"["+startIndexInclusive.toString()+","+endIndexExclusive.toString()+"]";
				
	}
	public static CursorRange invalidRange(SourceKind sourceKind,PositionResolver positionResolver) {
	  return new CursorRange(sourceKind,positionResolver);
	}
	
//	static final CursorRange invalidRange = new CursorRange();
	
	public Range toRange() {
	   return new Range(startIndexInclusive.position() , endIndexExclusive.position());
	}
	
	public CursorRange newWithAdd(CodePointOffset codePointOffset) {
	  return new CursorRange(
  	  startIndexInclusive.newWithAddPosition(codePointOffset),
  	  endIndexExclusive.newWithAddPosition(codePointOffset)
 	  );
	}
}