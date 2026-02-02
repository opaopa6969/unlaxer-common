package org.unlaxer.ast;

import java.math.BigDecimal;
import java.util.Objects;

public final class EvalValue {

  public enum Kind { NUMBER, STRING, BOOL }

  private final Kind kind;
  private final BigDecimal number;
  private final String string;
  private final Boolean bool;

  private EvalValue(Kind kind, BigDecimal number, String string, Boolean bool) {
    this.kind = kind;
    this.number = number;
    this.string = string;
    this.bool = bool;
  }

  public static EvalValue number(BigDecimal n) {
    return new EvalValue(Kind.NUMBER, Objects.requireNonNull(n, "n"), null, null);
  }

  public static EvalValue string(String s) {
    return new EvalValue(Kind.STRING, null, Objects.requireNonNull(s, "s"), null);
  }

  public static EvalValue bool(boolean b) {
    return new EvalValue(Kind.BOOL, null, null, b);
  }

  public Kind kind() { return kind; }

  public BigDecimal asNumber() {
    if (kind != Kind.NUMBER) throw new IllegalStateException("not a number");
    return number;
  }

  public String asString() {
    if (kind != Kind.STRING) throw new IllegalStateException("not a string");
    return string;
  }

  public boolean asBool() {
    if (kind != Kind.BOOL) throw new IllegalStateException("not a bool");
    return bool.booleanValue();
  }

  @Override
  public String toString() {
    return switch (kind) {
      case NUMBER -> number.toPlainString();
      case STRING -> "\"" + string + "\"";
      case BOOL -> Boolean.toString(bool);
    };
  }
}
