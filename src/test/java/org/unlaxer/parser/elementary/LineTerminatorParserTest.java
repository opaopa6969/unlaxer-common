package org.unlaxer.parser.elementary;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.unlaxer.Cursor.EndExclusiveCursor;
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
			EndExclusiveCursor consumed = parserCursor.getCursor(TokenKind.consumed);
			EndExclusiveCursor matchOnly= parserCursor.getCursor(TokenKind.matchOnly);
			
      assertEquals(1, consumed.getLineNumber().value());
      assertEquals(4, consumed.getPosition().value());
      assertEquals(0, matchOnly.getLineNumber().value());
      assertEquals(4, matchOnly.getPosition().value());
		}
		
		{
      TestResult testAllMatch = testAllMatch(
          new Chain(abcParser,lineTerminatorParser), "abc\n");
      
      ParserCursor parserCursor = testAllMatch.parseContext.getCurrent().getParserCursor();
      EndExclusiveCursor consumed = parserCursor.getCursor(TokenKind.consumed);
      parserCursor.getCursor(TokenKind.matchOnly);
      
      assertEquals(1, consumed.getLineNumber().value());
    }
		
		{
      TestResult testAllMatch = testAllMatch(
          new Chain(abcParser), "abc");
      
      ParserCursor parserCursor = testAllMatch.parseContext.getCurrent().getParserCursor();
      EndExclusiveCursor consumed = parserCursor.getCursor(TokenKind.consumed);
      parserCursor.getCursor(TokenKind.matchOnly);
      
      assertEquals(0, consumed.getLineNumber().value());
    }
		
		{
			TestResult testAllMatch = testAllMatch(
					new Chain(abcParser,lineTerminatorParser,abcParser,lineTerminatorParser), "abc\nabc");
			
			ParserCursor parserCursor = testAllMatch.parseContext.getCurrent().getParserCursor();
			EndExclusiveCursor consumed = parserCursor.getCursor(TokenKind.consumed);
			parserCursor.getCursor(TokenKind.matchOnly);
			
			assertEquals(1, consumed.getLineNumber().value());
		}
		
		{
      TestResult testAllMatch = testAllMatch(
          new Chain(abcParser,lineTerminatorParser,abcParser,lineTerminatorParser,abcParser,lineTerminatorParser),
          "abc\nabc\nabc");
      
      ParserCursor parserCursor = testAllMatch.parseContext.getCurrent().getParserCursor();
      EndExclusiveCursor consumed = parserCursor.getCursor(TokenKind.consumed);
      parserCursor.getCursor(TokenKind.matchOnly);
      
      assertEquals(2, consumed.getLineNumber().value());
    }
		
		{
      TestResult testAllMatch = testAllMatch(
          new Chain(abcParser,lineTerminatorParser,abcParser,lineTerminatorParser
              ,abcParser,lineTerminatorParser,abcParser,lineTerminatorParser),
          "abc\nabc\nabc\nabc");
      
      ParserCursor parserCursor = testAllMatch.parseContext.getCurrent().getParserCursor();
      EndExclusiveCursor consumed = parserCursor.getCursor(TokenKind.consumed);
      parserCursor.getCursor(TokenKind.matchOnly);
      
      assertEquals(3, consumed.getLineNumber().value());
    }
		
	}

}
