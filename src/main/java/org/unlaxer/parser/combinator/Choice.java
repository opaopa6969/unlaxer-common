package org.unlaxer.parser.combinator;

import java.util.List;

import org.unlaxer.Name;
import org.unlaxer.Parsed;
import org.unlaxer.TokenKind;
import org.unlaxer.ast.ASTNodeKind;
import org.unlaxer.context.ParseContext;
import org.unlaxer.parser.HasChildrenParser;
import org.unlaxer.parser.Parser;

public class Choice extends ConstructedCombinatorParser implements ChoiceInterface{

	private static final long serialVersionUID = 1464495138641251351L;

	public Choice(Name name, List<Parser> parsers) {
		super(name, parsers);
	}

	public Choice(List<Parser> parsers) {
		super(parsers);
	}

	@SafeVarargs
	public Choice(Name name, Parser... parsers) {
		super(name, parsers);
	}

	@SafeVarargs
	public Choice(Parser... parsers) {
		super(parsers);
		setASTNodeKind(ASTNodeKind.ChoicedOperator);
	}
	
	public Choice(Name name, ASTNodeKind astNodeKind ,  List<Parser> parsers) {
		super(name, parsers);
		addTag(astNodeKind.tag());
	}

	public Choice(ASTNodeKind astNodeKind ,  List<Parser> parsers) {
		super(parsers);
		addTag(astNodeKind.tag());
	}

	@SafeVarargs
	public Choice(Name name, ASTNodeKind astNodeKind ,  Parser... parsers) {
		super(name, parsers);
		addTag(astNodeKind.tag());
	}

	@SafeVarargs
	public Choice(ASTNodeKind astNodeKind ,  Parser... parsers) {
		super(parsers);
		addTag(astNodeKind.tag());
	}


	@Override
	public Parsed parse(ParseContext parseContext, TokenKind tokenKind, boolean invertMatch) {
		return ChoiceInterface.super.parse(parseContext, tokenKind, invertMatch);
	}
	
//	public Choice newWithout(Predicate<Parser> cutFilter){
//		
//		Predicate<Parser> passFilter = cutFilter.negate();
//		List<Parser> newChildren = getChildren().stream()
//			.filter(passFilter)
//			.collect(Collectors.toList());
//		
//		return new Choice(getName() , newChildren);
//	}

	@Override
	public HasChildrenParser createWith(List<Parser> children) {
		return new Choice(getName() , children);
	}
}
