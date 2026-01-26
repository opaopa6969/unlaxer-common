package org.unlaxer.ast;

import java.util.Objects;

import org.unlaxer.Token;
import org.unlaxer.TokenList;
import org.unlaxer.parser.Parser;

/**
 * Pretty-prints a binary-expression AST back to an expression string.
 *
 * Policy:
 * - Always parenthesize binary operator nodes: (L op R)
 * - Operands are printed as their original source text.
 *
 * This is "safe" (no precedence table required). The output may contain
 * more parentheses than necessary, but preserves meaning.
 */
public final class BinaryExpressionPrinter {

  private BinaryExpressionPrinter() {}

  public static String print(Token ast) {
    Objects.requireNonNull(ast, "ast");
    return printNode(ast);
  }

  private static String printNode(Token node) {
    ASTNodeKind kind = kindOf(node);

    if (kind == ASTNodeKind.Operand || kind == ASTNodeKind.ChoicedOperand) {
      return node.getSource().toString();
    }

    if (kind == ASTNodeKind.Operator || kind == ASTNodeKind.ChoicedOperator) {
      TokenList ch = node.getAstNodeChildren();
      if (ch.size() != 2) {
        throw new IllegalArgumentException("operator node must have 2 children: " + node.getSource());
      }
      String left = printNode(ch.get(0));
      String right = printNode(ch.get(1));
      String op = node.getSource().toString();
      return "(" + left + " " + op + " " + right + ")";
    }

    throw new IllegalArgumentException("unexpected node kind: " + kind + " source=" + node.getSource());
  }

  private static ASTNodeKind kindOf(Token t) {
    Parser p = t.getParser();
    ASTNodeKind k = p.astNodeKind();
    return k != null ? k : ASTNodeKind.Other;
  }
}
