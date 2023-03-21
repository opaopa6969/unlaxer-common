package org.unlaxer.parser;

import java.util.List;

public class ChoiceParsers extends Parsers{

	private static final long serialVersionUID = -7959572145076832575L;

	public ChoiceParsers(List<Parser> parsers) {
		super(parsers);
	}

	public ChoiceParsers(Parser... parsers) {
		super(parsers);
	}
}