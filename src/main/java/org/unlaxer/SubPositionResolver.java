package org.unlaxer;

public interface SubPositionResolver extends 
  SubCodePointIndexToStringIndex , 
  SubCodePointIndexToLineNumber , 
  SubStringIndexToCodePointIndex{
  
  /**
   * @return cursorRange for subSource. start position is 0
   */
  CursorRange subCursorRange();
  
  public static SubPositionResolver createSubPositionReslover(
      int[] codePoints,
      RootPositionResolver rootPositionResolver,
      CodePointOffset offsetFromRoot){
   return new PositionResolverImpl(codePoints, rootPositionResolver, offsetFromRoot);
  }
}