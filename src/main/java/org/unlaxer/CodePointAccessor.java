package org.unlaxer;

public interface CodePointAccessor extends Comparable<CodePointAccessor>, StringBase {
  
  
  StringIndex toStringIndex(CodePointIndex codePointIndex);
  StringIndexWithNegativeValue toStringIndex(CodePointIndexWithNegativeValue codePointIndex);
  CodePointIndex toCodePointIndex(StringIndex stringIndex);
  CodePointIndexWithNegativeValue toCodePointIndexWithNegativeValue(StringIndexWithNegativeValue stringIndex);
  /**
   * Returns the length of this string.
   * The length is equal to the number of <a href="Character.html#unicode">Unicode
   * code units</a> in the string.
   *
   * @return  the length of the sequence of characters represented by this
   *          object.
   */
  StringLength stringLength();
  
  CodePointLength codePointLength();
  
  String sourceAsString();
  
  Source source();

  StringIndexAccessor stringIndexAccessor();
  
//  StringBase stringBase();

  default CodePoint codePointAt(CodePointIndex index) {
    return new CodePoint(stringIndexAccessor().codePointAt(toStringIndex(index).value()));
  }
  
  default CodePoint codePointBefore(CodePointIndex index) {
    return new CodePoint(stringIndexAccessor().codePointBefore(toStringIndex(index).value()));
  }
  
  default Count codePointCount(CodePointIndex beginIndex, CodePointIndex endIndex) {
    return stringIndexAccessor().codePointCount(toStringIndex(beginIndex), toStringIndex(endIndex));
  }
  
  default CodePointIndex offsetByCodePoints(CodePointIndex index, CodePointOffset codePointOffset) {
    return toCodePointIndex(stringIndexAccessor().offsetByCodePoints(
        toStringIndex(index), codePointOffset));
  }
  
  default void getChars(CodePointIndex srcBegin, CodePointIndex srcEnd, char dst[], StringIndex dstBegin) {
    stringIndexAccessor().getChars(
        toStringIndex(srcBegin), toStringIndex(srcEnd), dst, dstBegin);
  }
  
  default void getBytes(CodePointIndex srcBegin, CodePointIndex srcEnd, byte dst[], StringIndex dstBegin) {
    stringIndexAccessor().getBytes(
        toStringIndex(srcBegin), toStringIndex(srcEnd), dst, dstBegin);
  }
  
  default boolean equalsIgnoreCase(CodePointAccessor anotherString) {
    return sourceAsString().equalsIgnoreCase(anotherString.sourceAsString());
  }

  default int compareTo(CodePointAccessor  anotherString) {
    return compareTo(anotherString.source());
  }

  default int compareToIgnoreCase(CodePointAccessor str) {
    return compareToIgnoreCase(str.toString());
  }
  
  default boolean regionMatches(CodePointIndex toffset, String other, CodePointIndex ooffset, Length len) {
    return stringIndexAccessor().regionMatches(toStringIndex(toffset).value(), other , toStringIndex(ooffset).value(), len.value());
  }
  
  default boolean regionMatches(CodePointIndex toffset, CodePointAccessor other, CodePointIndex ooffset, Length len) {
    return stringIndexAccessor().regionMatches(toStringIndex(toffset).value(), other.sourceAsString(), toStringIndex(ooffset).value(), len.value());
  }
  
  default boolean regionMatches(boolean ignoreCase, CodePointIndex toffset, String other, CodePointIndex ooffset, Length len) {
    return stringIndexAccessor().regionMatches(ignoreCase, toStringIndex(toffset), other, toStringIndex(ooffset), len);
  }
  
  default boolean regionMatches(boolean ignoreCase, CodePointIndex toffset, CodePointAccessor other, CodePointIndex ooffset, Length len) {
    return stringIndexAccessor().regionMatches(ignoreCase, toStringIndex(toffset), other.source(), toStringIndex(ooffset), len);
  }

  default boolean startsWith(String prefix, CodePointIndex toffset) {
    return stringIndexAccessor().startsWith(prefix, toStringIndex(toffset));
  }
  
