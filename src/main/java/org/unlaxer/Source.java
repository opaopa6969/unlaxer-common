package org.unlaxer;

import java.io.Serializable;

public interface Source extends Serializable{
	
	public RangedString peek(CodePointIndex startIndexInclusive, CodePointLength length);
	
  public default RangedString peek(CodePointIndex startIndexInclusive, Index endIndexExclusive) {
    return peek(startIndexInclusive , new CodePointLength(endIndexExclusive.value - startIndexInclusive.value));
  }
	
	public default RangedString peekLast(CodePointIndex endIndexInclusive, CodePointLength length) {
		int start = endIndexInclusive.value - length.value;
		start = start <0 ? 0 : start;
		int end = endIndexInclusive.value - start;
		return peek(new CodePointIndex(start) , new CodePointIndex(end));
	}
	
	public CodePointLength getLength();
	
	public LineNumber getLineNUmber(CodePointIndex Position);
	
}
