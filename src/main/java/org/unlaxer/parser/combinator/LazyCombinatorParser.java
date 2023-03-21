package org.unlaxer.parser.combinator;

import org.unlaxer.Name;
import org.unlaxer.parser.MetaFunctionParser;
import org.unlaxer.parser.NonTerminallSymbol;

public abstract class LazyCombinatorParser extends LazyMultiChildCollectingParser 
	implements MetaFunctionParser , NonTerminallSymbol {

	private static final long serialVersionUID = 3966216675279889282L;

	public LazyCombinatorParser() {
		super();
	}

	public LazyCombinatorParser(Name name) {
		super(name);
	}
}