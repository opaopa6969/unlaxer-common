package org.unlaxer;

public interface SubPositionResolver extends 
SubCodePointIndexToStringIndex , 
SubCodePointIndexToLineNumber , SubStringIndexToCodePointIndex{
  CursorRange subCursorRange();
}