package org.unlaxer;

import org.unlaxer.base.IntegerValue;

public class CodePointIndex extends IntegerValue<CodePointIndex> implements CodePointIndexFromRoot<CodePointIndex> , CodePointIndexFromParent<CodePointIndex>{

  
  Source attachedSource;

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
  
  public CodePointIndex(int value , Source source) {
    super(value);
    this.attachedSource = source;
  }
  
  public CodePointIndex(IntegerValue<?> value , Source source) {
    super(value);
    this.attachedSource = source;
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
  
//  public CodePointIndex create(int i) {
//    return new CodePointIndex(i);
//  }
//
//  @Override
//  public CodePointIndex create(IntegerValue<?> i) {
//    return new CodePointIndex(i);
//  }

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

  @Override
  public IndexKind indexKind() {
    
    return IndexKind.thisSource;
  }

  @Override
  public boolean isPresent(IndexKind indexKind) {
    return indexKind.isThisSource();
  }

  @Override
  public int value(IndexKind indexKind) {
    switch (indexKind) {
      case parent:
        return indexFromParent();
      case root:
        return indexFromRoot();
      case thisSource:
        return value();
      default:
        throw new IllegalArgumentException();
    }
  }

  @Override
  public int indexFromParent() {
    return attachedSource.offsetFromParent().newWithAdd(value()).value();
  }

  @Override
  public int indexFromRoot() {
    
    AttachedCodePointIndex newWithAdd2 = attachedSource.offsetFromRoot().newWithAdd(value());
    return newWithAdd2.value();

  }

  @Override
  public CodePointIndex create(int i) {
    throw new UnsupportedOperationException("Unimplemented method 'create(i)'");
  }

  @Override
  public CodePointIndex create(IntegerValue<?> i) {
    throw new UnsupportedOperationException("Unimplemented method 'create(i)'");
  }
}