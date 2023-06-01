package org.unlaxer.parser.elementary;

import static org.junit.Assert.*;

import org.junit.Test;
import org.unlaxer.Cursor;
import org.unlaxer.ParserCursor;
import org.unlaxer.ParserTestBase;
import org.unlaxer.TestResult;
import org.unlaxer.TokenKind;
import org.unlaxer.parser.combinator.Chain;

public class LineTerminatorParserTest extends ParserTestBase{

	@Test
	public void test() {
		LineTerminatorParser lineTerminatorParser = new LineTerminatorParser();
		WordParser abcParser = new WordParser("abc");

		{
			testUnMatch(lineTerminatorParser, "abc");
			testAllMatch(new Chain(abcParser,lineTerminatorParser), "abc");
			TestResult testPartialMatch = testPartialMatch(new Chain(abcParser,lineTerminatorParser), "abc\ndef","abc\n");
			
			ParserCursor parserCursor = testPartialMatch.parseContext.getCurrent().getParserCursor();
			Cursor consumed = parserCursor.getCursor(TokenKind.consumed);
			parserCursor.getCursor(TokenKind.matchOnly);
			
			assertEquals(1, consumed.getLineNumber());
		}
		
		{
			TestResult testAllMatch = testAllMatch(
					new Chain(abcParser,lineTerminatorParser,abcParser,lineTerminatorParser), "abc\nabc");
			
			ParserCursor parserCursor = testAllMatch.parseContext.getCurrent().getParserCursor();
			Cursor consumed = parserCursor.getCursor(TokenKind.consumed);
			parserCursor.getCursor(TokenKind.matchOnly);
			
			assertEquals(2, consumed.getLineNumber());
		}
		
	}

}
