package org.unlaxer.ast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

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

public class NormalizeChoicedOperatorMapperTest {

  static class DummyParser extends AbstractParser {
    private static final long serialVersionUID = 1L;

    DummyParser(ASTNodeKind kind) {
      setASTNodeKind(kind);
    }

    @Override public Parser createParser() { return this; }
    @Override public void prepareChildren(Parsers childrenContainer) { }
    @Override public ChildOccurs getChildOccurs() { return ChildOccurs.none; }
  }

  private static Token tok(String s, Parser p) {
    return new Token(TokenKind.consumed, StringSource.create(s, SourceKind.root), p);
  }

  @Test
  public void unwraps_whenExactlyOneOperatorExistsInSubtree() {
    Parser wrapper = new DummyParser(ASTNodeKind.ChoicedOperatorRoot);
    Parser op = new DummyParser(ASTNodeKind.Operator);

    Token inner = tok("+", op);
    Token root = new Token(TokenKind.consumed, new TokenList(inner), wrapper);

    ASTMapperContext ctx = ASTMapperContext.create(new NormalizeChoicedOperatorMapper());

    Token ast = ctx.toAST(root);

    assertNotSame(root, ast);
    assertEquals("+", ast.getSource().toString());
  }

  @Test
  public void returnsSame_whenNoOperatorInSubtree() {
    Parser wrapper = new DummyParser(ASTNodeKind.ChoicedOperatorRoot);
    Parser other = new DummyParser(ASTNodeKind.Other);

    Token root = new Token(TokenKind.consumed, new TokenList(tok("?", other)), wrapper);

    ASTMapperContext ctx = ASTMapperContext.create(new NormalizeChoicedOperatorMapper());

    Token ast = ctx.toAST(root);

    assertSame(root, ast);
  }

  @Test
  public void returnsSame_whenMultipleOperatorsInSubtree() {
    Parser wrapper = new DummyParser(ASTNodeKind.ChoicedOperatorRoot);
    Parser op = new DummyParser(ASTNodeKind.Operator);

    Token a = tok("+", op);
    Token b = tok("-", op);

    Token root = new Token(TokenKind.consumed, new TokenList(a, b), wrapper);

    ASTMapperContext ctx = ASTMapperContext.create(new NormalizeChoicedOperatorMapper());

    Token ast = ctx.toAST(root);

    assertSame(root, ast);
  }
}
