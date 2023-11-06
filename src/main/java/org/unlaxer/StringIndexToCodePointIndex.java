package org.unlaxer;

public interface StringIndexToCodePointIndex /*extends Function<CodePointIndex,StringIndex>*/{
    CodePointIndex codePointIndexFrom(StringIndex stringIndex);
}