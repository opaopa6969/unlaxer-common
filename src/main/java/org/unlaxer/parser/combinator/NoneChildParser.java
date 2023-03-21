package org.unlaxer.parser.combinator;

import java.util.List;

import org.unlaxer.Name;
import org.unlaxer.parser.AbstractParser;
import org.unlaxer.parser.ChildOccurs;
import org.unlaxer.parser.Parser;

public abstract class NoneChildParser extends AbstractParser {

	private static final long serialVersionUID = 6972225718514512056L;

	public NoneChildParser() {
		super();
	}

	public NoneChildParser(Name name) {
		super(name);
	}

	@Override
	public final ChildOccurs getChildOccurs() {
		return ChildOccurs.none;
	}

	@Override
	public final void prepareChildren(List<Parser> childrenContainer) {
		
	}
	
	public Parser getLazyParser() {
		return getParser();
	}
}