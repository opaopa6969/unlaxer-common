package org.unlaxer;

import org.unlaxer.base.IntegerValue;
import org.unlaxer.base.MinIntegerValue._MinIntegerValue;

@_MinIntegerValue(0)
public class LineNumber extends IntegerValue{

  public LineNumber(int value) {
    super(value);
  }
  
  public LineNumber increments() {
    return new LineNumber(value+1);
  }
  
  public LineNumber add(int adding) {
    return new LineNumber(value+adding);
  }
}