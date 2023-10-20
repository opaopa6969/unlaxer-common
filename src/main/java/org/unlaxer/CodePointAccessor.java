package org.unlaxer;

import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Stream;

public interface CodePointAccessor extends java.io.Serializable, Comparable<CodePointAccessor>, StringBase {
  
  Function<String,CodePointAccessor> stringToStringInterface();
  Function<CodePointAccessor,String> stringInterfaceToStgring();
  
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
  
  String getSource();

  StringIndexAccessor stringIndexAccessor();
  
//  StringBase stringBase();



  default CodePoint codePointAt(CodePointIndex index) {
    return new CodePoint(stringIndexAccessor().codePointAt(toStringIndex(index).value));
  }
  
  default CodePoint codePointBefore(CodePointIndex index) {
    return new CodePoint(stringIndexAccessor().codePointBefore(toStringIndex(index).value));
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
    return equalsIgnoreCase(anotherString.getSource());
  }

  default int compareTo(CodePointAccessor  anotherString) {
    return compareTo(anotherString.getSource());
  }

  default int compareToIgnoreCase(CodePointAccessor str) {
    return compareToIgnoreCase(str.toString());
  }
  
  default boolean regionMatches(CodePointIndex toffset, String other, CodePointIndex ooffset, Length len) {
    return stringIndexAccessor().regionMatches(toStringIndex(toffset).value, other , toStringIndex(ooffset).value, len.value);
  }
  
  default boolean regionMatches(CodePointIndex toffset, CodePointAccessor other, CodePointIndex ooffset, Length len) {
    return stringIndexAccessor().regionMatches(toStringIndex(toffset).value, other.getSource(), toStringIndex(ooffset).value, len.value);
  }
  
  default boolean regionMatches(boolean ignoreCase, CodePointIndex toffset, String other, CodePointIndex ooffset, Length len) {
    return stringIndexAccessor().regionMatches(ignoreCase, toStringIndex(toffset), other, toStringIndex(ooffset), len);
  }
  
  default boolean regionMatches(boolean ignoreCase, CodePointIndex toffset, CodePointAccessor other, CodePointIndex ooffset, Length len) {
    return stringIndexAccessor().regionMatches(ignoreCase, toStringIndex(toffset), other.getSource(), toStringIndex(ooffset), len);
  }

  default boolean startsWith(String prefix, CodePointIndex toffset) {
    return stringIndexAccessor().startsWith(prefix, toStringIndex(toffset));
  }
  
  default boolean startsWith(CodePointAccessor prefix, CodePointIndex toffset) {
    return stringIndexAccessor().startsWith(prefix.getSource(), toffset.value);
  }
  
  default boolean startsWith(CodePointAccessor prefix) {
    return startsWith(prefix.getSource());
  }

  
  default boolean endsWith(CodePointAccessor suffix) {
    return endsWith(suffix.getSource());
  }

  default CodePointIndexWithNegativeValue indexOf(CodePoint codePoint) {
    return new CodePointIndexWithNegativeValue(
        toCodePointIndex(new StringIndex(
            indexOf(codePoint.value))));
  }
  
  default CodePointIndexWithNegativeValue indexOf(CodePoint codePoint, CodePointIndex fromIndex) {
    return new CodePointIndexWithNegativeValue(
        toCodePointIndex(stringIndexAccessor().indexOf(codePoint,toStringIndex(fromIndex)).toStringIndex()));
  }

  
  default CodePointIndexWithNegativeValue lastIndexOf(CodePoint codePoint) {
    return new CodePointIndexWithNegativeValue(
        toCodePointIndex(new StringIndex(lastIndexOf(codePoint.value))));
  }
  
  default CodePointIndexWithNegativeValue lastIndexOf(CodePoint codePoint, CodePointIndex fromIndex) {
    
    return new CodePointIndexWithNegativeValue(
        toCodePointIndex(stringIndexAccessor().lastIndexOf(codePoint , toStringIndex(fromIndex)).toStringIndex()));
  }
  
  default CodePointIndexWithNegativeValue indexOf(CodePointAccessor str) {
    return new CodePointIndexWithNegativeValue(
        toCodePointIndex(new StringIndex(indexOf(str.getSource()))));
  }
    
