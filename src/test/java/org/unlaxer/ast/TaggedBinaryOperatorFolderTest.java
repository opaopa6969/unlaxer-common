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

public class TaggedBinaryOperatorFolderTest {

  static class DummyParser extends AbstractParser {
    private static final long serialVersionUID = 1L;

    DummyParser(ASTNodeKind kind) {
      setASTNodeKind(kind);
    }

    @Override
    public Parser createParser() {
      return this;
    }

    @Override
    public void prepareChildren(Parsers childrenContainer) {
      // no children
    }

    @Override
    public ChildOccurs getChildOccurs() {
      return ChildOccurs.none;
    }
  }

  private static Token tok(String s, Parser p) {
    return new Token(
      TokenKind.consumed,
      StringSource.create(s, SourceKind.root),
      p
    );
  }

  @Test
  public void foldsLeftAssociative_whenTagged() {
    Parser rootP = new DummyParser(ASTNodeKind.ZeroOrMoreOperatorOperandSuccessor);
    Parser operandP = new DummyParser(ASTNodeKind.Operand);
    Parser plusP = new DummyParser(ASTNodeKind.Operator);
    Parser minusP = new DummyParser(ASTNodeKind.Operator);

    // 1 + 2 - 3
    Token one = tok("1", operandP);
    Token plus = tok("+", plusP);
    Token two = tok("2", operandP);
    Token minus = tok("-", minusP);
    Token three = tok("3", operandP);

    Token root = new Token(
      TokenKind.consumed,
      new TokenList(one, plus, two, minus, three),
      rootP
    );

    ASTMapperContext ctx = ASTMapperContext.create(
      new TaggedBinaryOperatorFolder(ASTNodeKind.ZeroOrMoreOperatorOperandSuccessor)
    );

    Token ast = ctx.toAST(root);

    assertNotSame(root, ast);
    assertEquals("-", ast.getSource().toString());

    assertEquals(2, ast.getAstNodeChildren().size());
    Token left = ast.getAstNodeChildren().get(0);
    Token right = ast.getAstNodeChildren().get(1);

    assertEquals("+", left.getSource().toString());
    assertEquals("3", right.getSource().toString());

    assertEquals(2, left.getAstNodeChildren().size());
    assertEquals("1", left.getAstNodeChildren().get(0).getSource().toString());
    assertEquals("2", left.getAstNodeChildren().get(1).getSource().toString());
  }

  @Test
  public void returnsSameToken_whenRootNotTagged() {
    Parser rootP = new DummyParser(ASTNodeKind.Other);
    Parser operandP = new DummyParser(ASTNodeKind.Operand);
    Parser opP = new DummyParser(ASTNodeKind.Operator);

    Token root = new Token(
      TokenKind.consumed,
      new TokenList(tok("1", operandP), tok("+", opP), tok("2", operandP)),
      rootP
    );

    ASTMapperContext ctx = ASTMapperContext.create(
      new TaggedBinaryOperatorFolder(ASTNodeKind.ZeroOrMoreOperatorOperandSuccessor)
    );

    Token ast = ctx.toAST(root);
    assertSame(root, ast);
  }
}
