package org.unlaxer.ast;

import java.util.EnumSet;
import java.util.Objects;
import java.util.function.BiPredicate;

import org.unlaxer.Token;
import org.unlaxer.TokenList;
import org.unlaxer.parser.Parser;

/**
 * Generic left-associative folder for tokens shaped as:
 *   operand (operator operand)*
 *
 * - Applies only when the root token parser matches one of rootKinds.
 * - Operands/operators are recognized by provided predicates.
 * - Each child token is normalized via context.toAST(...) before checking predicates.
 *
 * OneOrMore behavior:
 * - If matched root kind is oneOrMoreKind, at least one fold must happen.
 */
public final class TaggedInfixFolder implements ASTMapper {

  private final EnumSet<ASTNodeKind> rootKinds;
  private final ASTNodeKind oneOrMoreKind; // may be null
  private final BiPredicate<ASTMapperContext, Token> isOperand;
  private final BiPredicate<ASTMapperContext, Token> isOperator;

  public TaggedInfixFolder(
      EnumSet<ASTNodeKind> rootKinds,
      ASTNodeKind oneOrMoreKind,
      BiPredicate<ASTMapperContext, Token> isOperand,
      BiPredicate<ASTMapperContext, Token> isOperator
  ) {
    this.rootKinds = EnumSet.copyOf(Objects.requireNonNull(rootKinds, "rootKinds"));
    if (this.rootKinds.isEmpty()) throw new IllegalArgumentException("rootKinds must not be empty");
    this.oneOrMoreKind = oneOrMoreKind; // null allowed
    this.isOperand = Objects.requireNonNull(isOperand, "isOperand");
    this.isOperator = Objects.requireNonNull(isOperator, "isOperator");
  }

  public static TaggedInfixFolder nonChoiced(ASTNodeKind... kinds) {
    EnumSet<ASTNodeKind> s = enumSetOf(kinds);
    return new TaggedInfixFolder(
        s,
        s.contains(ASTNodeKind.OneOrMoreOperatorOperandSuccessor) ? ASTNodeKind.OneOrMoreOperatorOperandSuccessor : null,
        (ctx, t) -> hasKind(t, ASTNodeKind.Operand),
        (ctx, t) -> hasKind(t, ASTNodeKind.Operator)
    );
  }

  public static TaggedInfixFolder choiced(ASTNodeKind... kinds) {
    EnumSet<ASTNodeKind> s = enumSetOf(kinds);
    return new TaggedInfixFolder(
        s,
        s.contains(ASTNodeKind.OneOrMoreChoicedOperatorOperandSuccessor) ? ASTNodeKind.OneOrMoreChoicedOperatorOperandSuccessor : null,
        (ctx, t) -> hasKind(t, ASTNodeKind.Operand) || hasKind(t, ASTNodeKind.ChoicedOperand),
        (ctx, t) -> hasKind(t, ASTNodeKind.Operator) || hasKind(t, ASTNodeKind.ChoicedOperator)
    );
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
    if (!isOperand.test(context, first)) return parsedToken;

    Token acc = first;
    int foldedCount = 0;

    for (int i = 1; i < nodes.size(); i += 2) {
      if (i + 1 >= nodes.size()) return parsedToken;

      Token op = context.toAST(nodes.get(i));
      Token right = context.toAST(nodes.get(i + 1));

      if (!isOperator.test(context, op) || !isOperand.test(context, right)) return parsedToken;

      acc = new Token(
          op.tokenKind,
          op.source,
          op.parser,
          new TokenList(acc, right)
      ).setParent(op.parent);

      foldedCount++;
    }

    if (oneOrMoreKind != null && matchedRoot == oneOrMoreKind && foldedCount == 0) {
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

  private static boolean hasKind(Token t, ASTNodeKind kind) {
    Parser p = t.getParser();
    return p.hasTag(kind.tag()) || p.astNodeKind() == kind;
  }

  private static EnumSet<ASTNodeKind> enumSetOf(ASTNodeKind... kinds) {
    Objects.requireNonNull(kinds, "kinds");
    if (kinds.length == 0) throw new IllegalArgumentException("kinds must not be empty");

    EnumSet<ASTNodeKind> s = EnumSet.noneOf(ASTNodeKind.class);
    for (ASTNodeKind k : kinds) if (k != null) s.add(k);
    if (s.isEmpty()) throw new IllegalArgumentException("kinds must not be empty");
    return s;
  }
}
