package org.unlaxer.ast;

import java.util.Objects;

import org.unlaxer.Token;
import org.unlaxer.TokenList;
import org.unlaxer.parser.Parser;

/**
 * Normalizes "choiced operand" wrapper nodes to a real operand token.
 *
 * Expected shapes:
 * - ChoicedOperandRoot(...) containing exactly one operand-like token in its subtree
 * - ChoicedOperand(...) containing exactly one operand-like token in its subtree
 *
 * If the mapper cannot safely decide, it returns the original token (no mapping).
 */
public final class NormalizeChoicedOperandMapper implements ASTMapper {

  @Override
  public boolean canASTMapping(Token parsedToken) {
    Parser p = parsedToken.getParser();
    return p.hasTag(ASTNodeKind.ChoicedOperandRoot.tag()) || p.astNodeKind() == ASTNodeKind.ChoicedOperandRoot
        || p.hasTag(ASTNodeKind.ChoicedOperand.tag()) || p.astNodeKind() == ASTNodeKind.ChoicedOperand;
  }

  @Override
  public Token toAST(ASTMapperContext context, Token parsedToken) {
    Objects.requireNonNull(context, "context");
    Objects.requireNonNull(parsedToken, "parsedToken");

    // Find operand candidates in subtree (including self's children)
    TokenList found = new TokenList();
    collectOperands(parsedToken, found);

    // If there is exactly one operand candidate, unwrap to it.
    if (found.size() == 1) {
      return context.toAST(found.get(0));
    }

    // Otherwise do not map (ambiguous or not found)
    return parsedToken;
  }

  private static void collectOperands(Token node, TokenList out) {
    for (Token child : node.getAstNodeChildren()) {
      if (isRealOperand(child)) {
        out.add(child);
      } else {
        collectOperands(child, out);
      }
    }
  }

  private static boolean isRealOperand(Token t) {
    Parser p = t.getParser();
    return p.hasTag(ASTNodeKind.Operand.tag()) || p.astNodeKind() == ASTNodeKind.Operand
        || p.hasTag(ASTNodeKind.ChoicedOperand.tag()) || p.astNodeKind() == ASTNodeKind.ChoicedOperand;
  }

}
