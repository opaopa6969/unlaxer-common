package org.unlaxer;

import java.util.stream.Stream;

/**
 * position resolver use position related root source
 */
public interface PositionResolver {
  
  StringIndex stringIndexInRootFrom(CodePointIndexFromRoot<?> CodePointIndex);
  CodePointIndexInLine codePointIndexInLineFrom(CodePointIndexFromRoot<?> rootCodePointIndex);
  LineNumber lineNumberFrom(CodePointIndexFromRoot<?> rootCodePointIndex);
  AttachedCodePointIndex rootCodePointIndexFrom(StringIndex stringIndex);
  
  
  StringIndex subStringIndexFrom(CodePointIndexFromRoot<?> subCodePointIndex);
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
  
  public Source rootSource();

}