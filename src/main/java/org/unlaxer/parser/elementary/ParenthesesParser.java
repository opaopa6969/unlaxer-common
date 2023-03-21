package org.unlaxer.parser.elementary;

import java.util.List;

import org.unlaxer.Name;
import org.unlaxer.Token;
import org.unlaxer.parser.Parser;
import org.unlaxer.parser.Parsers;
import org.unlaxer.parser.ascii.LeftParenthesisParser;
import org.unlaxer.parser.ascii.RightParenthesisParser;
import org.unlaxer.parser.combinator.WhiteSpaceDelimitedLazyChain;

public class ParenthesesParser extends WhiteSpaceDelimitedLazyChain {

	private static final long serialVersionUID = 6964996290002171327L;
	
	Parser inner;
	

	public ParenthesesParser(Name name , Parser inner) {
		super(name);
		this.inner = inner;
	}


	public ParenthesesParser(Parser inner) {
		super();
		this.inner = inner;
	}

	
	public static Token getParenthesesed(Token parenthesesed ){
		if(false == parenthesesed.parser instanceof ParenthesesParser){
			throw new IllegalArgumentException("this token did not generate from " + 
				ParenthesesParser.class.getName());
		}
		Parser contentsParser = ParenthesesParser.class.cast(parenthesesed.parser).inner;
		return parenthesesed.filteredChildren.stream()
			.filter(token->token.parser.equals(contentsParser))
			.findFirst().get();
	}
	
	public Parser getParenthesesedParser(){
		return inner;
	}

	List<Parser> parsers;


	@Override
	public void initialize() {
		
		parsers = 
			new Parsers(
				new LeftParenthesisParser(),
				inner,
				new RightParenthesisParser()
			);

	}


	@Override
	public List<Parser> getLazyParsers() {
		return parsers;
	}
	
	public static Token getInnerParserParsed(Token thisParserParsed) {
//		return thisParserParsed.filteredChildren.get(3);
		return thisParserParsed.filteredChildren.get(1);
	}
}
