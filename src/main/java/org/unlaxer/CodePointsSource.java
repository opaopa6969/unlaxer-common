package org.unlaxer;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CodePointsSource implements StringInterface{
  
  private String source;
  private int[] codePoints;
  NavigableMap<CodePointIndex, LineNumber> lineNumberByIndex = new TreeMap<>();
  Map<CodePointIndex,StringIndex> stringIndexByCodePointIndex = new HashMap<>();
  
  public CodePointsSource(String source) {
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
      int codePointAt = codePoints[i];
      
      stringIndex = stringIndex.add(Character.isBmpCodePoint(codePointAt) ? 1:2);
      
      if(codePointAt == SymbolMap.lf.codes[0]) {
        lineNumberByIndex.put(new CodePointIndex(i+1), lineNumber.increments());
      }else if(codePointAt == SymbolMap.cr.codes[0]) {
        if(codePointCount-1!=i && codePoints[i+1] ==SymbolMap.lf.codes[0]) {
          i++;
          lineNumberByIndex.put(new CodePointIndex(i+1), lineNumber.increments());
          
          stringIndex = stringIndex.add(1);
          stringIndexByCodePointIndex.put(codePointIndex.add(1), stringIndex);
        }else {
          lineNumberByIndex.put(new CodePointIndex(i+1), lineNumber.increments());
        }
      }
    }
  }
  
  static Function<String, StringInterface> stringToStringInterface = CodePointsSource::new;

  @Override
  public Function<String, StringInterface> stringToStringInterface() {
    return stringToStringInterface;
  }

  @Override
  public Function<StringInterface, String> stringInterfaceToStgring() {
    return StringInterface::toString;
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
  public int codePointAt(int index) {
    return source.codePointAt(index);
  }

  @Override
  public int codePointBefore(int index) {
    return source.codePointBefore(index);
  }

  @Override
  public int codePointCount(int beginIndex, int endIndex) {
    return source.codePointCount(beginIndex, endIndex);
  }

  @Override
  public int offsetByCodePoints(int index, int codePointOffset) {
    return source.offsetByCodePoints(index, codePointOffset);
  }

  @Override
  public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
    source.getChars(srcBegin, srcEnd, dst, dstBegin);
  }

  @Override
  public void getBytes(int srcBegin, int srcEnd, byte[] dst, int dstBegin) {
    source.getBytes(srcBegin, srcEnd, dst, dstBegin);
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
  public boolean regionMatches(int toffset, String other, int ooffset, int len) {
    return source.regionMatches(toffset, other, ooffset, len);
  }

  @Override
  public boolean regionMatches(CodePointOffset toffset, StringInterface other, CodePointOffset ooffset, Length len) {
    return source.re;
  }

  @Override
  public boolean regionMatches(boolean ignoreCase, int toffset, String other, int ooffset, int len) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean regionMatches(boolean ignoreCase, CodePointOffset toffset, StringInterface other,
      CodePointOffset ooffset, Length len) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean startsWith(String prefix, int toffset) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean startsWith(StringInterface prefix, CodePointOffset toffset) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean startsWith(String prefix) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean startsWith(StringInterface prefix) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean endsWith(String suffix) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean endsWith(StringInterface suffix) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public int indexOf(int ch) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public CodePointIndex indexOf(CodePointIndex codePointIndex) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int indexOf(int ch, int fromIndex) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public CodePointIndex indexOf(CodePointIndex codePointIndex, CodePointIndex fromIndex) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int lastIndexOf(int ch) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public CodePointIndex lastIndexOf(CodePointIndex codePointIndex) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int lastIndexOf(int ch, int fromIndex) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public CodePointIndex lastIndexOf(CodePointIndex codePointIndex, CodePointIndex fromIndex) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int indexOf(String str) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public CodePointIndex indexOf(StringInterface str) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int indexOf(String str, int fromIndex) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public CodePointIndex indexOf(StringInterface str, CodePointIndex fromIndex) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int lastIndexOf(String str) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public CodePointIndex lastIndexOf(StringInterface str) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int lastIndexOf(String str, int fromIndex) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public CodePointIndex lastIndexOf(StringInterface str, CodePointIndex fromIndex) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String substring(int beginIndex) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public StringInterface substring(CodePointIndex beginIndex) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String substring(int beginIndex, int endIndex) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public StringInterface substring(CodePointIndex beginIndex, CodePointIndex endIndex) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CharSequence subSequence(int beginIndex, int endIndex) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String concat(String str) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public StringInterface concat(StringInterface str) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String replace(char oldChar, char newChar) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean matches(String regex) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean contains(CharSequence s) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public String replaceFirst(String regex, String replacement) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String replaceAll(String regex, String replacement) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String replace(CharSequence target, CharSequence replacement) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String[] split(String regex, int limit) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String[] split(String regex) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String toLowerCase(Locale locale) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String toLowerCase() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String toUpperCase(Locale locale) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String toUpperCase() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String trim() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String strip() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String stripLeading() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String stripTrailing() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isBlank() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Stream<String> lines() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IntStream chars() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IntStream codePoints() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public char[] toCharArray() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String intern() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String repeat(int count) {
    // TODO Auto-generated method stub
    return null;
  }

  


}
