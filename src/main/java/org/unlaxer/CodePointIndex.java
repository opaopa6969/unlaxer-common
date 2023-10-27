package org.unlaxer;

import org.unlaxer.base.IntegerValue;

public class CodePointIndex extends IntegerValue<CodePointIndex>{

  public CodePointIndex(int value) {
    super(value);
  }
  
  public CodePointIndex(IntegerValue<?> value) {
    super(value);
  }

  @Override
  public CodePointIndex create(int i) {
    return new CodePointIndex(i);
  }

  @Override
  public CodePointIndex create(IntegerValue<?> i) {
    return new CodePointIndex(i);
  }
  
}