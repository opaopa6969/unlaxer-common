package org.unlaxer.parser.elementary;

import org.junit.Test;
import org.unlaxer.ParserTestBase;

public class DoubleQuotedParserTest extends ParserTestBase{

	@Test
	public void test() {
		
		DoubleQuotedParser doubleQuotedParser = new DoubleQuotedParser();
		
		testAllMatch(doubleQuotedParser, "\"\"");
		testAllMatch(doubleQuotedParser, "\"abc,123\"");
		testAllMatch(doubleQuotedParser, "\"123\\\"abc\"");
		testAllMatch(doubleQuotedParser, "\"\\\"\"");
		testAllMatch(doubleQuotedParser, "\"\\\"\"");
	}

}
