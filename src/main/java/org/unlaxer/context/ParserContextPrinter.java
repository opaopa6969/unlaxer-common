package org.unlaxer.context;

import org.unlaxer.Cursor;
import org.unlaxer.ParserCursor;
import org.unlaxer.RangedString;
import org.unlaxer.TokenKind;
import org.unlaxer.listener.OutputLevel;

public class ParserContextPrinter {

	public static String get(ParseContext parseContext , OutputLevel level){
		
		int position = parseContext.getPosition(TokenKind.consumed);
		if(level.isMostDetail()) {
			ParserCursor parserCursor = parseContext.getCurrent().getParserCursor();
			Cursor consumed= parserCursor.getCursor(TokenKind.consumed);
			Cursor matchOnly= parserCursor.getCursor(TokenKind.matchOnly);
			RangedString peek = parseContext.peekLast(position,20);
			
			return String.format("CON(L:%d,P:%d) MO(L:%d,P:%d) Last20='%s' ", 
					consumed.getLineNumber(),
					consumed.getPositionInLine(),
					matchOnly.getLineNumber(),
					matchOnly.getPositionInLine(),
					peek.token.map(ParserContextPrinter::normalize).orElse(""));
		}
		
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
