package org.unlaxer;

import java.util.List;

import org.unlaxer.parser.Parser;

public class TypedToken<P extends Parser> extends Token{



	public TypedToken(TokenKind tokenKind, List<Token> tokens, P parser, int position) {
		super(tokenKind, tokens, parser, position);
	}

	public TypedToken(TokenKind tokenKind, RangedString token, P parser, List<Token> children) {
		super(tokenKind, token, parser, children);
	}

	public TypedToken(TokenKind tokenKind, RangedString token, P parser) {
		super(tokenKind, token, parser);
	}
	
	public TypedToken(Token token , P parser) {
		super(
			token.tokenKind, 
			Token.createRangedString(token.getOriginalChildren(), token.tokenRange.startIndexInclusive) ,
			parser,
			token.getOriginalChildren()
		);
	}

	@SuppressWarnings("unchecked")
	public P getParser() {
		return (P) super.getParser();
	}
	
	@SuppressWarnings("unchecked")
	public Class<? extends P> getParserClass(){
		return (Class<? extends P>) getParser().getClass();
	}
}