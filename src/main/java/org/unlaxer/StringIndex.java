package org.unlaxer;

import org.unlaxer.base.MinIntegerValue._MinIntegerValue;

@_MinIntegerValue(0)
public class StringIndex extends Index{

  public StringIndex(int value) {
    super(value);
  }
  
  public StringIndex increments() {
    return new StringIndex(value+1);
  }
  
  public StringIndex add(int adding) {
    return new StringIndex(value+adding);
  }
  
  public StringIndex minus(int minusing) {
    return new StringIndex(value-minusing);
  }

}