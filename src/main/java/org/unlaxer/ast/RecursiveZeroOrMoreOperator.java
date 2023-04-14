package org.unlaxer.ast;

import java.util.Iterator;
import java.util.List;

import org.unlaxer.Token;

public interface RecursiveZeroOrMoreOperator extends ASTMapper{

	/* fit to following
	new Parsers(
		Parser.get(StringFactorParser.class),
		new ZeroOrMore(
			Parser.get(SliceParser.class)
		)
	);
	*/
	@Override
	default Token toAST(ASTMapperContext context , Token parsedToken) {
		
		List<Token> tokens = parsedToken.getAstNodeChildren();
		Iterator<Token> iterator = tokens.iterator();
		
		// get operand
		Token lastOpearatorAndOperands = context.toAST(iterator.next());;
		
		while(iterator.hasNext()){
			Token operator = iterator.next();
			lastOpearatorAndOperands = 
//				operator.newCreatesOf(operator , lastOpearatorAndOperands);
				operator.newCreatesOf(lastOpearatorAndOperands);
		}
		return lastOpearatorAndOperands;
	}
}