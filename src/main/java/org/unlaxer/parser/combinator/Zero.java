package org.unlaxer.parser.combinator;

import org.unlaxer.Name;
import org.unlaxer.ast.ASTNodeKind;
import org.unlaxer.parser.Parser;

public class Zero extends ChildOccursWithTerminator {

	private static final long serialVersionUID = -9115349154403135594L;

	public Zero(Parser inner) {
		super(inner);
	}
	
	public Zero(Name name , Parser inner) {
		super(name , inner);
	}

	
	private Zero(Name name , Parser inner,Parser terminator) {
		super(name , inner,terminator);
	}
	
	public Zero(ASTNodeKind astNodeKind ,  Parser inner) {
		super(inner);
		addTag(astNodeKind.tag());
	}
	
	public Zero(Name name , ASTNodeKind astNodeKind ,  Parser inner) {
		super(name , inner);
		addTag(astNodeKind.tag());
	}

	
	private Zero(Name name , ASTNodeKind astNodeKind ,  Parser inner,Parser terminator) {
		super(name , inner,terminator);
		addTag(astNodeKind.tag());
	}

	
	public Zero newWithTerminator(Parser terminator){
		return new Zero(getName() , getChild(), terminator);
	}

	@Override
	public int min() {
		return 0;
	}

	@Override
	public int max() {
		return 0;
	}
}