package org.unlaxer.parser.elementary;

import org.unlaxer.CodePointIndex;
import org.unlaxer.CodePointLength;
import org.unlaxer.Name;
import org.unlaxer.Source;
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
		
		Source peeked = parseContext.peek(tokenKind , new CodePointLength(1));
		Token token = 
			peeked.isPresent() && (invertMatch ^ isMatch(
			    peeked.subSource(new CodePointIndex(0,peeked),new CodePointIndex(1,peeked)).toString()))?
				new Token(tokenKind , peeked, this) : 
				Token.empty(tokenKind , parseContext.getCursor(TokenKind.consumed),this);
		return token;
	}

	public abstract boolean isMatch(String target);
	
}