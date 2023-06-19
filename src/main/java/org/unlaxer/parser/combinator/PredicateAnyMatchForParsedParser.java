package org.unlaxer.parser.combinator;

import java.util.List;
import java.util.function.Predicate;

import org.unlaxer.Name;
import org.unlaxer.Parsed;
import org.unlaxer.Token;
import org.unlaxer.TokenKind;
import org.unlaxer.Parsed.Status;
import org.unlaxer.context.ParseContext;
import org.unlaxer.parser.Parser;

public class PredicateAnyMatchForParsedParser extends ConstructedSingleChildParser {
	
	Predicate<Token> predicate;

	public PredicateAnyMatchForParsedParser(Name name, Parser child , Predicate<Token> predicate ) {
		super(name, child);
		this.predicate = predicate;
	}

	public PredicateAnyMatchForParsedParser(Parser child , Predicate<Token> predicate) {
		super(child);
		this.predicate = predicate;
	}
	
	@Override
	public Parsed parse(ParseContext parseContext, TokenKind tokenKind, boolean invertMatch) {
		
		parseContext.startParse(this, parseContext, tokenKind, invertMatch);
		Parsed parsed = getChild().parse(parseContext,tokenKind , invertMatch);
		if(parsed.status == Status.succeeded) {
//			List<Token> list = parsed.getRootToken().flatten().stream().filter(predicate).toList();
			boolean anyMatch = parsed.getRootToken().flatten().stream().anyMatch(predicate);
			if(false == anyMatch) {
				parsed = parsed.negate();
			}
		}
		parseContext.endParse(this , parsed, parseContext, tokenKind, invertMatch);
		return parsed;
	}

	
}