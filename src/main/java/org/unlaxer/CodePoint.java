package org.unlaxer;

import org.unlaxer.base.IntegerValue;
import org.unlaxer.base.MaxIntegerValue._MaxIntegerValue;
import org.unlaxer.base.MinIntegerValue._MinIntegerValue;

@_MinIntegerValue(0)
@_MaxIntegerValue(0x10FFFF)
public class CodePoint extends IntegerValue{

  public CodePoint(int value) {
    super(value);
  }
  
  public String toString() {
    return Character.toString(value);
  }
  
  public char[] toChars() {
    return Character.toChars(value);
  }

}