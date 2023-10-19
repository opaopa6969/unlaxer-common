package org.unlaxer;

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
}