  default boolean startsWith(CodePointAccessor prefix, CodePointIndex toffset) {
    return stringIndexAccessor().startsWith(prefix.sourceAsString(), toffset.value());
  }
  
  default boolean startsWith(CodePointAccessor prefix) {
    return startsWith(prefix.source());
  }

  
  default boolean endsWith(CodePointAccessor suffix) {
    return endsWith(suffix.source());
  }

  default CodePointIndexWithNegativeValue indexOf(CodePoint codePoint) {
    return new CodePointIndexWithNegativeValue(
        toCodePointIndex(new StringIndex(
            indexOf(codePoint.value()))));
  }
  
  default CodePointIndexWithNegativeValue indexOf(CodePoint codePoint, CodePointIndex fromIndex) {
    return new CodePointIndexWithNegativeValue(
        toCodePointIndex(stringIndexAccessor().indexOf(codePoint,toStringIndex(fromIndex)).toStringIndex()));
  }

  
  default CodePointIndexWithNegativeValue lastIndexOf(CodePoint codePoint) {
    return new CodePointIndexWithNegativeValue(
        toCodePointIndex(new StringIndex(lastIndexOf(codePoint.value()))));
  }
  
  default CodePointIndexWithNegativeValue lastIndexOf(CodePoint codePoint, CodePointIndex fromIndex) {
    
    return new CodePointIndexWithNegativeValue(
        toCodePointIndex(stringIndexAccessor().lastIndexOf(codePoint , toStringIndex(fromIndex)).toStringIndex()));
  }
  
  default CodePointIndexWithNegativeValue indexOf(CodePointAccessor str) {
    return new CodePointIndexWithNegativeValue(
        toCodePointIndex(new StringIndex(indexOf(str.source()))));
  }
    
  default CodePointIndex indexOf(CodePointAccessor str, CodePointIndex fromIndex) {
    return new CodePointIndex(
        toCodePointIndexWithNegativeValue(stringIndexAccessor().indexOf(str,toStringIndex(fromIndex))));
  }

  default CodePointIndex lastIndexOf(CodePointAccessor str) {
    return new CodePointIndex(
        toCodePointIndexWithNegativeValue(new StringIndexWithNegativeValue(lastIndexOf(str.source()))));
  }

  default CodePointIndex lastIndexOf(CodePointAccessor str, CodePointIndex fromIndex) {
    return new CodePointIndex(
        toCodePointIndexWithNegativeValue(new StringIndexWithNegativeValue(stringIndexAccessor().lastIndexOf(str.sourceAsString() , 
            toStringIndex(fromIndex).value()))));
  }
  
  
  public default CodePointIndex endIndexExclusive() {
    return new CodePointIndex(codePointLength());
  }
  
  public LineNumber lineNumber(CodePointIndex Position);
  
  default CursorRange cursorRange(CodePointIndex startIndexInclusive, CodePointLength length) {

    //こういう処理が入っていたけどとりあえず無視してみる。
//  if(startIndexInclusive.value() + length.value() > codePoints.length){
//  CodePointIndex index = new CodePointIndex(startIndexInclusive.value());
//  CursorRange cursorRange = new CursorRange(new CursorImpl()
//      .setPosition(index)
//      .setLineNumber(lineNUmber(index))
//  );
//  return new StringSource(this , cursorRange , null);
//}

    
    CodePointIndex endIndexExclusive = new CodePointIndex(startIndexInclusive.newWithPlus(length));
    return cursorRange(startIndexInclusive, endIndexExclusive);
  }
  
  default CursorRange cursorRange(CodePointIndex startIndexInclusive, CodePointIndex endIndexExclusive) {
      CursorRange cursorRange = new CursorRange(
          new StartInclusiveCursorImpl()
            .setPosition(startIndexInclusive)
            .setLineNumber(lineNumber(startIndexInclusive)),
          new EndExclusiveCursorImpl()
            .setPosition(endIndexExclusive)
            .setLineNumber(lineNumber(endIndexExclusive))
      );
      return cursorRange;
  }
  
  int[] subCodePoints(CodePointIndex startIndexInclusive, CodePointIndex endIndexExclusive);
}