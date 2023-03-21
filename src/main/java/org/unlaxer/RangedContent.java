package org.unlaxer;

public interface RangedContent<T>{
	public Range getRange();
	public T getContent();
}