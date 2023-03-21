package org.unlaxer.parser.combinator;

import org.unlaxer.Name;
import org.unlaxer.parser.Parser;

public class Repeat extends ChildOccursWithTerminator {
	
	private static final long serialVersionUID = -5296440022640156880L;
	
	public final int minInclusive , maxInclusive;

	public Repeat(Parser inner , int minInclusive , int maxInclusive) {
		super(inner);
		this.minInclusive = minInclusive;
		this.maxInclusive = maxInclusive;
	}
	
	public Repeat(Name name , Parser inner , int minInclusive , int maxInclusive) {
		super(name , inner);
		this.minInclusive = minInclusive;
		this.maxInclusive = maxInclusive;
	}

	
	private Repeat(Name name , Parser inner , int minInclusive , int maxInclusive,Parser terminator) {
		super(name , inner,terminator);
		this.minInclusive = minInclusive;
		this.maxInclusive = maxInclusive;
	}
	
	public Repeat newWithTerminator(Parser terminator){
		return new Repeat(getName() , getChild(), minInclusive , maxInclusive , terminator);
	}


	@Override
	public int min() {
		return minInclusive;
	}

	@Override
	public int max() {
		return maxInclusive;
	}
}
