package org.unlaxer.ast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

public class BinaryExpressionEvaluatorBoolTest {

  static class DummyParser extends AbstractParser {
    private static final long serialVersionUID = 1L;

    DummyParser(ASTNodeKind kind) { setASTNodeKind(kind); }

    @Override public Parser createParser() { return this; }
    @Override public void prepareChildren(Parsers c) {}
    @Override public ChildOccurs getChildOccurs() { return ChildOccurs.none; }
  }

  private static Token leaf(String s) {
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
  public void eval_comparison_numbers() {
    Token ast = op(">", leaf("a"), leaf("10"));
    EvalValue v = new BinaryExpressionEvaluator(Map.of("a", EvalValue.number(new BigDecimal("11")))).eval(ast);
    assertEquals(EvalValue.Kind.BOOL, v.kind());
    assertTrue(v.asBool());
  }

  @Test
  public void eval_logic_shortCircuit() {
    // false && (undefinedVar == 1) should not throw
    Token dangerous = op("==", leaf("undefinedVar"), leaf("1"));
    Token ast = op("&&", leaf("false"), dangerous);

    EvalValue v = new BinaryExpressionEvaluator(Map.of()).eval(ast);
    assertEquals(EvalValue.Kind.BOOL, v.kind());
    assertEquals(false, v.asBool());
  }

  @Test
  public void eval_string_equality() {
    Token ast = op("==", leaf("\"x\""), leaf("name"));
    EvalValue v = new BinaryExpressionEvaluator(Map.of("name", EvalValue.string("x"))).eval(ast);
    assertEquals(true, v.asBool());
  }
}
