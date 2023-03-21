package org.unlaxer.parser.combinator;

import java.util.List;

import org.unlaxer.Name;
import org.unlaxer.parser.CollectingParser;
import org.unlaxer.parser.NonTerminallSymbol;
import org.unlaxer.parser.Parser;

public abstract class ConstructedMultiChildCollectingParser extends ConstructedMultiChildParser implements CollectingParser , NonTerminallSymbol{
	
	private static final long serialVersionUID = 3746117207729959189L;
	
	public ConstructedMultiChildCollectingParser(List<Parser> children) {
		super(children);
	}

	public ConstructedMultiChildCollectingParser(Name name, List<Parser> children) {
		super(name, children);
	}
}