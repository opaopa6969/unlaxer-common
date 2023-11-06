package org.unlaxer;

import org.unlaxer.base.IntegerValue;

public class SubCodePointIndex extends IntegerValue<SubCodePointIndex>{

  public SubCodePointIndex(int value) {
    super(value);
  }
  
  public SubCodePointIndex(IntegerValue<?> value) {
    super(value);
  }

  @Override
  public SubCodePointIndex create(int i) {
    return new SubCodePointIndex(i);
  }

  @Override
  public SubCodePointIndex create(IntegerValue<?> i) {
    return new SubCodePointIndex(i);
  }
  
}