package org.unlaxer;

import org.unlaxer.Source.SourceKind;
import org.unlaxer.base.IntegerValue;

public class AttachedCodePointIndex extends IntegerValue<AttachedCodePointIndex>{
  
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

  public AttachedCodePointIndex(int value) {
    super(value);
  }
  
  public AttachedCodePointIndex(IntegerValue<?> value) {
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
  
  public AttachedCodePointIndex create(int i) {
    return new AttachedCodePointIndex(i);
  }

  @Override
  public AttachedCodePointIndex create(IntegerValue<?> i) {
    return new AttachedCodePointIndex(i);
  }

//  public SourceKind sourceKind() {
//    return sourceKind;
//  }
  
  public int indexFromParentvalue() {
    
    return attachedSource.offsetFromParent().newWithAdd(value()).value();
  }
  
  public int indexFromRoot() {
    
    return attachedSource.offsetFromRoot().newWithAdd(value()).value();
  }
  
  public CodePointOffset codePointOffsetFromRoot() {
    
    CodePointOffset newWithAdd = attachedSource.offsetFromRoot().newWithAdd(value());
  }
  
}