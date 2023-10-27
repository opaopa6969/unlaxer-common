package org.unlaxer;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StringSource implements Source {
  
  private String sourceString;
  private int[] codePoints;
  NavigableMap<CodePointIndex, LineNumber> lineNumberByIndex = new TreeMap<>();
  Map<CodePointIndex,StringIndex> stringIndexByCodePointIndex = new HashMap<>();
  Map<StringIndex,CodePointIndex> codePointIndexByStringIndex = new HashMap<>();
  
  public StringSource(String source) {
    super();
    this.sourceString = source;
    codePoints = source.codePoints().toArray();
    int codePointCount = codePoints.length;
    
    LineNumber lineNumber = new LineNumber(0);
    CodePointIndex index = new CodePointIndex(0);
    lineNumberByIndex.put(index, lineNumber);
    
    StringIndex stringIndex = new StringIndex(0);
    CodePointIndex codePointIndex = new CodePointIndex(0);
    
    for (int i = 0; i < codePointCount; i++) {
      stringIndexByCodePointIndex.put(codePointIndex, stringIndex);
      codePointIndexByStringIndex.put(stringIndex,codePointIndex);
    
      int codePointAt = codePoints[i];
      
      int adding = Character.isBmpCodePoint(codePointAt) ? 1:2;
      stringIndex = stringIndex.add(adding);
      
      if(codePointAt == SymbolMap.lf.codes[0]) {
        lineNumberByIndex.put(new CodePointIndex(i+1), lineNumber.increments());
      }else if(codePointAt == SymbolMap.cr.codes[0]) {
        if(codePointCount-1!=i && codePoints[i+1] ==SymbolMap.lf.codes[0]) {
          i++;
          lineNumberByIndex.put(new CodePointIndex(i+1), lineNumber.increments());
          
          stringIndex = stringIndex.add(1);
          stringIndexByCodePointIndex.put(codePointIndex.add(1), stringIndex);
          codePointIndexByStringIndex.put(stringIndex,codePointIndex.add(1));
        }else {
          lineNumberByIndex.put(new CodePointIndex(i+1), lineNumber.increments());
        }
      }
    }
  }
  
  static Function<String, CodePointAccessor> stringToStringInterface = StringSource::new;
  static Function<CodePointAccessor, String> stringInterfaceToStgring = StringSource::toString;
  
  @Override
  public Function<String, CodePointAccessor> stringToStringInterface() {
    return stringToStringInterface;
  }

  @Override
  public Function<CodePointAccessor, String> stringInterfaceToStgring() {
    return stringInterfaceToStgring;
  }

  @Override
  public StringLength stringLength() {
    return new StringLength(sourceString.length());
  }
  
  public CodePointLength codePointLength() {
    return new CodePointLength(codePoints.length);
  }

  @Override
  public boolean isEmpty() {
    return sourceString.isEmpty();
  }

  @Override
  public char charAt(int index) {
    return sourceString.charAt(index);
  }

  @Override
  public byte[] getBytes(String charsetName) throws UnsupportedEncodingException {
    return sourceString.getBytes(charsetName);
  }

  @Override
  public byte[] getBytes(Charset charset) {
    return sourceString.getBytes(charset);
  }

  @Override
  public byte[] getBytes() {
    return sourceString.getBytes();
  }

  @Override
  public boolean contentEquals(StringBuffer sb) {
    return sourceString.contentEquals(sb);
  }

  @Override
  public boolean contentEquals(CharSequence cs) {
    return sourceString.contentEquals(cs);
  }

  @Override
  public boolean equalsIgnoreCase(String anotherString) {
    return sourceString.equalsIgnoreCase(anotherString);
  }

  @Override
  public int compareTo(String anotherString) {
    return sourceString.compareTo(anotherString);
  }

  @Override
  public int compareToIgnoreCase(String str) {
    return sourceString.compareToIgnoreCase(str);
  }

  @Override
  public boolean startsWith(String prefix) {
    return sourceString.startsWith(prefix);
  }

  @Override
  public boolean endsWith(String suffix) {
    return sourceString.endsWith(suffix);
  }

  @Override
  public int indexOf(int ch) {
    return sourceString.indexOf(ch);
  }

  @Override
  public int lastIndexOf(int ch) {
    return sourceString.lastIndexOf(ch);
  }

  @Override
  public int indexOf(String str) {
    return sourceString.indexOf(str);
  }

  @Override
  public int lastIndexOf(String str) {
    return sourceString.lastIndexOf(str);
  }

  @Override
  public CharSequence subSequence(int beginIndex, int endIndex) {
    return sourceString.subSequence(beginIndex, endIndex);
  }

  @Override
  public String concat(String str) {
    return sourceString.concat(str);
  }

  @Override
  public String replace(char oldChar, char newChar) {
    return sourceString.replace(oldChar, newChar);
  }

  @Override
  public boolean matches(String regex) {
    return sourceString.matches(regex);
  }

  @Override
  public boolean contains(CharSequence s) {
    return sourceString.contains(s);
  }

  @Override
  public String replaceFirst(String regex, String replacement) {
    return sourceString.replaceFirst(regex, replacement);
  }

  @Override
  public String replaceAll(String regex, String replacement) {
    return sourceString.replaceAll(regex, replacement);
  }

  @Override
  public String replace(CharSequence target, CharSequence replacement) {
    return sourceString.replace(target, replacement);
  }

  @Override
  public String[] split(String regex, int limit) {
    return sourceString.split(regex,limit);
  }

  @Override
  public String[] split(String regex) {
    return sourceString.split(regex);
  }

  @Override
  public String toLowerCase(Locale locale) {
    return sourceString.toLowerCase(locale);
  }

  @Override
  public String toLowerCase() {
    return sourceString.toLowerCase();
  }

  @Override
  public String toUpperCase(Locale locale) {
    return sourceString.toUpperCase(locale);
  }

  @Override
  public String toUpperCase() {
    return sourceString.toUpperCase();
  }

  @Override
  public String trim() {
    return sourceString.trim();
  }

  @Override
  public String strip() {
    return sourceString.strip();
  }

  @Override
  public String stripLeading() {
    return sourceString.stripLeading();
  }

  @Override
  public String stripTrailing() {
    return sourceString.stripTrailing();
  }

  @Override
  public boolean isBlank() {
    return sourceString.isBlank();
  }

  @Override
  public Stream<String> lines() {
    return sourceString.lines();
  }

  @Override
  public IntStream chars() {
    return sourceString.chars();
  }

  @Override
  public IntStream codePoints() {
    return sourceString.codePoints();
  }

  @Override
  public char[] toCharArray() {
    return sourceString.toCharArray();
  }

  @Override
  public String intern() {
    return sourceString.intern();
  }

  @Override
  public String repeat(int count) {
    return sourceString.repeat(count);
  }

  @Override
  public StringIndex toStringIndex(CodePointIndex codePointIndex) {
    return stringIndexByCodePointIndex.get(codePointIndex);
  }

  @Override
  public CodePointIndex toCodePointIndex(StringIndex stringIndex) {
    return codePointIndexByStringIndex.get(stringIndex);
  }

  @Override
  public int hashCode() {
    return sourceString.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return sourceString.equals(obj);
  }

  @Override
  public String toString() {
    return sourceString;
  }

  @Override
  public Source getSource() {
    return this;
  }

  @Override
  public int length() {
    return 0;
  }

  @Override
  public StringIndexWithNegativeValue toStringIndex(CodePointIndexWithNegativeValue codePointIndex) {
    if(codePointIndex.isNegative()) {
      return new StringIndexWithNegativeValue(codePointIndex.value());
    }
    return new StringIndexWithNegativeValue(toStringIndex(codePointIndex.toCodePointIndex()));
  }

  @Override
  public CodePointIndexWithNegativeValue toCodePointIndexWithNegativeValue(StringIndexWithNegativeValue stringIndex) {
    if(stringIndex.isNegative()) {
      return new CodePointIndexWithNegativeValue(stringIndex.value());
    }
    return new CodePointIndexWithNegativeValue(toCodePointIndex(stringIndex.toStringIndex()));
  }

  @Override
  public StringIndexAccessor stringIndexAccessor() {
    return null;
  }
  
  public static String toString(CodePointAccessor codePointAccessor) {
    
    return codePointAccessor.toString();
  }

  @Override
  public RangedString peek(CodePointIndex startIndexInclusive, CodePointLength length) {
    
    if(startIndexInclusive.value() + length.value() > codePoints.length){
      CodePointIndex index = new CodePointIndex(startIndexInclusive.value());
      CursorRange cursorRange = new CursorRange(new CursorImpl()
          .setPosition(index)
          .setLineNumber(getLineNUmber(index))
      );
      return new RangedString(cursorRange);
    }
    
    CodePointIndex endIndex = new CodePointIndex(startIndexInclusive.value() +length.value());
    CursorRange cursorRange = new CursorRange(
        new CursorImpl()
          .setPosition(startIndexInclusive)
          .setLineNumber(getLineNUmber(startIndexInclusive)),
        new CursorImpl()
          .setPosition(endIndex)
          .setLineNumber(getLineNUmber(endIndex))
        
    );

    return new RangedString(
        cursorRange,
        subSource(startIndexInclusive, length)
    );
  }
  
  @Override
  public Source subSource(CodePointIndex startIndexInclusive, CodePointIndex endIndexExclusive) {
    return new StringSource(subString(startIndexInclusive,endIndexExclusive));
  }
  
  @Override
  public Source subSource(CodePointIndex startIndexInclusive, CodePointLength codePointLength) {
    return new StringSource(subString(startIndexInclusive,codePointLength));
  }
 
  @Override
  public int[] subCodePoints(CodePointIndex startIndexInclusive, CodePointIndex endIndexExclusive) {
    return Arrays.copyOfRange(codePoints, startIndexInclusive.value() , endIndexExclusive.value());
  }
  
  public String subString(CodePointIndex startIndexInclusive, CodePointIndex endIndexExclusive) {
    return new String(codePoints, startIndexInclusive.value() , endIndexExclusive.value() - startIndexInclusive.value());
  }
  
  public String subString(CodePointIndex startIndexInclusive, CodePointLength length) {
    return new String(codePoints, startIndexInclusive.value() , length.value());
  }

  @Override
  public CodePointLength getLength() {
    return new CodePointLength(codePoints.length);
  }

  @Override
  public LineNumber getLineNUmber(CodePointIndex Position) {
    return lineNumberByIndex.floorEntry(Position).getValue();
  }

  @Override
  public String getSourceAsString() {
    return sourceString;
  }
}
