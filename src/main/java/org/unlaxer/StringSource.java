package org.unlaxer;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.unlaxer.util.function.TriFunction;

public class StringSource implements Source {
  
  private final Source root;
  private final Source parent;
  private final String sourceString;
//private final CodePoint[] codePoints;
  private final int[] codePoints;
  private final RootPositionResolver rootPositionResolver;
  private final SubPositionResolver subPositionResolver;
  private final Depth depth;
  private final SourceKind sourceKind;
  private final CodePointOffset offsetFromParent;
  private final StringIndexAccessor stringIndexAccessor;
  
  public static StringSource create(String source , SourceKind sourceKind) {
    if(sourceKind == SourceKind.subSource) {
      throw new IllegalArgumentException();
    }
    return new StringSource(source , sourceKind , new CodePointOffset(0));
  }
  
  public static StringSource createRootSource(String source) {
    return new StringSource(source , SourceKind.root , null ,  new CodePointOffset(0));
  }
  
  public static StringSource createDetachedSource(String source , Source root) {
    return new StringSource(source , SourceKind.detached , root , new CodePointOffset(0));
  }
  
  public static StringSource createDetachedSource(String source) {
    return new StringSource(source , SourceKind.detached , null , new CodePointOffset(0));
  }

  
  public static StringSource createDetachedSource(String source , Source root , CodePointOffset codePointOffset) {
    return new StringSource(source , SourceKind.detached , root , codePointOffset);
  }
  
  public StringSource(String source , SourceKind sourceKind , CodePointOffset offsetFromParent) {
    this(source , sourceKind , null , offsetFromParent);
  }

  
  StringSource(String source , SourceKind sourceKind , Source root , CodePointOffset offsetFromParent) {
    super();
    Objects.requireNonNull(source,"source require non null");
    this.root = root == null ? this : root;
    parent = this.root;
    depth = new Depth(0);
    this.sourceKind = sourceKind;
    this.sourceString = source;
    this.offsetFromParent = offsetFromParent;
//    codePoints = source.codePoints().mapToObj(CodePoint::new).toArray(CodePoint[]::new);
    codePoints = source.codePoints().toArray();
    rootPositionResolver = root == null ? 
        new RootPositionResolverImpl(codePoints) : 
        root.rootPositionResolver(); 
    subPositionResolver = new SubPositionResolverImpl(codePoints,rootPositionResolver, offsetFromParent);
    stringIndexAccessor = new StringIndexAccessorImpl(source);
  }

  
  private StringSource(Source parent , Source source , CodePointOffset offsetFromParent) {
    super();
    this.sourceString = source.toString();
    this.root = parent.root();
    if(false == this.root.isRoot()) {
      throw new IllegalArgumentException();
    }
    this.parent = parent;
    this.offsetFromParent = offsetFromParent;
    depth = parent.depth().increments();
    sourceKind = SourceKind.subSource;
//    codePoints = source.codePoints().mapToObj(CodePoint::new).toArray(CodePoint[]::new);
    codePoints = source.codePoints().toArray();
    rootPositionResolver = root.rootPositionResolver();
    subPositionResolver = new SubPositionResolverImpl(codePoints,rootPositionResolver, offsetFromParent);
    stringIndexAccessor = new StringIndexAccessorImpl(source.sourceAsString());

//    Source _root = parent;
//    while(true) {
//      if(_root == null || _root.parent().isEmpty()) {
//        root = _root;
//        break;
//      }
//      _root = parent;
//    }
  }
  
  
  public StringSource(Source parent , String source , CodePointOffset codePointOffset) {
    super();
    Objects.requireNonNull(source,"source require non null");
    this.sourceString = source.toString();
    this.parent = parent;
    this.root = parent.root();
    depth = parent.depth().increments();
    sourceKind = SourceKind.subSource;
    offsetFromParent = codePointOffset;
//    codePoints = source.codePoints().mapToObj(CodePoint::new).toArray(CodePoint[]::new);
    codePoints = source.codePoints().toArray();
    rootPositionResolver = root.rootPositionResolver();
    subPositionResolver = new SubPositionResolverImpl(codePoints,rootPositionResolver, offsetFromParent);
    stringIndexAccessor = new StringIndexAccessorImpl(source);
    
//    Source _root = parent;
//    while(true) {
//      if(_root == null || _root.parent().isEmpty()) {
//        root = _root;
//        break;
//      }
//      _root = parent;
//    }
  }
  
  public LineNumber lineNumberFrom(CodePointIndex codePointIndex) {
    return rootPositionResolver.lineNumberFrom(codePointIndex);
  }

  public StringIndex stringIndexFrom(CodePointIndex codePointIndex) {
    return rootPositionResolver.stringIndexFrom(codePointIndex);
  }

  public CodePointIndex codePointIndexFrom(StringIndex stringIndex) {
    return rootPositionResolver.codePointIndexFrom(stringIndex);
  }

  public CursorRange rootCursorRange() {
    return rootPositionResolver.rootCursorRange();
  }

  public Stream<Source> lines(Source root) {
    return rootPositionResolver.lines(root);
  }

