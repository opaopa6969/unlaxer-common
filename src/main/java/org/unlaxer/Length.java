package org.unlaxer;

import org.unlaxer.base.IntegerValue;
import org.unlaxer.base.MinIntegerValue._MinIntegerValue;

@_MinIntegerValue(0)
public class Length extends IntegerValue{

  public Length(int value) {
    super(value);
  }
  
  public Length increments() {
    return new Length(value+1);
  }
  
  public Length add(int adding) {
    return new Length(value+adding);
  }

}