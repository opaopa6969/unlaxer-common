package org.unlaxer;

public class StringIndexWithNegativeValue extends Index{

  public StringIndexWithNegativeValue(int value) {
    super(value);
  }
  
  public StringIndexWithNegativeValue increments() {
    return new StringIndexWithNegativeValue(value+1);
  }
  
  public StringIndexWithNegativeValue add(int adding) {
    return new StringIndexWithNegativeValue(value+adding);
  }
  
  public StringIndex toStringIndex() {
    return new StringIndex(value);
  }
}