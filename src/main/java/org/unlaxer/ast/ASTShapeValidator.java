package org.unlaxer.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.unlaxer.Token;
import org.unlaxer.TokenList;
import org.unlaxer.parser.Parser;

/**
 * Validates expected AST shapes after mapping.
 * This is intentionally "use-case specific": provide dedicated validators per AST usage.
 */
public final class ASTShapeValidator {

  public static final class Violation {
    public final String path;
    public final ASTNodeKind kind;
    public final int childCount;
    public final String message;

    public Violation(String path, ASTNodeKind kind, int childCount, String message) {
      this.path = path;
      this.kind = kind;
      this.childCount = childCount;
      this.message = message;
    }

    @Override
    public String toString() {
      return path + " kind=" + kind + " children=" + childCount + " : " + message;
    }
  }

  private ASTShapeValidator() {}

  /**
   * Validate a binary-expression AST:
   * - Operand / ChoicedOperand: must be leaf (0 children)
   * - Operator / ChoicedOperator: must have exactly 2 children and recurse
   * - Choice roots should not remain (should have been normalized away)
   */
  public static List<Violation> validateBinaryExpressionAST(Token root) {
    Objects.requireNonNull(root, "root");
    List<Violation> out = new ArrayList<>();
    validateBinaryExprNode(root, "$", out);
    return out;
  }

  private static void validateBinaryExprNode(Token node, String path, List<Violation> out) {
    ASTNodeKind kind = kindOf(node);
    TokenList ch = node.getAstNodeChildren();
    int n = ch.size();

    // forbidden wrappers (normalize漏れ検知)
    if (kind == ASTNodeKind.ChoicedOperandRoot || kind == ASTNodeKind.ChoicedOperatorRoot) {
      out.add(new Violation(path, kind, n, "choice root should be normalized away"));
      // keep validating children for more diagnostics
    }

    // operand
    if (kind == ASTNodeKind.Operand || kind == ASTNodeKind.ChoicedOperand) {
      if (n != 0) {
        out.add(new Violation(path, kind, n, "operand should be a leaf"));
      }
      return;
    }

    // operator
    if (kind == ASTNodeKind.Operator || kind == ASTNodeKind.ChoicedOperator) {
      if (n != 2) {
        out.add(new Violation(path, kind, n, "operator must have exactly 2 children"));
        for (int i = 0; i < n; i++) {
          validateBinaryExprNode(ch.get(i), path + "[" + i + "]", out);
        }
        return;
      }
      validateBinaryExprNode(ch.get(0), path + ".L", out);
      validateBinaryExprNode(ch.get(1), path + ".R", out);
      return;
    }

    // unexpected
    out.add(new Violation(path, kind, n, "unexpected node kind for binary-expression AST"));
    for (int i = 0; i < n; i++) {
      validateBinaryExprNode(ch.get(i), path + "[" + i + "]", out);
    }
  }

  private static ASTNodeKind kindOf(Token t) {
    Parser p = t.getParser();
    ASTNodeKind k = p.astNodeKind();
    return k != null ? k : ASTNodeKind.Other;
  }
}