package org.unlaxer.ast;

import static org.junit.Assert.assertEquals;
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

/**
 * Tests for {@link RecursiveZeroOrMoreBinaryOperator}.
 *
 * This verifies the left-associative folding behavior:
 *   [opnd, op, opnd, op, opnd]  ->  (op (op opnd opnd) opnd)
 */
public class RecursiveZeroOrMoreBinaryOperatorTest {

  /** Leaf parser (operand). */
  static class OperandParser extends AbstractParser {
    private static final long serialVersionUID = 1L;

    OperandParser() {
      setASTNodeKind(ASTNodeKind.Operand);
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

  /** Operator token parser (does not need to be an ASTMapper). */
  static class OpParser extends AbstractParser {
    private static final long serialVersionUID = 1L;

    OpParser() {
      setASTNodeKind(ASTNodeKind.Operator);
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

  /**
   * Root parser that represents:
   *   operand (op operand)*
   * and maps it into a nested operator tree.
   */
  static class RootParser extends AbstractParser implements RecursiveZeroOrMoreBinaryOperator {
    private static final long serialVersionUID = 1L;

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
    return new Token(TokenKind.consumed, StringSource.create(s, SourceKind.root), p);
  }

  @Test
  public void foldsLeftAssociative_binaryOperators() {
    RootParser root = new RootParser();
    ASTMapperContext ctx = ASTMapperContext.create(root);

    OperandParser operand = new OperandParser();
    OpParser plus = new OpParser();
    OpParser minus = new OpParser();

    Token t1 = tok("1", operand);
    Token t2 = tok("2", operand);
    Token t3 = tok("3", operand);

    Token opPlus = tok("+", plus);
    Token opMinus = tok("-", minus);

    // parse result children: 1 + 2 - 3
    Token parsed = new Token(TokenKind.consumed, new TokenList(t1, opPlus, t2, opMinus, t3), root);

    Token ast = ctx.toAST(parsed);

    // root should become '-' operator node
    assertSame(minus.getClass(), ast.getParser().getClass());
    assertEquals("-", ast.getSource().toString());

    // '-' has two children: (1 + 2) and 3
    assertEquals(2, ast.getAstNodeChildren().size());
    Token left = ast.getAstNodeChildren().get(0);
    Token right = ast.getAstNodeChildren().get(1);

    assertEquals("3", right.getSource().toString());
    assertSame(operand.getClass(), right.getParser().getClass());

    // left subtree should be '+' node with children 1 and 2
    assertEquals("+", left.getSource().toString());
    assertSame(plus.getClass(), left.getParser().getClass());
    assertEquals(2, left.getAstNodeChildren().size());
    assertEquals("1", left.getAstNodeChildren().get(0).getSource().toString());
    assertEquals("2", left.getAstNodeChildren().get(1).getSource().toString());
  }

  @Test
  public void singleOperand_returnsThatOperand() {
    RootParser root = new RootParser();
    ASTMapperContext ctx = ASTMapperContext.create(root);

    OperandParser operand = new OperandParser();
    Token t1 = tok("1", operand);

    Token parsed = new Token(TokenKind.consumed, new TokenList(t1), root);

    Token ast = ctx.toAST(parsed);

    // when there is only one child, mapper returns that mapped child.
    assertEquals("1", ast.getSource().toString());
    assertSame(operand.getClass(), ast.getParser().getClass());
  }
}
