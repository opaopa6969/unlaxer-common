package org.unlaxer.ast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.unlaxer.Source.SourceKind;
import org.unlaxer.StringSource;
import org.unlaxer.Token;
import org.unlaxer.TokenKind;
import org.unlaxer.TokenList;
import org.unlaxer.parser.AbstractParser;
import org.unlaxer.parser.ChildOccurs;
import org.unlaxer.parser.Parser;
import org.unlaxer.parser.Parsers;

public class ASTShapeValidatorTest {

  static class DummyParser extends AbstractParser {
    private static final long serialVersionUID = 1L;

    DummyParser(ASTNodeKind kind) {
      setASTNodeKind(kind);
    }

    @Override public Parser createParser() { return this; }
    @Override public void prepareChildren(Parsers c) {}
    @Override public ChildOccurs getChildOccurs() { return ChildOccurs.none; }
  }

  private static Token leaf(String s, ASTNodeKind kind) {
    return new Token(TokenKind.consumed, StringSource.create(s, SourceKind.root), new DummyParser(kind));
  }

  @Test
  public void validateBinaryExpression_ok() {
    // ((1+2)-3)
    Token one = leaf("1", ASTNodeKind.Operand);
    Token two = leaf("2", ASTNodeKind.Operand);
    Token three = leaf("3", ASTNodeKind.Operand);

    Token plus = new Token(
        TokenKind.consumed,
        StringSource.create("+", SourceKind.root),
        new DummyParser(ASTNodeKind.Operator),
        new TokenList(one, two)
    );

    Token minus = new Token(
        TokenKind.consumed,
        StringSource.create("-", SourceKind.root),
        new DummyParser(ASTNodeKind.Operator),
        new TokenList(plus, three)
    );

    assertTrue(ASTShapeValidator.validateBinaryExpressionAST(minus).isEmpty());
  }

  @Test
  public void validateBinaryExpression_detectsChoiceRootRemain() {
    Token one = leaf("1", ASTNodeKind.Operand);
    Token two = leaf("2", ASTNodeKind.Operand);

    Token plus = new Token(
        TokenKind.consumed,
        StringSource.create("+", SourceKind.root),
        new DummyParser(ASTNodeKind.Operator),
        new TokenList(one, two)
    );

    Token wrapped = new Token(
        TokenKind.consumed,
        new TokenList(plus),
        new DummyParser(ASTNodeKind.ChoicedOperatorRoot)
    );

    var violations = ASTShapeValidator.validateBinaryExpressionAST(wrapped);

    // 重要なのは「choice root が残ってる」を検知すること
    assertTrue(violations.stream().anyMatch(v ->
        v.kind == ASTNodeKind.ChoicedOperatorRoot &&
        v.message.contains("choice root should be normalized away")
    ));
  }

}
