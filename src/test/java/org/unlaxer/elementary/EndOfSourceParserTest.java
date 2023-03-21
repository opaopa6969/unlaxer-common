package org.unlaxer.elementary;

import org.junit.Test;
import org.unlaxer.ParserTestBase;
import org.unlaxer.listener.OutputLevel;
import org.unlaxer.parser.elementary.EndOfSourceParser;

public class EndOfSourceParserTest extends ParserTestBase{

	@Test
	public void test() {
		
		setLevel(OutputLevel.detail);
		
		EndOfSourceParser emptyParser = new EndOfSourceParser();
		
		
		testAllMatch(emptyParser, "");
		testUnMatch(emptyParser, " ");
	}
}
