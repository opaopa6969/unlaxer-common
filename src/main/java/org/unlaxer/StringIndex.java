package org.unlaxer;

import org.unlaxer.base.IntegerValue;
import org.unlaxer.base.MinIntegerValue._MinIntegerValue;

@_MinIntegerValue(0)
public class StringIndex extends IntegerValue<StringIndex>{
  
  public final Source targetSource;
  
  public StringIndex create(int i , Source targetSource) {
    return new StringIndex(i , targetSource);
  }

  public StringIndex create(IntegerValue<?> i , Source targetSource) {
    return new StringIndex(i , targetSource);
  }

  public StringIndex(IntegerValue<?> value, Source targetSource) {
    super(value);
    this.targetSource =targetSource;
  }
  
  public StringIndex(int value, Source targetSource) {
    super(value);
    this.targetSource = targetSource;
  }
  public Source getTargetSource() {
    return targetSource;
  }

  @Override
  public StringIndex create(int i) {
    throw new UnsupportedOperationException("must specify target source");
  }

  @Override
  public StringIndex create(IntegerValue<?> i) {
    throw new UnsupportedOperationException("must specify tsource");
  }

}