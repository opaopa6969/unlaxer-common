package org.unlaxer;

import java.util.stream.Stream;

/**
 * position resolver use position related root source
 */
public interface PositionResolver {
  
  StringIndex stringIndexFrom(CodePointIndex codePointIndex);
  CodePointIndexInLine codePointIndexInLineFrom(CodePointIndex codePointIndex);
  LineNumber lineNumberFrom(CodePointIndex codePointIndex);
  CodePointIndex codePointIndexFrom(StringIndex stringIndex);
//  StringIndex subStringIndexFrom(CodePointIndex codePointIndex);
//  CodePointIndex subCodePointIndexFrom(StringIndex subStringIndex);
  
//  CodePointOffset offsetFromRoot();
  
  /**
   * @return cursorRange for rootSource.
   */
  CursorRange rootCursorRange();
  
//  /**
//   * @return cursorRange for subSource. start position is 0
//   */
//  CursorRange subCursorRange();
  
  

  Stream<Source> lines(Source root);

  Size lineSize();
  
  public static PositionResolver createPositionResolver(Source source , int[] codePoints){
    return new PositionResolverImpl(source , codePoints);
  }
  
}