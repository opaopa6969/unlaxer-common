package org.unlaxer.referencer;

import org.junit.Test;
import org.unlaxer.Name;
import org.unlaxer.ParserTestBase;
import org.unlaxer.listener.OutputLevel;
import org.unlaxer.parser.combinator.Chain;
import org.unlaxer.parser.combinator.MatchOnly;
import org.unlaxer.parser.combinator.OneOrMore;
import org.unlaxer.parser.elementary.ParenthesesParser;
import org.unlaxer.parser.elementary.WildCardStringParser;
import org.unlaxer.parser.elementary.WordParser;
import org.unlaxer.parser.posix.AlphabetParser;
import org.unlaxer.parser.referencer.MatchedTokenParser;

public class MatchedTokenParserTest extends ParserTestBase{

	@Test
	public void test() {
		setLevel(OutputLevel.simple);
		
		Name elementsName = Name.of("inner");
		OneOrMore inner = new OneOrMore(elementsName , new AlphabetParser());
		Chain parser = new Chain(
			new ParenthesesParser(//
				inner//
			),
			new MatchedTokenParser(inner)
		);
		
		for(boolean createMeta : new boolean[]{true}){
			
			testAllMatch(parser, "(abc)abc",createMeta);
			testAllMatch(parser, "(abz)abz",createMeta);
			testUnMatch(parser, "(abz)abu",createMeta);
		}
		
	}
	
	@Test
	public void testOccursWithoutTerminator() {
		setLevel(OutputLevel.simple);
		
		OneOrMore inner = new OneOrMore(new AlphabetParser());
		Chain parser = new Chain(
			inner,//
			new WordParser(":"),//separetor
			new MatchedTokenParser(inner)
		);
		
		testAllMatch(parser, "abc:abc",true);
		testAllMatch(parser, "abz:abz",true);
		testUnMatch(parser, "abz:abu",true);
	}

	@Test
	public void testOccursWithTerminator() {
		setLevel(OutputLevel.simple);
		
		OneOrMore inner = new OneOrMore(new WildCardStringParser())
			.newWithTerminator(new MatchOnly(new WordParser(":")));
		
		Chain parser = new Chain(
			inner,//
			new WordParser(":"),//separetor
			new MatchedTokenParser(inner)
		);
		
		testAllMatch(parser, "abc:abc",true);
		testAllMatch(parser, "abz:abz",true);
		testUnMatch(parser, "abz:abu",true);
	}


}
