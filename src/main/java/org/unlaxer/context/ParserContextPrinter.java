package org.unlaxer.context;

import org.unlaxer.RangedString;
import org.unlaxer.TokenKind;
import org.unlaxer.listener.OutputLevel;

public class ParserContextPrinter {

	public static String get(ParseContext parseContext , OutputLevel level){
		int position = parseContext.getPosition(TokenKind.consumed);
		int matchOnlyPosition = parseContext.getPosition(TokenKind.matchOnly);
		RangedString peek = parseContext.peek(position,1);
		return String.format("position:(c:%d m:%d) targetchar='%s' ", 
				position,
				matchOnlyPosition,
				peek.token.map(ParserContextPrinter::normalize).orElse(""));
	}
	
	static String normalize(String word){
		if("\r".equals(word)){
			return "\\r";
		}
		if("\n".equals(word)){
			return "\\n";
		}
		return word;
	}

}
