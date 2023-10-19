package org.unlaxer;

import org.unlaxer.base.IntegerValue;
import org.unlaxer.base.MinIntegerValue._MinIntegerValue;

@_MinIntegerValue(0)
public class Index extends IntegerValue{

  public Index(int value) {
    super(value);
  }
  
  public Index increments() {
    return new Index(value+1);
  }
  
  public Index add(int adding) {
    return new Index(value+adding);
  }

}