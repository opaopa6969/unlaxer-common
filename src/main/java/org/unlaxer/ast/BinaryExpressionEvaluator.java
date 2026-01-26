package org.unlaxer.ast;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;
import java.util.Objects;

import org.unlaxer.Token;
import org.unlaxer.TokenList;
import org.unlaxer.parser.Parser;

public final class BinaryExpressionEvaluator {

  private final Map<String, EvalValue> env;
  private final MathContext mathContext;

  public BinaryExpressionEvaluator(Map<String, EvalValue> env) {
    this(env, MathContext.DECIMAL128);
  }

  public BinaryExpressionEvaluator(Map<String, EvalValue> env, MathContext mathContext) {
    this.env = Objects.requireNonNull(env, "env");
    this.mathContext = Objects.requireNonNull(mathContext, "mathContext");
  }

  public EvalValue eval(Token ast) {
    Objects.requireNonNull(ast, "ast");

    ASTNodeKind kind = kindOf(ast);

    if (kind == ASTNodeKind.Operand || kind == ASTNodeKind.ChoicedOperand) {
      return evalOperand(ast.getSource().toString());
    }

    if (kind == ASTNodeKind.Operator || kind == ASTNodeKind.ChoicedOperator) {
      TokenList ch = ast.getAstNodeChildren();
      if (ch.size() != 2) {
        throw new IllegalArgumentException("operator node must have 2 children: " + ast.getSource());
      }

      String op = ast.getSource().toString();
      // NOTE: short-circuit for && / || (optional but useful)
      if ("&&".equals(op)) {
        EvalValue left = eval(ch.get(0));
        boolean lb = left.asBool();
        if (!lb) return EvalValue.bool(false);
        EvalValue right = eval(ch.get(1));
        return EvalValue.bool(lb && right.asBool());
      }
      if ("||".equals(op)) {
        EvalValue left = eval(ch.get(0));
        boolean lb = left.asBool();
        if (lb) return EvalValue.bool(true);
        EvalValue right = eval(ch.get(1));
        return EvalValue.bool(lb || right.asBool());
      }

      EvalValue left = eval(ch.get(0));
      EvalValue right = eval(ch.get(1));
      return apply(op, left, right);
    }

    throw new IllegalArgumentException("unexpected node kind: " + kind + " source=" + ast.getSource());
  }

  private EvalValue evalOperand(String text) {
    String s = text.trim();

    // bool literals
    if ("true".equalsIgnoreCase(s)) return EvalValue.bool(true);
    if ("false".equalsIgnoreCase(s)) return EvalValue.bool(false);

    // string literal: "..." or '...' (no escape handling yet)
    if (isQuoted(s)) {
      return EvalValue.string(unquote(s));
    }

    // number literal
    try {
      return EvalValue.number(new BigDecimal(s));
    } catch (NumberFormatException ignore) {
      // identifier
    }

    EvalValue v = env.get(s);
    if (v == null) {
      throw new IllegalArgumentException("undefined identifier: " + s);
    }
    return v;
  }

  private EvalValue apply(String op, EvalValue l, EvalValue r) {
    return switch (op) {
      // arithmetic
      case "+" -> plus(l, r);
      case "-" -> EvalValue.number(l.asNumber().subtract(r.asNumber(), mathContext));
      case "*" -> EvalValue.number(l.asNumber().multiply(r.asNumber(), mathContext));
      case "/" -> EvalValue.number(l.asNumber().divide(r.asNumber(), mathContext));

      // equality
      case "==" -> EvalValue.bool(equalsValue(l, r));
      case "!=" -> EvalValue.bool(!equalsValue(l, r));

      // ordering (numbers or strings only)
      case "<"  -> EvalValue.bool(compare(l, r) < 0);
      case "<=" -> EvalValue.bool(compare(l, r) <= 0);
      case ">"  -> EvalValue.bool(compare(l, r) > 0);
      case ">=" -> EvalValue.bool(compare(l, r) >= 0);

      default -> throw new IllegalArgumentException("unknown operator: " + op);
    };
  }

  private EvalValue plus(EvalValue l, EvalValue r) {
    if (l.kind() == EvalValue.Kind.NUMBER && r.kind() == EvalValue.Kind.NUMBER) {
      return EvalValue.number(l.asNumber().add(r.asNumber(), mathContext));
    }
    // concat for any string involvement
    String ls = toStringForConcat(l);
    String rs = toStringForConcat(r);
    return EvalValue.string(ls + rs);
  }

  private static String toStringForConcat(EvalValue v) {
    return switch (v.kind()) {
      case STRING -> v.asString();
      case NUMBER -> v.asNumber().toPlainString();
      case BOOL -> Boolean.toString(v.asBool());
    };
  }

  private static boolean equalsValue(EvalValue l, EvalValue r) {
    if (l.kind() != r.kind()) {
      throw new IllegalArgumentException("cannot compare different kinds: " + l.kind() + " vs " + r.kind());
    }
    return switch (l.kind()) {
      case NUMBER -> l.asNumber().compareTo(r.asNumber()) == 0;
      case STRING -> l.asString().equals(r.asString());
      case BOOL -> l.asBool() == r.asBool();
    };
  }

  private static int compare(EvalValue l, EvalValue r) {
    if (l.kind() != r.kind()) {
      throw new IllegalArgumentException("cannot order different kinds: " + l.kind() + " vs " + r.kind());
    }
    return switch (l.kind()) {
      case NUMBER -> l.asNumber().compareTo(r.asNumber());
      case STRING -> l.asString().compareTo(r.asString());
      case BOOL -> throw new IllegalArgumentException("ordering not supported for BOOL");
    };
  }

  private static boolean isQuoted(String s) {
    return (s.length() >= 2 && s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"')
        || (s.length() >= 2 && s.charAt(0) == '\'' && s.charAt(s.length() - 1) == '\'');
  }

  private static String unquote(String s) {
    return s.substring(1, s.length() - 1);
  }

  private static ASTNodeKind kindOf(Token t) {
    Parser p = t.getParser();
    ASTNodeKind k = p.astNodeKind();
    return k != null ? k : ASTNodeKind.Other;
  }
}
