package org.unlaxer.parser.combinator;

import org.unlaxer.Name;
import org.unlaxer.Parsed;
import org.unlaxer.TokenKind;
import org.unlaxer.context.ParseContext;
import org.unlaxer.parser.Parser;

public class Not extends ConstructedSingleChildParser {

	private static final long serialVersionUID = -1735074020146586037L;

	public Not(Name name, Parser children) {
		super(name, children);
	}

	public Not(Parser child) {
		super(child);
	}

	@Override
	public Parsed parse(ParseContext parseContext, TokenKind tokenKind, boolean invertMatch) {
		
		parseContext.startParse(this, parseContext, tokenKind, invertMatch);
		Parsed parsed = getChild().parse(parseContext,tokenKind , invertMatch).negate();
		parseContext.endParse(this , parsed, parseContext, tokenKind, invertMatch);
		return parsed;
	}

}