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

public class TaggedBinaryOperatorFolderOneOrMoreTest {

  static class DummyParser extends AbstractParser {
    private static final long serialVersionUID = 1L;

    DummyParser(ASTNodeKind kind) { setASTNodeKind(kind); }

    @Override public Parser createParser() { return this; }
    @Override public void prepareChildren(Parsers childrenContainer) { }
    @Override public ChildOccurs getChildOccurs() { return ChildOccurs.none; }
  }

  private static Token tok(String s, Parser p) {
    return new Token(TokenKind.consumed, StringSource.create(s, SourceKind.root), p);
  }

  @Test
  public void oneOrMore_returnsSame_whenOnlyOneOperand() {
    Parser rootP = new DummyParser(ASTNodeKind.OneOrMoreOperatorOperandSuccessor);
    Parser operandP = new DummyParser(ASTNodeKind.Operand);

    Token root = new Token(TokenKind.consumed, new TokenList(tok("1", operandP)), rootP);

    ASTMapperContext ctx = ASTMapperContext.create(
        new TaggedBinaryOperatorFolder(ASTNodeKind.OneOrMoreOperatorOperandSuccessor)
    );

    Token ast = ctx.toAST(root);
    assertSame(root, ast);
  }

  @Test
  public void oneOrMore_folds_whenHasAtLeastOneOperatorOperandPair() {
    Parser rootP = new DummyParser(ASTNodeKind.OneOrMoreOperatorOperandSuccessor);
    Parser operandP = new DummyParser(ASTNodeKind.Operand);
    Parser opP = new DummyParser(ASTNodeKind.Operator);

    Token one = tok("1", operandP);
    Token plus = tok("+", opP);
    Token two = tok("2", operandP);

    Token root = new Token(TokenKind.consumed, new TokenList(one, plus, two), rootP);

    ASTMapperContext ctx = ASTMapperContext.create(
        new TaggedBinaryOperatorFolder(ASTNodeKind.OneOrMoreOperatorOperandSuccessor)
    );

    Token ast = ctx.toAST(root);

    assertNotSame(root, ast);
    assertEquals("+", ast.getSource().toString());
    assertEquals(2, ast.getAstNodeChildren().size());
    assertEquals("1", ast.getAstNodeChildren().get(0).getSource().toString());
    assertEquals("2", ast.getAstNodeChildren().get(1).getSource().toString());
  }
}
