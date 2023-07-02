package org.unlaxer;

import java.io.Serializable;

public interface Source extends Serializable{
	
	public RangedString peek(int startIndexInclusive, int length);
	public default RangedString peekLast(int endIndexInclusive, int length) {
		int start = endIndexInclusive-length;
		start = start <0 ? 0 : start;
		return peek(start , length);
	}
	
	public int getLength();
	
}
