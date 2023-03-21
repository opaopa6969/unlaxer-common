package org.unlaxer.parser.combinator;

import org.unlaxer.Name;
import org.unlaxer.parser.Parser;

public class ZeroOrOne extends Optional{

	private static final long serialVersionUID = 7544142678234641766L;

	public ZeroOrOne(Name name, Parser inner) {
		super(name, inner);
	}

	public ZeroOrOne(Parser inner) {
		super(inner);
	}
}