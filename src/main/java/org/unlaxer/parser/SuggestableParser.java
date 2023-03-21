package org.unlaxer.parser;

import java.util.Optional;

import org.unlaxer.RangedString;
import org.unlaxer.Token;
import org.unlaxer.TokenKind;
import org.unlaxer.TransactionElement;
import org.unlaxer.context.ParseContext;
import org.unlaxer.parser.elementary.AbstractTokenParser;

public abstract class SuggestableParser extends AbstractTokenParser {
	
	private static final long serialVersionUID = -7966896868712698646L;
	
	public final boolean ignoreCase;
	public final String[] targetStrings;
	
	public SuggestableParser(boolean ignoreCase, String... targetStrings) {
		super();
		this.ignoreCase = ignoreCase;
		this.targetStrings = targetStrings;
	}

	@Override
	public Token getToken(ParseContext parseContext,TokenKind tokenKind ,boolean invertMatch) {
		
		for (String targetString : targetStrings) {
			RangedString peeked = parseContext.peek(tokenKind , targetString.length());
			if(peeked.token.map(baseString->equals(targetString,baseString)).orElse(false)){
				return new Token(tokenKind ,  peeked, this);
			}
		}
//		addSuggests(parseContext);
		
		return Token.empty(tokenKind , parseContext.getConsumedPosition(),this);
	}
	
	//FIXME!
	@SuppressWarnings("unused")
	private void addSuggests(ParseContext parseContext) {
		TransactionElement current = parseContext.getCurrent();
		int position = current.getPosition(TokenKind.consumed);
		for (String targetString : targetStrings) {
			RangedString peeked = parseContext.peek(TokenKind.consumed ,targetString.length());
			if(peeked.token.isPresent()){
				continue;
			}
//			String peekWithMax = parseContext.peekWithMax(targetString.length()-1);
//			if(targetString.startsWith(peekWithMax)){
//				parseContext.addSuggests(this , position, targetString);
//			}
		}
	}
	
	public Optional<Suggest> getSuggests(String test){
		
		Suggest suggests = new Suggest(this);
		
		//TODO camel case matching, ignore case
		for(int endIndex= test.length() ; endIndex > 0 ; endIndex--){
			String currentTest = test.substring(0, endIndex);
			for(String targetString :targetStrings){
				if(targetString.startsWith(currentTest)){
					suggests.words.add(targetString);
				}
			}
			if(suggests.words.size() >0){
				break;
			}
		}
		return suggests.words.size() ==0 ? 
				Optional.empty():
				Optional.of(suggests);
	}
	
	public abstract String getSuggestString(String matchedString);
	
	boolean equals(String targetString , String baseString){
		return ignoreCase ? 
				targetString.equalsIgnoreCase(baseString):
				targetString.equals(baseString);
	}
}
