package org.unlaxer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



public class TransactionElement implements Serializable{
	
	private static final long serialVersionUID = -4168699143819523755L;

	Optional<TokenKind> tokenKind;
	ParserCursor parserCursor ;
	
	boolean resetMatchedWithConsumed = true;
	
	public final List<Token> tokens = new ArrayList<Token>();
	
	public TransactionElement(ParserCursor parserCursor) {
		super();
		this.parserCursor = new ParserCursor(parserCursor,true);
		tokenKind = Optional.empty();
	}
	
	
	public TransactionElement(ParserCursor cursor, boolean resetMatchedWithConsumed) {
		super();
		this.parserCursor = cursor;
		this.resetMatchedWithConsumed = resetMatchedWithConsumed;
	}


	public TransactionElement createNew() {
		return new TransactionElement(new ParserCursor(parserCursor,resetMatchedWithConsumed),resetMatchedWithConsumed);
	}
	
	public void consume(int length){
		parserCursor.addPosition(length);
	}
	
	public void matchOnly(int length){
		parserCursor.addMatchedPosition(length);
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
			.map(Token::getSource)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.collect(Collectors.joining());
		return Optional.of(parsedString);
	}
	
	public CodePointIndex getPosition(TokenKind tokenKind){
		return parserCursor.getPosition(tokenKind);
	}
	
	public Cursor getCursor(TokenKind tokenKind){
	   return parserCursor.getCursor(tokenKind);
  }
	
	public List<Token> getTokens(){
		return tokens;
	}

	public Optional<TokenKind> getTokenKind() {
		return tokenKind;
	}

	public ParserCursor getParserCursor() {
		return parserCursor;
	}

	public void setCursor(ParserCursor cursor) {
		this.parserCursor = cursor;
	}
	
	public void setResetMatchedWithConsumed(boolean resetMatchedWithConsumed) {
		this.resetMatchedWithConsumed = resetMatchedWithConsumed;
	}
}