  default CodePointIndex indexOf(CodePointAccessor str, CodePointIndex fromIndex) {
    return new CodePointIndexWithNegativeValue(
        toCodePointIndexWithNegativeValue(stringIndexAccessor().indexOf(str,toStringIndex(fromIndex))));
  }

  default CodePointIndex lastIndexOf(CodePointAccessor str) {
    return new CodePointIndexWithNegativeValue(
        toCodePointIndexWithNegativeValue(new StringIndexWithNegativeValue(lastIndexOf(str.getSource()))));
  }

  default CodePointIndex lastIndexOf(CodePointAccessor str, CodePointIndex fromIndex) {
    return new CodePointIndexWithNegativeValue(
        toCodePointIndexWithNegativeValue(new StringIndexWithNegativeValue(stringIndexAccessor().lastIndexOf(str.getSource() , 
            toStringIndex(fromIndex).value))));
  }
  
  default CodePointAccessor substring(CodePointIndex beginIndex) {
    return stringToStringInterface().apply(
        stringIndexAccessor().substring(toStringIndex(beginIndex).value));
  }
  
  default CodePointAccessor substring(CodePointIndex beginIndex, CodePointIndex endIndex) {
      return stringToStringInterface().apply(
          stringIndexAccessor().substring(toStringIndex(beginIndex).value,toStringIndex(endIndex).value));
  }
  
  default CodePointAccessor concat(CodePointAccessor str) {
    return stringToStringInterface().apply(concat(str.getSource()));
  }
  
  default CodePointAccessor replaceAsStringInterface(char oldChar, char newChar) {
    return stringToStringInterface().apply(replace(oldChar, newChar));
  }
  
  
  default CodePointAccessor replaceFirst(String regex, CodePointAccessor replacement) {
    return stringToStringInterface().apply(replaceFirst(regex, replacement.toString()));
  }
  
  default CodePointAccessor replaceAll(String regex, CodePointAccessor replacement) {
    return stringToStringInterface().apply(replaceAll(regex, replacement.toString()));
  }
  
  default CodePointAccessor replaceaAsStringInterface(CharSequence target, CharSequence replacement) {
    return stringToStringInterface().apply(replace(target, replacement));
  }

  default CodePointAccessor[] splitAsStringInterface(String regex, int limit) {
    
    String[] returning = split(regex, limit);
    
    CodePointAccessor[] result = new CodePointAccessor[returning.length];
    
    int i =0;
    for (String string : returning) {
      
      result[i++] = stringToStringInterface().apply(string);
    }
    return result;
  }
  
  default CodePointAccessor[] splitAsStringInterface(String regex) {
    
    String[] returning = split(regex);
    
    CodePointAccessor[] result = new CodePointAccessor[returning.length];
    
    int i =0;
    for (String string : returning) {
      
      result[i++] = stringToStringInterface().apply(string);
    }
    return result;
  }
  
  default CodePointAccessor toLowerCaseAsStringInterface(Locale locale){
    return stringToStringInterface().apply(toLowerCase(locale));
  }
  
  default CodePointAccessor toLowerCaseAsStringInterface(){
    return stringToStringInterface().apply(toLowerCase());
  }

  default CodePointAccessor toUpperCaseAsStringInterface(Locale locale){
    return stringToStringInterface().apply(toUpperCase(locale));
  }
  
  default CodePointAccessor toUpperCaseAsStringInterface(){
    return stringToStringInterface().apply(toUpperCase());
  }
  
  default CodePointAccessor trimAsStringInterface() {
    return stringToStringInterface().apply(trim());
  }
  
  default CodePointAccessor stripAsStringInterface() {
    
    return stringToStringInterface().apply(strip());
  }
  
  default CodePointAccessor stripLeadingAsStringInterface() {
    return stringToStringInterface().apply((stripLeading()));
  }
  
  default CodePointAccessor stripTrailingAsStringInterface() {
    return stringToStringInterface().apply(stripTrailing());
  }
  
  default Stream<CodePointAccessor> linesAsStringInterface(){
    Function<String, CodePointAccessor> stringToStringInterface = stringToStringInterface();
    return lines().map(stringToStringInterface);
  }

  default CodePointAccessor repeatAsStringInterface(int count) {
    
    return stringToStringInterface().apply(repeat(count));
  }
}