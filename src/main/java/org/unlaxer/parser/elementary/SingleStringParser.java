package org.unlaxer.parser.elementary;

import org.unlaxer.Name;
import org.unlaxer.RangedString;
import org.unlaxer.Token;
import org.unlaxer.TokenKind;
import org.unlaxer.context.ParseContext;
import org.unlaxer.parser.TerminalSymbol;


public abstract class SingleStringParser extends AbstractTokenParser implements TerminalSymbol{

	private static final long serialVersionUID = 2701391051407683974L;

	public SingleStringParser() {
		super();
	}

	public SingleStringParser(Name name) {
		super(name);
	}

	@Override
	public Token getToken(ParseContext parseContext,TokenKind tokenKind,boolean invertMatch) {
		
		RangedString peeked = parseContext.peek(tokenKind , 1);
		Token token = 
			peeked.token.isPresent() && (invertMatch ^ isMatch(peeked.token.get().substring(0,1)))?
				new Token(tokenKind , peeked, this) : 
				Token.empty(tokenKind , parseContext.getConsumedPosition(),this);
		return token;
	}

	public abstract boolean isMatch(String target);
	
}