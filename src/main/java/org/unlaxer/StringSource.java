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
  
  private String source;
  private int[] codePoints;
  NavigableMap<CodePointIndex, LineNumber> lineNumberByIndex = new TreeMap<>();
  Map<CodePointIndex,StringIndex> stringIndexByCodePointIndex = new HashMap<>();
  Map<StringIndex,CodePointIndex> codePointIndexByStringIndex = new HashMap<>();
  
  public StringSource(String source) {
    super();
    this.source = source;
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
    return new StringLength(source.length());
  }
  
  public CodePointLength codePointLength() {
    return new CodePointLength(codePoints.length);
  }

  @Override
  public boolean isEmpty() {
    return source.isEmpty();
  }

  @Override
  public char charAt(int index) {
    return source.charAt(index);
  }

  @Override
  public byte[] getBytes(String charsetName) throws UnsupportedEncodingException {
    return source.getBytes(charsetName);
  }

  @Override
  public byte[] getBytes(Charset charset) {
    return source.getBytes(charset);
  }

  @Override
  public byte[] getBytes() {
    return source.getBytes();
  }

  @Override
  public boolean contentEquals(StringBuffer sb) {
    return source.contentEquals(sb);
  }

  @Override
  public boolean contentEquals(CharSequence cs) {
    return source.contentEquals(cs);
  }

  @Override
  public boolean equalsIgnoreCase(String anotherString) {
    return source.equalsIgnoreCase(anotherString);
  }

  @Override
  public int compareTo(String anotherString) {
    return source.compareTo(anotherString);
  }

  @Override
  public int compareToIgnoreCase(String str) {
    return source.compareToIgnoreCase(str);
  }

  @Override
  public boolean startsWith(String prefix) {
    return source.startsWith(prefix);
  }

  @Override
  public boolean endsWith(String suffix) {
    return source.endsWith(suffix);
  }

  @Override
  public int indexOf(int ch) {
    return source.indexOf(ch);
  }

  @Override
  public int lastIndexOf(int ch) {
    return source.lastIndexOf(ch);
  }

  @Override
  public int indexOf(String str) {
    return source.indexOf(str);
  }

  @Override
  public int lastIndexOf(String str) {
    return source.lastIndexOf(str);
  }

  @Override
  public CharSequence subSequence(int beginIndex, int endIndex) {
    return source.subSequence(beginIndex, endIndex);
  }

  @Override
  public String concat(String str) {
    return source.concat(str);
  }

  @Override
  public String replace(char oldChar, char newChar) {
    return source.replace(oldChar, newChar);
  }

  @Override
  public boolean matches(String regex) {
    return source.matches(regex);
  }

  @Override
  public boolean contains(CharSequence s) {
    return source.contains(s);
  }

  @Override
  public String replaceFirst(String regex, String replacement) {
    return source.replaceFirst(regex, replacement);
  }

  @Override
  public String replaceAll(String regex, String replacement) {
    return source.replaceAll(regex, replacement);
  }

  @Override
  public String replace(CharSequence target, CharSequence replacement) {
    return source.replace(target, replacement);
  }

  @Override
  public String[] split(String regex, int limit) {
    return source.split(regex,limit);
  }

  @Override
  public String[] split(String regex) {
    return source.split(regex);
  }

  @Override
  public String toLowerCase(Locale locale) {
    return source.toLowerCase(locale);
  }

  @Override
  public String toLowerCase() {
    return source.toLowerCase();
  }

  @Override
  public String toUpperCase(Locale locale) {
    return source.toUpperCase(locale);
  }

  @Override
  public String toUpperCase() {
    return source.toUpperCase();
  }

  @Override
  public String trim() {
    return source.trim();
  }

  @Override
  public String strip() {
    return source.strip();
  }

  @Override
  public String stripLeading() {
    return source.stripLeading();
  }

  @Override
  public String stripTrailing() {
    return source.stripTrailing();
  }

  @Override
  public boolean isBlank() {
    return source.isBlank();
  }

  @Override
  public Stream<String> lines() {
    return source.lines();
  }

  @Override
  public IntStream chars() {
    return source.chars();
  }

  @Override
  public IntStream codePoints() {
    return source.codePoints();
  }

  @Override
  public char[] toCharArray() {
    return source.toCharArray();
  }

  @Override
  public String intern() {
    return source.intern();
  }

  @Override
  public String repeat(int count) {
    return source.repeat(count);
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
    return source.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return source.equals(obj);
  }

  @Override
  public String toString() {
    return source;
  }

  @Override
  public String getSource() {
    return source;
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
  
  public Source subSource(CodePointIndex startIndexInclusive, CodePointIndex endIndexExclusive) {
    return new StringSource(subString(startIndexInclusive,endIndexExclusive));
  }
  
  public Source subSource(CodePointIndex startIndexInclusive, CodePointLength codePointLength) {
    return new StringSource(subString(startIndexInclusive,codePointLength));
  }
 
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
}
