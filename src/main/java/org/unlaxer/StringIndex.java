package org.unlaxer;

import org.unlaxer.base.IntegerValue;

//@_MinIntegerValue(0)
public class StringIndex extends IntegerValue<StringIndex>{

  @Override
  public StringIndex create(int i ) {
    return new StringIndex(i);
  }

  @Override
  public StringIndex create(IntegerValue<?> i) {
    return new StringIndex(i);
  }

  public StringIndex(IntegerValue<?> value) {
    super(value);
  }
  
  public StringIndex(int value) {
    super(value);
  }
}