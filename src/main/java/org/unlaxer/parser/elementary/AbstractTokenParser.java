package org.unlaxer.parser.elementary;

import java.util.function.Consumer;

import org.unlaxer.Name;
import org.unlaxer.Parsed;
import org.unlaxer.Token;
import org.unlaxer.TokenKind;
import org.unlaxer.context.ParseContext;
import org.unlaxer.parser.Parser;
import org.unlaxer.parser.combinator.NoneChildParser;


public abstract class AbstractTokenParser extends NoneChildParser {
	
	private static final long serialVersionUID = -1754533020946090748L;
	
	public AbstractTokenParser() {
		super();
	}

	public AbstractTokenParser(Name name) {
		super(name);
	}

	@Override
	public Parsed parse(ParseContext parseContext,TokenKind tokenKind,boolean invertMatch){

		
		Token token = getToken(parseContext,tokenKind,invertMatch);
		
		if(token.tokenSource.isPresent()){
			
			parseContext.getCurrent().addToken(token,tokenKind);
		}
		
		
		Consumer<Integer> positionIncrement = tokenKind.isConsumed() ? 
				parseContext::consume : parseContext::matchOnly;
		
		token.tokenSource.map(String::length).ifPresent(positionIncrement);
		return token.tokenSource.isPresent() ?
				new Parsed(token):
				Parsed.FAILED;
	}
	
	public abstract Token getToken(ParseContext parseContext,TokenKind tokenKind , boolean invertMatch);

	@Override
	public Parser getParser() {
		return this;
	}

	@Override
	public Parser createParser() {
		return this;
	}
}