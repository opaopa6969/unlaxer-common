package org.unlaxer.ast;

import java.util.Iterator;

import org.unlaxer.Token;
import org.unlaxer.TokenList;

public interface RecursiveZeroOrMoreBinaryOperator extends ASTMapper{

  /* fit to following
    new Parsers(
      Parser.get(StringTermParser.class),
      new ZeroOrMore(
        new WhiteSpaceDelimitedChain(
          Parser.get(StringPlusParser.class),
          Parser.get(StringTermParser.class)
        )
      )
    );
  */
  @Override
  default Token toAST(ASTMapperContext context , Token parsedToken) {

    TokenList originalTokens = parsedToken.getAstNodeChildren();
    Iterator<Token> iterator = originalTokens.iterator();

    Token left = context.toAST(iterator.next());

    Token last = left;

    while(iterator.hasNext()){
      Token operator = iterator.next();
      Token right = context.toAST(iterator.next());

      // Keep operator token's parser+source as the AST node, and attach (left,right) as children.
      last = new Token(
        operator.tokenKind,
        operator.source,
        operator.parser,
        new TokenList(last, right)
      ).setParent(operator.parent);
    }
    return last;
  }
}
