package org.unlaxer.parser.combinator;

import org.unlaxer.Name;
import org.unlaxer.ast.ASTMapper.ASTNodeKind;
import org.unlaxer.parser.Parser;

public class ZeroOrMore extends ChildOccursWithTerminator {

	private static final long serialVersionUID = 4026350324813186034L;

	public ZeroOrMore(Parser inner) {
		super(inner);
	}
	
	public ZeroOrMore(Name name , Parser inner) {
		super(name , inner);
	}

	
	private ZeroOrMore(Name name , Parser inner,Parser terminator) {
		super(name , inner,terminator);
	}
	
	public ZeroOrMore(ASTNodeKind astNodeKind ,  Parser inner) {
		super(inner);
		addTag(astNodeKind.tag());
	}
	
	public ZeroOrMore(Name name ,ASTNodeKind astNodeKind  ,  Parser inner) {
		super(name , inner);
		addTag(astNodeKind.tag());
	}

	
	private ZeroOrMore(Name name , ASTNodeKind astNodeKind , Parser inner,Parser terminator) {
		super(name , inner,terminator);
		addTag(astNodeKind.tag());
	}


	public ZeroOrMore newWithTerminator(Parser terminator){
		return new ZeroOrMore(getName() , getChild(), terminator);
	}

	@Override
	public int min() {
		return 0;
	}

	@Override
	public int max() {
		return Integer.MAX_VALUE;
	}

}
