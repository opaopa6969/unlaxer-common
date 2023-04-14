package org.unlaxer.parser.combinator;

import org.unlaxer.Name;
import org.unlaxer.ast.ASTMapper.ASTNodeKind;
import org.unlaxer.parser.Parser;

public class ZeroOrOne extends Optional{

	private static final long serialVersionUID = 7544142678234641766L;

	public ZeroOrOne(Name name, Parser inner) {
		super(name, inner);
	}

	public ZeroOrOne(Parser inner) {
		super(inner);
	}
	
	public ZeroOrOne(Name name, ASTNodeKind astNodeKind ,  Parser inner) {
		super(name, inner);
		addTag(astNodeKind.tag());
	}

	public ZeroOrOne(ASTNodeKind astNodeKind , Parser inner) {
		super(inner);
		addTag(astNodeKind.tag());
	}

}