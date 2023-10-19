package org.unlaxer;

import org.unlaxer.base.MinIntegerValue._MinIntegerValue;

@_MinIntegerValue(0)
public class CodePointLength extends Length{
  
  public CodePointLength(int value) {
    super(value);
  }
  
  public CodePointLength increments() {
    return new CodePointLength(value+1);
  }
  
  public CodePointLength add(int adding) {
    return new CodePointLength(value+adding);
  }
}