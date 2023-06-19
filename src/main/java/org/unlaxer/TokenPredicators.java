package org.unlaxer;

import java.util.function.Predicate;

public class TokenPredicators{
	
	public final static Predicate<Token> parserImplements(Class<?>... interfaceClasses){
		return token->{
			for (Class<?> interfaceClass : interfaceClasses) {
				
				if(interfaceClass.isAssignableFrom(token.parser.getClass())) {
					return true;
				}
			}
			return false;
		};
	}
	
	public final static Predicate<Token> afterToken(Token targetToken){
		return token-> targetToken.tokenRange.smallerThan(token.tokenRange);
	}
	
	public final static Predicate<Token> beforeToken(Token targetToken){
		return token-> targetToken.tokenRange.biggerThan(token.tokenRange);
	}
	
	public final static Predicate<Token> relation(Token targetToken , RangesRelation rangesRelation){
		return token-> targetToken.tokenRange.relation(token.tokenRange) == rangesRelation;
	}
	
	public final static Predicate<Token> hasTag(Tag tag){
		return token-> token.parser.hasTag(tag);
	}
	
	public final static Predicate<Token> hasTagInParent(Tag tag){
		return token-> token.parser.getParent()
				.map(_parent->_parent.hasTag(tag)).orElse(false);
	}
	
	public final static Predicate<Token> pathEndsWith(String path){
		return token-> token.parser.getPath().endsWith(path);
	}
	
	public final static Predicate<Token> parentPathEndsWith(String path){
		return token-> token.parser.getParentPath().endsWith(path);
	}
}