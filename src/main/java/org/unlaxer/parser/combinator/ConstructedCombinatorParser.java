package org.unlaxer.parser.combinator;

import java.util.List;

import org.unlaxer.Name;
import org.unlaxer.parser.MetaFunctionParser;
import org.unlaxer.parser.NonTerminallSymbol;
import org.unlaxer.parser.Parser;
import org.unlaxer.parser.Parsers;

public abstract class ConstructedCombinatorParser extends ConstructedMultiChildCollectingParser 
	implements MetaFunctionParser , NonTerminallSymbol {

	private static final long serialVersionUID = -517554162836750441L;
	
	public ConstructedCombinatorParser(List<Parser> children) {
		super(children);
	}

	public ConstructedCombinatorParser(Parser... children) {
		super(new Parsers(children));
	}

	public ConstructedCombinatorParser(Name name, List<Parser> children){
		super(name, children);
	}

	public ConstructedCombinatorParser(Name name,Parser... children) {
		super(name,new Parsers(children));	
	}

}
