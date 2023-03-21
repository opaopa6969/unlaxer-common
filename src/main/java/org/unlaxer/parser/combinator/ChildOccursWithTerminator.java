package org.unlaxer.parser.combinator;

import java.util.Optional;

import org.unlaxer.Name;
import org.unlaxer.parser.ChildOccurs;
import org.unlaxer.parser.MetaFunctionParser;
import org.unlaxer.parser.NonTerminallSymbol;
import org.unlaxer.parser.Parser;
import org.unlaxer.parser.Parsers;


public abstract class ChildOccursWithTerminator extends ConstructedOccurs 
	implements MetaFunctionParser , NonTerminallSymbol{

	private static final long serialVersionUID = -4411440278839259161L;

	
	Optional<Parser> terminator;

	public ChildOccursWithTerminator(Parser inner) {
		this(inner,null);
	}
	ChildOccursWithTerminator(Parser inner,Parser terminator) {
		this(null,inner,terminator);
	}
	
	public ChildOccursWithTerminator(Name name , Parser inner) {
		this(name , inner, null);
	}
	
	ChildOccursWithTerminator(Name name , Parser inner,Parser terminator) {
		super(name , terminator == null ? 
				new Parsers(inner):
				new Parsers(inner,terminator));
		this.terminator = Optional.ofNullable(terminator);
	}
	
	@Override
	public Parser createParser() {
		return this;
	}
	
	@Override
	public ChildOccurs getChildOccurs() {
		return ChildOccurs.multi;
	}
	
	@Override
	public Optional<Parser> getTerminator(){
		return terminator;
				
	}
}
