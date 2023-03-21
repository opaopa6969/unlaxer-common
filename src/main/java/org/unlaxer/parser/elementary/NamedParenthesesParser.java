package org.unlaxer.parser.elementary;

import java.util.List;

import org.unlaxer.Name;
import org.unlaxer.Token;
import org.unlaxer.parser.Parser;
import org.unlaxer.parser.Parsers;
import org.unlaxer.parser.ascii.LeftParenthesisParser;
import org.unlaxer.parser.ascii.RightParenthesisParser;
import org.unlaxer.parser.combinator.WhiteSpaceDelimitedLazyChain;

public abstract class NamedParenthesesParser extends WhiteSpaceDelimitedLazyChain{
	
	private static final long serialVersionUID = 5506328765442699565L;

	public NamedParenthesesParser(Name name) {
		super(name);
	}
	
	public NamedParenthesesParser() {
		super();
	}

	List<Parser> parsers;



	public abstract Parser nameParser();
	
	public abstract Parser innerParser();
	
	
	@Override
	public void initialize() {
		
		parsers = 
			new Parsers(
				nameParser(),
				new LeftParenthesisParser(),
				innerParser(),
				new RightParenthesisParser()
			);
	}
	
	public static Token getInnerParserParsed(Token thisParserParsed) {
		return thisParserParsed.filteredChildren.get(2);
	}
	
	@Override
	public List<Parser> getLazyParsers() {
		return parsers;
	}


}