package org.unlaxer.context;

import org.unlaxer.CodePointIndex;
import org.unlaxer.CodePointLength;
import org.unlaxer.Cursor;
import org.unlaxer.ParserCursor;
import org.unlaxer.RangedString;
import org.unlaxer.TokenKind;
import org.unlaxer.listener.OutputLevel;

public class ParserContextPrinter {

	public static String get(ParseContext parseContext , OutputLevel level){
		
		CodePointIndex position = parseContext.getPosition(TokenKind.consumed);
		if(level.isMostDetail()) {
			ParserCursor parserCursor = parseContext.getCurrent().getParserCursor();
			Cursor consumed= parserCursor.getCursor(TokenKind.consumed);
			Cursor matchOnly= parserCursor.getCursor(TokenKind.matchOnly);
			RangedString peek = parseContext.peekLast(position, new CodePointLength(20));
			
			return String.format("CON(L:%d,P:%d) MO(L:%d,P:%d) Last20='%s' ", 
					consumed.getLineNumber(),
//					consumed.getPositionInLine(), // きちんと実装されてない
					consumed.getPosition(),
					matchOnly.getLineNumber(),
//					matchOnly.getPositionInLine(),
					matchOnly.getPosition(),
					peek.token.map(ParserContextPrinter::normalize).orElse(""));
		}
		
		CodePointIndex matchOnlyPosition = parseContext.getPosition(TokenKind.matchOnly);
		RangedString peek = parseContext.peek(position,new CodePointLength(1));
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
