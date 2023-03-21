package org.unlaxer;

import java.io.Serializable;

public interface Source extends Serializable{
	
	public RangedString peek(int startIndexInclusive, int length);
	
	public int getLength();
	
}
