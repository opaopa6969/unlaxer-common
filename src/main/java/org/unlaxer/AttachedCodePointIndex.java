package org.unlaxer;

import org.unlaxer.base.IntegerValue;

public class AttachedCodePointIndex extends IntegerValue<AttachedCodePointIndex> implements CodePointIndexFromRoot , CodePointIndexFromParent{
  
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
  
//  public CodePointOffset codePointOffsetFromRoot() {
//    
//    CodePointOffset newWithAdd = attachedSource.offsetFromRoot().newWithAdd(value());
//  }

  @Override
  public AttachedCodePointIndex create(int i) {
    throw new UnsupportedOperationException("Unimplemented method 'create'");
  }

}