package org.unlaxer.parser.combinator;

import org.unlaxer.Name;
import org.unlaxer.ast.ASTNodeKind;
import org.unlaxer.parser.Parser;

public class OneOrMore extends ChildOccursWithTerminator {

	private static final long serialVersionUID = 3883160475654738794L;

	public OneOrMore(Parser inner) {
		super(inner);
	}
	
	public  OneOrMore(Name name, Parser inner, Parser terminator) {
		super(name, inner, terminator);
	}

	public OneOrMore(Name name, Parser inner) {
		super(name, inner);
	}
	
	public OneOrMore(ASTNodeKind astNodeKind ,  Parser inner) {
		super(inner);
		addTag(astNodeKind.tag());
	}
	
	public  OneOrMore(Name name, ASTNodeKind astNodeKind ,  Parser inner, Parser terminator) {
		super(name, inner, terminator);
		addTag(astNodeKind.tag());
	}

	public OneOrMore(Name name, ASTNodeKind astNodeKind ,  Parser inner) {
		super(name, inner);
		addTag(astNodeKind.tag());
	}


	public OneOrMore newWithTerminator(Parser terminator){
		return new OneOrMore(getName() , getChild(),terminator);
	}
	@Override
	public int min() {
		return 1;
	}

	@Override
	public int max() {
		return Integer.MAX_VALUE;
	}

}
