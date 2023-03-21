package org.unlaxer.parser.combinator;

import org.unlaxer.Name;
import org.unlaxer.parser.Parser;

public class Optional extends ChildOccursWithTerminator {

	private static final long serialVersionUID = 9178853471703766611L;

	public Optional(Parser inner) {
		super(inner);
	}

	public Optional(Name name, Parser inner) {
		super(name, inner);
	}

	@Override
	public int min() {
		return 0;
	}

	@Override
	public int max() {
		return 1;
	}
}
