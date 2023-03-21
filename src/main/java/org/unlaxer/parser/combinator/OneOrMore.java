package org.unlaxer.parser.combinator;

import org.unlaxer.Name;
import org.unlaxer.parser.Parser;

public class OneOrMore extends ChildOccursWithTerminator {

	private static final long serialVersionUID = 3883160475654738794L;

	public OneOrMore(Parser inner) {
		super(inner);
	}
	
	private OneOrMore(Name name, Parser inner, Parser terminator) {
		super(name, inner, terminator);
	}
	
	public OneOrMore newWithTerminator(Parser terminator){
		return new OneOrMore(getName() , getChild(),terminator);
	}

	public OneOrMore(Name name, Parser inner) {
		super(name, inner);
	}

	@Override
	public int min() {
		return 1;
	}

	@Override
	public int max() {
		return Integer.MAX_VALUE;
	}

}
