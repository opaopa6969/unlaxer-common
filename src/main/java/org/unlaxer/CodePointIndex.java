package org.unlaxer;

public class CodePointIndex extends Index{

  public CodePointIndex(int value) {
    super(value);
  }
  
  public CodePointIndex increments() {
    return new CodePointIndex(value+1);
  }
  
  public CodePointIndex add(int adding) {
    return new CodePointIndex(value+adding);
  }
}