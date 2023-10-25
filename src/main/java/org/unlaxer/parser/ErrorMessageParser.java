package org.unlaxer.parser;

import org.unlaxer.ErrorMessage;
import org.unlaxer.Name;
import org.unlaxer.Range;
import org.unlaxer.RangedContent;
import org.unlaxer.parser.combinator.ContainerParser;

public class ErrorMessageParser extends ContainerParser<String>{
	
	private static final long serialVersionUID = -1442039244922724686L;
	
	String message;
	

	public ErrorMessageParser(String message) {
		super();
		this.message = message;
	}

	public ErrorMessageParser(String message , Parsers children) {
		this.message = message;
	}

	public ErrorMessageParser(String message , Name name) {
		super(name);
		this.message = message;
	}

	@Override
	public String get() {
		return message;
	}
	
	@Override
	public RangedContent<String> get(Range position) {
		return new ErrorMessage(position, message);
	}

	@Override
	public Parser createParser() {
		return this;
	}
}