package org.unlaxer.ast;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Map;

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

public class BinaryExpressionEvaluatorTest {

  static class DummyParser extends AbstractParser {
    private static final long serialVersionUID = 1L;

    DummyParser(ASTNodeKind kind) { setASTNodeKind(kind); }

    @Override public Parser createParser() { return this; }
    @Override public void prepareChildren(Parsers c) {}
    @Override public ChildOccurs getChildOccurs() { return ChildOccurs.none; }
  }

  private static Token leaf(String s, ASTNodeKind kind) {
    return new Token(TokenKind.consumed, StringSource.create(s, SourceKind.root), new DummyParser(kind));
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
  public void eval_numbers() {
    // (1 + 2) * 3 = 9
    Token ast = op("*",
        op("+", leaf("1", ASTNodeKind.Operand), leaf("2", ASTNodeKind.Operand)),
        leaf("3", ASTNodeKind.Operand)
    );

    EvalValue v = new BinaryExpressionEvaluator(Map.of()).eval(ast);
    assertEquals(EvalValue.Kind.NUMBER, v.kind());
    assertEquals(new BigDecimal("9"), v.asNumber());
  }

  @Test
  public void eval_identifiers() {
    // a + 2
    Token ast = op("+", leaf("a", ASTNodeKind.Operand), leaf("2", ASTNodeKind.Operand));

    EvalValue v = new BinaryExpressionEvaluator(Map.of("a", EvalValue.number(new BigDecimal("40")))).eval(ast);
    assertEquals(new BigDecimal("42"), v.asNumber());
  }

  @Test
  public void eval_string_concat() {
    // "hi" + name
    Token ast = op("+", leaf("\"hi\"", ASTNodeKind.Operand), leaf("name", ASTNodeKind.Operand));

    EvalValue v = new BinaryExpressionEvaluator(Map.of("name", EvalValue.string(" opa"))).eval(ast);
    assertEquals(EvalValue.Kind.STRING, v.kind());
    assertEquals("hi opa", v.asString());
  }
}
