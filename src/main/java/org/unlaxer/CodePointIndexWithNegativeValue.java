package org.unlaxer;

public class CodePointIndexWithNegativeValue extends CodePointIndex{

  public CodePointIndexWithNegativeValue(int value) {
    super(value);
  }
  
  public CodePointIndexWithNegativeValue(CodePointIndex codePointIndex) {
    super(codePointIndex.value);
  }
  
  public CodePointIndexWithNegativeValue increments() {
    return new CodePointIndexWithNegativeValue(value+1);
  }
  
  public CodePointIndexWithNegativeValue add(int adding) {
    return new CodePointIndexWithNegativeValue(value+adding);
  }
  
  public boolean isNegative() {
    return value < 0;
  }
  
  /**
   * @return CodePointIndex
   * @throws if code less than 0 then throw IllegalArgumentException
   */
  public CodePointIndex toCodePointIndex() {
    return new CodePointIndex(value);
  }
}