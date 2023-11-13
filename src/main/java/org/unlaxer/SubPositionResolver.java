package org.unlaxer;

public interface SubPositionResolver extends 
  SubCodePointIndexToStringIndex , 
  SubCodePointIndexToLineNumber , 
  SubStringIndexToCodePointIndex{
  
  /**
   * @return cursorRange for subSource. start position is 0
   */
  CursorRange subCursorRange();
}