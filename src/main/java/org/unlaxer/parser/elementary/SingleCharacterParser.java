package org.unlaxer.parser.elementary;

import org.unlaxer.CodePointLength;
import org.unlaxer.Name;
import org.unlaxer.Source;
import org.unlaxer.Token;
import org.unlaxer.TokenKind;
import org.unlaxer.context.ParseContext;
import org.unlaxer.parser.TerminalSymbol;

public abstract class SingleCharacterParser extends AbstractTokenParser implements TerminalSymbol{

	private static final long serialVersionUID = -4800259064123105938L;

	public SingleCharacterParser() {
		super();
	}

	public SingleCharacterParser(Name name) {
		super(name);
	}

	@Override
	public Token getToken(ParseContext parseContext,TokenKind tokenKind , boolean invertMatch) {
		
		Source peeked = parseContext.peek(tokenKind , new CodePointLength(1));
		Token token = 
			peeked.isPresent() && (invertMatch ^ isMatch(peeked.charAt(0)))?
				new Token(tokenKind , peeked, this) : 
				Token.empty(tokenKind , parseContext.getCursor(TokenKind.consumed),this,parseContext.getSource());
		return token;
	}

	public abstract boolean isMatch(char target);

}
