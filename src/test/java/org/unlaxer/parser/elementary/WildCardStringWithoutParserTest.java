package org.unlaxer.parser.elementary;

import org.junit.Test;
import org.unlaxer.ParserTestBase;

public class WildCardStringWithoutParserTest extends ParserTestBase{

	@Test
	public void test() {
		
		var parser = new WildCardStringTerninatorParser("B");
		
		testAllMatch(parser,"/* niku */");
		testUnMatch(parser,"/* nikuB */");
	}

}
