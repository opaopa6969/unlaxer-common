package org.unlaxer;

import org.unlaxer.Source.SourceKind;
import org.unlaxer.base.IntegerValue;

public class AttachedCodePointIndex_ extends IntegerValue<AttachedCodePointIndex_> implements CodePointIndexFromRoot<AttachedCodePointIndex_> , CodePointIndexFromParent<AttachedCodePointIndex_>{
  
  Source attachedSource;

  public AttachedCodePointIndex_(int value , Source source) {
    super(value);
    this.attachedSource = source;
  }
  
  public AttachedCodePointIndex_(IntegerValue<?> value  , Source source) {
    super(value);
    this.attachedSource = source;
  }
  
  public AttachedCodePointIndex_ create(int i , Source source) {
    return new AttachedCodePointIndex_(i,source);
  }

  public AttachedCodePointIndex_ create(IntegerValue<?> i  , Source source) {
    return new AttachedCodePointIndex_(i,source);
  }

  @Override
  public AttachedCodePointIndex_ create(IntegerValue<?> i) {
    throw new UnsupportedOperationException("Unimplemented method 'create(i)'");
  }

  public int indexFromParent() {
    
    return attachedSource.offsetFromParent().newWithAdd(value()).value();
  }
  
  public int indexFromRoot() {
    CodePointOffset newWithAdd = attachedSource.offsetFromRoot().newWithAdd(value());
    return newWithAdd.value();
  }
  
  @Override
  public AttachedCodePointIndex_ create(int i) {
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
      return IndexKind.root;
    }
    return IndexKind.parent;
  }

  @Override
  public boolean isPresent(IndexKind indexKind) {
    return true;
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
}