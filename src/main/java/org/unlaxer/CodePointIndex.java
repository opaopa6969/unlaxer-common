package org.unlaxer;

import org.unlaxer.base.IntegerValue;

public class CodePointIndex extends IntegerValue<CodePointIndex> implements CodePointIndexInterface{

//  SourceKind sourceKind;
//  
//  public CodePointIndex(int value , SourceKind sourceKind) {
//    super(value);
//    this.sourceKind = sourceKind;
//  }
//  
//  public CodePointIndex(IntegerValue<?> value , SourceKind sourceKind) {
//    super(value);
//    this.sourceKind = sourceKind;
//  }
  
  public CodePointIndex(int value) {
    super(value);
  }
  
  public CodePointIndex(IntegerValue<?> value) {
    super(value);
  }
  
//  @Override
//  public CodePointIndex create(int i) {
//    return new CodePointIndex(i ,sourceKind);
//  }
//
//  @Override
//  public CodePointIndex create(IntegerValue<?> i) {
//    return new CodePointIndex(i ,sourceKind);
//  }
  
  public CodePointIndex create(int i) {
    return new CodePointIndex(i);
  }

  @Override
  public CodePointIndex create(IntegerValue<?> i) {
    return new CodePointIndex(i);
  }

  public CodePointOffset toCodePointOffset() {
    return new CodePointOffset(value());
  }
  
  public CodePointIndexInLine toCodePointIndexInLine() {
    return new CodePointIndexInLine(value());
  }

  @Override
  public int rawValue() {
    return value();
  }

//  public SourceKind sourceKind() {
//    return sourceKind;
//  }

}