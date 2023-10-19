package org.unlaxer;

import java.io.Serializable;

public interface Source extends Serializable{
	
	public RangedString peek(int startIndexInclusive, int length);
	
	public default RangedString peek(Index startIndexInclusive, Length length) {
	  return peek(startIndexInclusive.value, length.value);
	}
	
  public default RangedString peek(Index startIndexInclusive, Index endIndexExclusive) {
    return peek(startIndexInclusive.value, endIndexExclusive.value - startIndexInclusive.value);
  }
  
  public default RangedString peekLast(Index endIndexInclusive, Length length) {
    return peekLast(endIndexInclusive.value, length.value);
  }
	
	public default RangedString peekLast(int endIndexInclusive, int length) {
		int start = endIndexInclusive-length;
		start = start <0 ? 0 : start;
		int end = endIndexInclusive - start;
		return peek(start , end);
	}
	
	public int getLength();
	
	public default Length length() {
	  return new Length(getLength());
	}
	
}
