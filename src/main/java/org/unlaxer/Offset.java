package org.unlaxer;

import org.unlaxer.base.IntegerValue;

public class Offset extends IntegerValue{

  public Offset(int value) {
    super(value);
  }
  
  public Index add(int adding) {
    return new Index(value+adding);
  }
}