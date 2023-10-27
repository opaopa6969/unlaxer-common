package org.unlaxer;

import org.unlaxer.base.IntegerValue;

public class CodePointOffset extends IntegerValue<CodePointOffset>{

  public CodePointOffset(int value) {
    super(value);
  }
  
  public CodePointOffset(IntegerValue<?> value) {
    super(value);
  }

  @Override
  public CodePointOffset create(int i) {
    return new CodePointOffset(i);
  }

  @Override
  public CodePointOffset create(IntegerValue<?> i) {
    return new CodePointOffset(i);
  }
}