package org.unlaxer.parser.elementary;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.unlaxer.Name;
import org.unlaxer.parser.Parser;
import org.unlaxer.parser.StaticParser;
import org.unlaxer.parser.combinator.Choice;
import org.unlaxer.parser.combinator.ZeroOrMore;

public class WildCardStringTerninatorParser extends ZeroOrMore implements StaticParser {

	private static final long serialVersionUID = -3386398191774012367L;
	
	static final Parser wildCardStringParser = new WildCardStringParser();
	
	public WildCardStringTerninatorParser(String... excludes) {
		super(wildCardStringParser , createTerminator(excludes));
	}

	public WildCardStringTerninatorParser(Name name, String... excludes) {
		super(name , wildCardStringParser ,  createTerminator(excludes));
	}
	
	static Parser createTerminator(String[] terminators) {
	  
	  List<Parser> parsers =  Stream.of(terminators)
	    .map(WordParser::new)
	    .collect(Collectors.toList());
	  Choice choice = new Choice(parsers);
	  
	  return choice;
	}
}