package org.unlaxer;

import org.unlaxer.base.IntegerValue;
import org.unlaxer.base.MinIntegerValue._MinIntegerValue;

@_MinIntegerValue(0)
public class Count extends IntegerValue{

  public Count(int value) {
    super(value);
  }
  
  public Count increments() {
    return new Count(value+1);
  }
  
  public Count add(int adding) {
    return new Count(value+adding);
  }

}