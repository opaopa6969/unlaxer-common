package org.unlaxer.ast;

import static org.junit.Assert.assertEquals;

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

public class BinaryExpressionPrinterTest {

  static class DummyParser extends AbstractParser {
    private static final long serialVersionUID = 1L;

    DummyParser(ASTNodeKind kind) { setASTNodeKind(kind); }

    @Override public Parser createParser() { return this; }
    @Override public void prepareChildren(Parsers c) {}
    @Override public ChildOccurs getChildOccurs() { return ChildOccurs.none; }
  }

  private static Token operand(String s) {
    return new Token(TokenKind.consumed, StringSource.create(s, SourceKind.root), new DummyParser(ASTNodeKind.Operand));
  }

  private static Token op(String s, Token left, Token right) {
    return new Token(
        TokenKind.consumed,
        StringSource.create(s, SourceKind.root),
        new DummyParser(ASTNodeKind.Operator),
        new TokenList(left, right)
    );
  }

  @Test
  public void prints_withParenthesesAlways() {
    // ((1+2)-3)
    Token ast = op("-",
        op("+", operand("1"), operand("2")),
        operand("3")
    );

    assertEquals("((1 + 2) - 3)", BinaryExpressionPrinter.print(ast));
  }

  @Test
  public void prints_identifiers_and_stringLiterals_asIs() {
    Token ast = op("+", operand("name"), operand("\"!\""));
    assertEquals("(name + \"!\")", BinaryExpressionPrinter.print(ast));
  }

  @Test
  public void prints_logic_and_comparison() {
    Token ast = op("&&",
        op(">", operand("a"), operand("10")),
        op("==", operand("b"), operand("\"x\""))
    );
    assertEquals("((a > 10) && (b == \"x\"))", BinaryExpressionPrinter.print(ast));
  }
}
