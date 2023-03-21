package org.unlaxer.parser.elementary;

import java.util.List;

import org.unlaxer.Name;
import org.unlaxer.parser.Parser;
import org.unlaxer.parser.Parsers;
import org.unlaxer.parser.StaticParser;
import org.unlaxer.parser.ascii.BackSlashParser;
import org.unlaxer.parser.combinator.LazyChain;

public class EscapeInQuotedParser extends LazyChain implements StaticParser{
	
	private static final long serialVersionUID = -3666240429142759284L;
	
	public EscapeInQuotedParser() {
		super();
	}

	public EscapeInQuotedParser(Name name) {
		super(name);
	}
	
	@Override
	public List<Parser> getLazyParsers(){
		return new Parsers(
			new BackSlashParser(),
			new WildCardStringParser()
		);
	}

}