  public Size lineSize() {
    return rootPositionResolver.lineSize();
  }

  public LineNumber subLineNumberFrom(CodePointIndex subCodePointIndex) {
    return subPositionResolver.subLineNumberFrom(subCodePointIndex);
  }

  public StringIndex subStringIndexFrom(CodePointIndex subCodePointIndex) {
    return subPositionResolver.subStringIndexFrom(subCodePointIndex);
  }

  public CodePointIndex subCodePointIndexFrom(StringIndex subStringIndex) {
    return subPositionResolver.subCodePointIndexFrom(subStringIndex);
  }

  static Function<String, Source> stringToStringInterface = string-> StringSource.createRootSource(string);
  static TriFunction<Source , String, CodePointOffset , Source> parentSourceAndStringToSource = 
      (parent,sourceAsString , codePointOffset)-> 
        new StringSource(parent, 
            StringSource.createDetachedSource(sourceAsString,parent.root()),
            codePointOffset);
  static Function<Source, String> stringInterfaceToStgring = StringSource::toString;
  
  @Override
  public TriFunction<Source , String, CodePointOffset , Source> parentSourceAndStringToSource() {
    return parentSourceAndStringToSource;
  }

  @Override
  public Function<Source, String> sourceToStgring() {
    return stringInterfaceToStgring;
  }

  @Override
  public StringLength stringLength() {
    return new StringLength(sourceString.length());
  }
  
  @Override
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
  public int indexOf(int ch, int fromIndex) {
    return sourceString.indexOf(ch,fromIndex);
  }

  @Override
  public int lastIndexOf(int ch, int fromIndex) {
    return sourceString.lastIndexOf(ch, fromIndex);
  }

  @Override
  public int lastIndexOf(String str, int fromIndex) {
    return sourceString.lastIndexOf(str, fromIndex);
  }

  @Override
  public int indexOf(String str, int fromIndex) {
    return sourceString.indexOf(str,fromIndex);
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
    return rootPositionResolver.stringIndexFrom(codePointIndex);
  }

  @Override
  public CodePointIndex toCodePointIndex(StringIndex stringIndex) {
    return rootPositionResolver.codePointIndexFrom(stringIndex);
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
  public Source source() {
    return this;
  }

  @Override
  public int length() {
    return sourceAsString().length();
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
    return stringIndexAccessor;
  }
  
  public static String toString(CodePointAccessor codePointAccessor) {
    
    return codePointAccessor.toString();
  }

  @Override
  public Source peek(CodePointIndex startIndexInclusive, CodePointLength length) {
    
    CodePointOffset offset = new CodePointOffset(startIndexInclusive);
    if(startIndexInclusive.value() + length.value() > codePoints.length){
//      CodePointIndex index = new CodePointIndex(startIndexInclusive.value());
//      CursorRange cursorRange = new CursorRange(new CursorImpl()
//          .setPosition(index)
//          .setLineNumber(lineNUmber(index))
//      );
//      return new StringSource(this , cursorRange , null);
      return new StringSource(this , 
          subSource(startIndexInclusive, new CodePointLength(0)),
          offset);
    }
    
    return new StringSource(this , subSource(startIndexInclusive, length),offset);
  }
  
  @Override
  public Source subSource(CodePointIndex startIndexInclusive, CodePointIndex endIndexExclusive) {
    return new StringSource(this,
        subString(startIndexInclusive,endIndexExclusive),
        new CodePointOffset(startIndexInclusive)
    );
  }
  
  @Override
  public Source subSource(CodePointIndex startIndexInclusive, CodePointLength codePointLength) {
    return new StringSource(this,
        subString(startIndexInclusive,codePointLength),
        new CodePointOffset(startIndexInclusive)
    );
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
  public LineNumber lineNumber(CodePointIndex Position) {
    return rootPositionResolver.lineNumberFrom(Position);
  }

  @Override
  public String sourceAsString() {
    return sourceString;
  }

  @Override
  public Optional<Source> parent() {
    return Optional.ofNullable(parent);
  }

  @Override
  public Source root() {
    return root;
  }

  @Override
  public Source thisSource() {
    return this;
  }

  @Override
  public Depth depth() {
    return depth;
  }

  @Override
  public CursorRange cursorRange() {
    return subPositionResolver.cursorRange();
  }

  @Override
  public CodePointOffset offsetFromParent() {
    
    CodePointOffset offset = offsetFromParent;
    Source _root = parent;
    while(true) {
      if(_root == null || _root.parent().isEmpty()) {
        break;
      }
      _root = parent;
      offset = offset.add(_root.offsetFromParent());
    }
    return offset;
  }

  @Override
  public SourceKind sourceKind() {
    return sourceKind;
  }

  @Override
  public Stream<Source> linesAsSource() {
    return rootPositionResolver.lines(this);
  }

  @Override
  public boolean isRoot() {
    return parent == null && sourceKind == SourceKind.root;
  }

  @Override
  public RootPositionResolver rootPositionResolver() {
    return rootPositionResolver;
  }

}
