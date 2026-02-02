package org.unlaxer.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.unlaxer.Token;
import org.unlaxer.parser.Parser;

/**
 * Normalizes "choiced operator" wrapper nodes to a real operator token.
 *
 * Targets:
 * - ChoicedOperatorRoot
 * - ChoicedOperator
 *
 * Rule:
 * - If exactly one real operator (ASTNodeKind.Operator) exists in the subtree, unwrap to it.
 * - Otherwise do not map (return original).
 */
public final class NormalizeChoicedOperatorMapper implements ASTMapper {

  @Override
  public boolean canASTMapping(Token parsedToken) {
    Parser p = parsedToken.getParser();
    return p.hasTag(ASTNodeKind.ChoicedOperatorRoot.tag()) || p.astNodeKind() == ASTNodeKind.ChoicedOperatorRoot
        || p.hasTag(ASTNodeKind.ChoicedOperator.tag()) || p.astNodeKind() == ASTNodeKind.ChoicedOperator;
  }

  @Override
  public Token toAST(ASTMapperContext context, Token parsedToken) {
    Objects.requireNonNull(context, "context");
    Objects.requireNonNull(parsedToken, "parsedToken");

    List<Token> found = new ArrayList<>();
    collectRealOperators(parsedToken, found);

    if (found.size() == 1) {
      return context.toAST(found.get(0));
    }
    return parsedToken;
  }

  private static void collectRealOperators(Token node, List<Token> out) {
    if (isRealOperator(node)) {
      out.add(node);
      return;
    }
    for (Token child : node.getAstNodeChildren()) {
      collectRealOperators(child, out);
    }
  }


  private static boolean isRealOperator(Token t) {
    Parser p = t.getParser();
    return p.hasTag(ASTNodeKind.Operator.tag()) || p.astNodeKind() == ASTNodeKind.Operator
        || p.hasTag(ASTNodeKind.ChoicedOperator.tag()) || p.astNodeKind() == ASTNodeKind.ChoicedOperator;
  }

}
