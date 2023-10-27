package org.unlaxer;

import java.io.Serializable;

public interface Source extends Serializable{
	
	public RangedString peek(CodePointIndex startIndexInclusive, CodePointLength length);
	
  public default RangedString peek(CodePointIndex startIndexInclusive, CodePointIndex endIndexExclusive) {
    return peek(startIndexInclusive , new CodePointLength(endIndexExclusive.minus(startIndexInclusive)));
  }
	
	public default RangedString peekLast(CodePointIndex endIndexInclusive, CodePointLength length) {
	  
	  CodePointIndex start = endIndexInclusive.minus(length)
	      .createIfMatch(CodePointIndex::isNegative, ()->new CodePointIndex(0));
	  
	  CodePointIndex end = endIndexInclusive.minus(start);
		return peek(start , end);
	}
	
	public CodePointLength getLength();
	
	public LineNumber getLineNUmber(CodePointIndex Position);
	
}
