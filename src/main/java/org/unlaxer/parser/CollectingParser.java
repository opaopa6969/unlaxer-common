package org.unlaxer.parser;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.unlaxer.Index;
import org.unlaxer.Token;
import org.unlaxer.TokenKind;



public interface CollectingParser extends Parser {
	
	public default Token collect(List<Token> tokens, Index position , TokenKind tokenKind ,
			Predicate<Token> tokenFilter){
			
		return new Token(tokenKind,
				tokens.stream()
					.filter(tokenFilter)
					.collect(Collectors.toList())
				, this //
				, position);

	}
	
	public default Token collect(List<Token> tokens, Index position , TokenKind tokenKind){
		return collect(tokens, position, tokenKind , token->true);
	}
}
