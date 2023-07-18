package org.unlaxer.parser.elementary;

import java.util.List;

import org.unlaxer.Parsed;
import org.unlaxer.TokenKind;
import org.unlaxer.Parsed.Status;
import org.unlaxer.context.ParseContext;
import org.unlaxer.parser.AbstractParser;
import org.unlaxer.parser.ChildOccurs;
import org.unlaxer.parser.Parser;

public class StartOfSourceParser extends AbstractParser{

	@Override
	public void prepareChildren(List<Parser> childrenContainer) {
	}

	@Override
	public ChildOccurs getChildOccurs() {
		return ChildOccurs.none;
	}

	@Override
	public Parser createParser() {
		return this;
	}
	
	@Override
	public Parsed parse(ParseContext parseContext, TokenKind tokenKind, boolean invertMatch) {
		
		int position = parseContext.getPosition(tokenKind);
		boolean match = (position == 0) ^ invertMatch;
		return new Parsed(match ? Status.succeeded : Status.failed);
	}
}