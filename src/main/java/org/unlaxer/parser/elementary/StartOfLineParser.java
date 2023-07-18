package org.unlaxer.parser.elementary;

import java.util.List;

import org.unlaxer.Parsed;
import org.unlaxer.RangedString;
import org.unlaxer.TokenKind;
import org.unlaxer.Parsed.Status;
import org.unlaxer.context.ParseContext;
import org.unlaxer.parser.AbstractParser;
import org.unlaxer.parser.ChildOccurs;
import org.unlaxer.parser.Parser;

public class StartOfLineParser extends AbstractParser{
	
	static final String LF = new String(new byte[] {0x0a/*lf*/});
	static final String CR = new String(new byte[] {0x0d/*cr*/});
	static final String CRLF = new String(new byte[] {0x0d/*cr*/,0x0a/*lf*/});

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
		if(position == 0) {
			boolean match = (position == 0) ^ invertMatch;
			return new Parsed(match ? Status.succeeded : Status.failed);
		}
		if(position >0) {
			RangedString peekLast = parseContext.source.peekLast(position, 1);
			String string = peekLast.token.get();
			if(string.equals(CR) || string.equals(LF)) {
				boolean match = true ^ invertMatch;
				return new Parsed(match ? Status.succeeded : Status.failed);
			}
		}
		return new Parsed(Status.failed);
	}

}