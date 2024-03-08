package org.unlaxer;

import org.unlaxer.base.IntegerValue;
import org.unlaxer.base.MinIntegerValue._MinIntegerValue;

@_MinIntegerValue(0)
public class StringIndex extends IntegerValue<StringIndex>{
  
  public final IndexKind indexKind;

  
  public StringIndex create(int i , IndexKind indexKind) {
    return new StringIndex(i , indexKind);
  }

  public StringIndex create(IntegerValue<?> i , IndexKind indexKind) {
    return new StringIndex(i , indexKind);
  }

  public StringIndex(IntegerValue<?> value, IndexKind indexKind) {
    super(value);
    this.indexKind = indexKind;
  }
  
  public StringIndex(int value, IndexKind indexKind) {
    super(value);
    this.indexKind = indexKind;
  }

  public IndexKind getIndexKind() {
    return indexKind;
  }

  @Override
  public StringIndex create(int i) {
    throw new UnsupportedOperationException("must specify index kind");
  }

  @Override
  public StringIndex create(IntegerValue<?> i) {
    throw new UnsupportedOperationException("must specify index kind");
  }

}