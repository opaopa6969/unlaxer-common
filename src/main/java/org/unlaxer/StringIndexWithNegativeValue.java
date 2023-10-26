package org.unlaxer;

public class StringIndexWithNegativeValue extends Index{

  public StringIndexWithNegativeValue(int value) {
    super(value);
  }
  
  public StringIndexWithNegativeValue(StringIndex stringIndex) {
    super(stringIndex.value);
  }
  
  public StringIndexWithNegativeValue increments() {
    return new StringIndexWithNegativeValue(value+1);
  }
  
  public StringIndexWithNegativeValue add(int adding) {
    return new StringIndexWithNegativeValue(value+adding);
  }
  
  public StringIndexWithNegativeValue minus(int minusing) {
    return new StringIndexWithNegativeValue(value-minusing);
  }
  
  public StringIndex toStringIndex() {
    return new StringIndex(value);
  }
  
  public boolean isNegative() {
    
    return value <0;
  }
}