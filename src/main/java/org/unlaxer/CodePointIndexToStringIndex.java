package org.unlaxer;

public interface CodePointIndexToStringIndex /*extends Function<CodePointIndex,StringIndex>*/{
  StringIndex stringIndexFrom(CodePointIndex codePointIndex);
}