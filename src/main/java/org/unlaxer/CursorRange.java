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
	    AttachedCodePointIndex startIndexInclusive,
	    AttachedCodePointIndex endIndexExclusive,
      CodePointOffset offsetFromRoot,
      SourceKind sourceKind,
      PositionResolver positionResolver
	    ) {
	  return new CursorRange(
        new StartInclusiveCursorImpl(sourceKind,positionResolver,startIndexInclusive),
        new EndExclusiveCursorImpl(sourceKind,positionResolver,endIndexExclusive)
	  );
	  
	}
	
	public final StartInclusiveCursor startIndexInclusive() {
		return startIndexInclusive;
	}
	
	public final EndExclusiveCursor endIndexExclusive() {
		return endIndexExclusive;
	}
	
	public boolean isSingle() {
		return startIndexInclusive.position().value(SourceKind.subSource) == endIndexExclusive.position().value(SourceKind.subSource);
	}

	public boolean match(AttachedCodePointIndex position){
		return position.ge(startIndexInclusive.position()) 
		    && position.lt(endIndexExclusive.position());
	}

	
  public boolean lt(AttachedCodePointIndex position){
	  return position.ge(endIndexExclusive.position());
	}

	public boolean lessThan(AttachedCodePointIndex position){
		return position.ge(endIndexExclusive.position());
	}
	
	public boolean graterThan(AttachedCodePointIndex position){
		return position.lt(startIndexInclusive.position());
	}
	
  public boolean gt(AttachedCodePointIndex position){
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
	  AttachedCodePointIndex otherStart = other.startIndexInclusive.position();
	  AttachedCodePointIndex otherEnd= other.endIndexExclusive.position();
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
	
	public IntStream asIntStream(SourceKind sourceKind) {
		return IntStream.range(startIndexInclusive.position().value(sourceKind), 
		    endIndexExclusive.position().value(sourceKind));
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + endIndexExclusive.position().value(SourceKind.root);
		result = prime * result + startIndexInclusive.position().value(SourceKind.root);
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
		if (endIndexExclusive.position().value(SourceKind.root) != other.endIndexExclusive.position().value(SourceKind.root))
			return false;
		if (startIndexInclusive.position().value(SourceKind.root) != other.startIndexInclusive.position().value(SourceKind.root))
			return false;
		return true;
	}
	
	
	@Override
	public int compareTo(CursorRange other) {
		int value = startIndexInclusive.position().value(SourceKind.root) - other.startIndexInclusive.position().value(SourceKind.root);
		if(value == 0) {
			return endIndexExclusive.position().value(SourceKind.root) - other.endIndexExclusive.position().value(SourceKind.root);
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