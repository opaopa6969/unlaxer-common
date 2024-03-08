package org.unlaxer;

import org.unlaxer.Source.SourceKind;
import org.unlaxer.base.IntegerValue;

public class AttachedCodePointIndex extends IntegerValue<AttachedCodePointIndex> implements CodePointIndexFromRoot , CodePointIndexFromParent , CodePointIndexInterface{
  
  Source attachedSource;

  public AttachedCodePointIndex(int value , Source source) {
    super(value);
    this.attachedSource = source;
  }
  
  public AttachedCodePointIndex(IntegerValue<?> value  , Source source) {
    super(value);
    this.attachedSource = source;
  }
  
  public AttachedCodePointIndex create(int i , Source source) {
    return new AttachedCodePointIndex(i,source);
  }

  public AttachedCodePointIndex create(IntegerValue<?> i  , Source source) {
    return new AttachedCodePointIndex(i,source);
  }

  @Override
  public AttachedCodePointIndex create(IntegerValue<?> i) {
    throw new UnsupportedOperationException("Unimplemented method 'create(i)'");
  }

  public int indexFromParent() {
    
    return attachedSource.offsetFromParent().newWithAdd(value()).value();
  }
  
  public int indexFromRoot() {
    
    return attachedSource.offsetFromRoot().newWithAdd(value()).value();
  }
  
  @Override
  public AttachedCodePointIndex create(int i) {
    throw new UnsupportedOperationException("must specify source");
  }

  @Override
  public int rawValue() {
    return value();
  }

  @Override
  public IndexKind indexKind() {
    SourceKind sourceKind = attachedSource.sourceKind();
    if(sourceKind == SourceKind.root) {
      return IndexKind.fromRoot;
    }
    return IndexKind.fromParent;
  }

  @Override
  public boolean isPresent(IndexKind indexKind) {
    return true;
  }

  @Override
  public int value(IndexKind indexKind) {
    
    switch (indexKind) {
      case fromParent:
        return indexFromParent();
      case fromRoot:
        return indexFromRoot();
      case thisSource:
        return value();
      default:
        throw new IllegalArgumentException();
    }
  }
}