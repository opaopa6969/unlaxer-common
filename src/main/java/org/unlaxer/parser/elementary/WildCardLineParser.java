package org.unlaxer.parser.elementary;

import java.util.List;

import org.unlaxer.parser.Parser;
import org.unlaxer.parser.Parsers;
import org.unlaxer.parser.combinator.LazyChain;

public class WildCardLineParser extends LazyChain{

	@Override
	public List<Parser> getLazyParsers() {
	    return new Parsers(
	        Parser.get(StartOfLineParser.class),
	        new WildCardStringTerninatorParser(
	    		true ,
	    		Parser.get(LineTerminatorParser.class)
	        ),
	        Parser.get(LineTerminatorParser.class)
	    );
	}
}