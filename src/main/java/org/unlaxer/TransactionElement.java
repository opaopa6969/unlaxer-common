package org.unlaxer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



public class TransactionElement implements Serializable{
	
	private static final long serialVersionUID = -4168699143819523755L;

	Optional<TokenKind> tokenKind;
	ParserCursor cursor ;
	
	boolean resetMatchedWithConsumed = true;
	
	public final List<Token> tokens = new ArrayList<Token>();
	
	public TransactionElement(ParserCursor parserCursor) {
		super();
		this.cursor = new ParserCursor(parserCursor,true);
		tokenKind = Optional.empty();
	}
	
	
	public TransactionElement(ParserCursor cursor, boolean resetMatchedWithConsumed) {
		super();
		this.cursor = cursor;
		this.resetMatchedWithConsumed = resetMatchedWithConsumed;
	}


	public TransactionElement createNew() {
		return new TransactionElement(new ParserCursor(cursor,resetMatchedWithConsumed),resetMatchedWithConsumed);
	}
	
	public void consume(int length){
		cursor.addPosition(length);
	}
	
	public void matchOnly(int length){
		cursor.addMatchedPosition(length);
	}
	
	public void addToken(Token token){
		addToken(token,TokenKind.consumed);
	}
	
	public void addToken(Token token ,TokenKind tokenKind){
		tokens.add(token);
		this.tokenKind = Optional.of(tokenKind);
	}
	
	public Optional<String> getTokenString(){
		if(tokens.isEmpty()){
			return Optional.empty();
		}
		String parsedString = tokens.stream()
			.map(Token::getToken)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.collect(Collectors.joining());
		return Optional.of(parsedString);
	}
	
	public int getPosition(TokenKind tokenKind){
		return cursor.getPosition(tokenKind);
	}
	
	public List<Token> getTokens(){
		return tokens;
	}

	public Optional<TokenKind> getTokenKind() {
		return tokenKind;
	}

	public ParserCursor getParserCursor() {
		return cursor;
	}

	public void setCursor(ParserCursor cursor) {
		this.cursor = cursor;
	}
	
	public void setResetMatchedWithConsumed(boolean resetMatchedWithConsumed) {
		this.resetMatchedWithConsumed = resetMatchedWithConsumed;
	}
}