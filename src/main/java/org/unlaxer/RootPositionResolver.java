package org.unlaxer;

import java.util.stream.Stream;

public interface RootPositionResolver extends 
  CodePointIndexToStringIndex ,
  CodePointIndexToCodePointIndexInLine,
  CodePointIndexToLineNumber ,
  StringIndexToCodePointIndex{
  
  /**
   * @return cursorRange for rootSource.
   */
  CursorRange rootCursorRange();

  Stream<Source> lines(Source root);

  Size lineSize();
}