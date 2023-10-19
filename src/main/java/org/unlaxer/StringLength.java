package org.unlaxer;

import org.unlaxer.base.MinIntegerValue._MinIntegerValue;

@_MinIntegerValue(0)
public class StringLength extends Length{
  
  public StringLength(int value) {
    super(value);
  }
  
  public StringLength increments() {
    return new StringLength(value+1);
  }
  
  public StringLength add(int adding) {
    return new StringLength(value+adding);
  }
}