package org.unlaxer.ast;

import java.util.EnumSet;
import java.util.Objects;

import org.unlaxer.Token;
import org.unlaxer.TokenList;
import org.unlaxer.parser.Parser;

/**
 * Folds tokens shaped as: operand (operator operand)*
 *
 * Root token is mapped only when its parser is tagged with one of {@code rootKinds}.
 *
 * OneOrMore behavior:
 * - If the matched root kind is OneOrMoreOperatorOperandSuccessor, at least one fold must happen.
 * ZeroOrMore behavior:
 * - If the matched root kind is ZeroOrMoreOperatorOperandSuccessor, fold may be zero times (operand only).
 */
public final class TaggedBinaryOperatorFolder implements ASTMapper {

  private final EnumSet<ASTNodeKind> rootKinds;

  public TaggedBinaryOperatorFolder(EnumSet<ASTNodeKind> rootKinds) {
    this.rootKinds = EnumSet.copyOf(Objects.requireNonNull(rootKinds, "rootKinds"));
    if (this.rootKinds.isEmpty()) {
      throw new IllegalArgumentException("rootKinds must not be empty");
    }
  }

  public TaggedBinaryOperatorFolder(ASTNodeKind... kinds) {
    Objects.requireNonNull(kinds, "kinds");
    if (kinds.length == 0) {
      throw new IllegalArgumentException("kinds must not be empty");
    }
    EnumSet<ASTNodeKind> s = EnumSet.noneOf(ASTNodeKind.class);
    for (ASTNodeKind k : kinds) {
      if (k != null) s.add(k);
    }
    if (s.isEmpty()) throw new IllegalArgumentException("kinds must not be empty");
    this.rootKinds = s;
  }

  @Override
  public boolean canASTMapping(Token parsedToken) {
    Parser p = parsedToken.getParser();
    for (ASTNodeKind k : rootKinds) {
      if (p.hasTag(k.tag()) || p.astNodeKind() == k) return true;
    }
    return false;
  }

  @Override
  public Token toAST(ASTMapperContext context, Token parsedToken) {
    Objects.requireNonNull(context, "context");
    Objects.requireNonNull(parsedToken, "parsedToken");

    ASTNodeKind matchedRoot = matchedRootKind(parsedToken);
    if (matchedRoot == null) return parsedToken;

    TokenList nodes = parsedToken.getAstNodeChildren();
    if (nodes.isEmpty()) return parsedToken;

    Token first = context.toAST(nodes.get(0));
    if (!isOperand(first)) return parsedToken;

    Token acc = first;
    int foldedCount = 0;

    for (int i = 1; i < nodes.size(); i += 2) {
      if (i + 1 >= nodes.size()) return parsedToken;

      Token op = context.toAST(nodes.get(i));
      Token right = context.toAST(nodes.get(i + 1));

      if (!isOperator(op) || !isOperand(right)) return parsedToken;

      acc = new Token(
          op.tokenKind,
          op.source,
          op.parser,
          new TokenList(acc, right)
      ).setParent(op.parent);

      foldedCount++;
    }

    if (matchedRoot == ASTNodeKind.OneOrMoreOperatorOperandSuccessor && foldedCount == 0) {
      return parsedToken;
    }

    return acc;
  }

  private ASTNodeKind matchedRootKind(Token t) {
    Parser p = t.getParser();
    for (ASTNodeKind k : rootKinds) {
      if (p.hasTag(k.tag()) || p.astNodeKind() == k) return k;
    }
    return null;
  }

  private static boolean isOperand(Token t) {
    Parser p = t.getParser();
    return p.hasTag(ASTNodeKind.Operand.tag()) || p.astNodeKind() == ASTNodeKind.Operand;
  }

  private static boolean isOperator(Token t) {
    Parser p = t.getParser();
    return p.hasTag(ASTNodeKind.Operator.tag()) || p.astNodeKind() == ASTNodeKind.Operator;
  }
}
