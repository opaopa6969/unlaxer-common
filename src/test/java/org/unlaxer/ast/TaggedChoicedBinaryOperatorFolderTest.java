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

public class TaggedChoicedBinaryOperatorFolderTest {

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
  public void foldsLeftAssociative_whenOperatorIsWrappedByChoiceNode() {
    Parser rootP = new DummyParser(ASTNodeKind.ZeroOrMoreChoicedOperatorOperandSuccessor);
    Parser operandP = new DummyParser(ASTNodeKind.Operand);
    Parser opP = new DummyParser(ASTNodeKind.Operator);
    Parser choiceOpRootP = new DummyParser(ASTNodeKind.ChoicedOperatorRoot);

    // operator tokens
    Token plusOp = tok("+", opP);
    Token minusOp = tok("-", opP);

    // choice wrappers (each has exactly one child operator token)
    Token plusChoice = new Token(TokenKind.consumed, new TokenList(plusOp), choiceOpRootP);
    Token minusChoice = new Token(TokenKind.consumed, new TokenList(minusOp), choiceOpRootP);

    // 1 (+) 2 (-) 3
    Token one = tok("1", operandP);
    Token two = tok("2", operandP);
    Token three = tok("3", operandP);

    Token root = new Token(
        TokenKind.consumed,
        new TokenList(one, plusChoice, two, minusChoice, three),
        rootP
    );

    ASTMapperContext ctx = ASTMapperContext.create(
        new NormalizeChoicedOperandMapper(),
        new NormalizeChoicedOperatorMapper(),
        new TaggedChoicedBinaryOperatorFolder(ASTNodeKind.ZeroOrMoreChoicedOperatorOperandSuccessor)
    );

    Token ast = ctx.toAST(root);

    assertNotSame(root, ast);
    assertEquals("-", ast.getSource().toString());

    Token left = ast.getAstNodeChildren().get(0);
    Token right = ast.getAstNodeChildren().get(1);

    assertEquals("+", left.getSource().toString());
    assertEquals("3", right.getSource().toString());

    assertEquals("1", left.getAstNodeChildren().get(0).getSource().toString());
    assertEquals("2", left.getAstNodeChildren().get(1).getSource().toString());
  }

  @Test
  public void returnsSameToken_whenOperatorChoiceWrapperHasNoOperatorInside() {
    Parser rootP = new DummyParser(ASTNodeKind.ZeroOrMoreChoicedOperatorOperandSuccessor);
    Parser operandP = new DummyParser(ASTNodeKind.Operand);
    Parser choiceOpRootP = new DummyParser(ASTNodeKind.ChoicedOperatorRoot);

    Token badChoice = new Token(
        TokenKind.consumed,
        new TokenList(tok("?", new DummyParser(ASTNodeKind.Other))),
        choiceOpRootP
    );

    Token root = new Token(
        TokenKind.consumed,
        new TokenList(tok("1", operandP), badChoice, tok("2", operandP)),
        rootP
    );

    ASTMapperContext ctx = ASTMapperContext.create(
        new TaggedChoicedBinaryOperatorFolder(ASTNodeKind.ZeroOrMoreChoicedOperatorOperandSuccessor)
    );

    Token ast = ctx.toAST(root);
    assertSame(root, ast);
  }
}
