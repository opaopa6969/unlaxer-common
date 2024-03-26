package org.unlaxer;

import java.util.function.Supplier;

import org.unlaxer.base.IntegerValue;

//@_MinIntegerValue(0)
public class StringIndex extends IntegerValue<StringIndex>{
  
  final CodePointIndex codePointIndex;
  final Supplier<CodePointIndex> codePointIndexSupplier;

  @Override
  public StringIndex create(int i ) {
    throw new UnsupportedOperationException();
  }

  @Override
  public StringIndex create(IntegerValue<?> i) {
    throw new UnsupportedOperationException();
  }

  private StringIndex(IntegerValue<?> value) {
    super(value);
    throw new IllegalArgumentException();
  }
  
  private StringIndex(int value) {
    super(value);
    throw new IllegalArgumentException();
  }
  
  public StringIndex(IntegerValue<?> value , CodePointIndex codePointIndex) {
    super(value);
    this.codePointIndex = codePointIndex;
    this.codePointIndexSupplier =  null;
  }
  
  public StringIndex(IntegerValue<?> value , Supplier<CodePointIndex> codePointIndex) {
    super(value);
    this.codePointIndex = null;
    this.codePointIndexSupplier = codePointIndex;
  }

  
  public StringIndex(int value, CodePointIndex codePointIndex) {
    super(value);
    this.codePointIndex = codePointIndex;
    this.codePointIndexSupplier =  null;
  }

  
  public CodePointIndex relatedCodePointIndex(){
    if(codePointIndexSupplier != null) {
      return codePointIndexSupplier.get();
    }
    return codePointIndex;
  }
}