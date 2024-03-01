package org.unlaxer;

import org.unlaxer.Source.SourceKind;
import org.unlaxer.base.IntegerValue;

public class CodePointIndexWithNegativeValue extends IntegerValue<CodePointIndexWithNegativeValue>{
  
  final CodePointOffset offsetFromRoot;

  public CodePointIndexWithNegativeValue(int value , CodePointOffset offsetFromRoot) {
    super(value);
    this.offsetFromRoot = offsetFromRoot;
  }
  
  public CodePointIndexWithNegativeValue(IntegerValue<?> value , CodePointOffset offsetFromRoot) {
    super(value);
    this.offsetFromRoot = offsetFromRoot;
  }
  
  public CodePointIndexWithNegativeValue(CodePointIndex codePointIndex) {
    super(codePointIndex.valueInSubSource());
    this.offsetFromRoot = codePointIndex.offsetFromRoot;
  }

  
  /**
   * @return CodePointIndex
   * @throws if code less than 0 then throw IllegalArgumentException
   */
  public CodePointOffset toCodePointOffsetWithOffsetFromRoot() {
    return new CodePointOffset(super.value()+offsetFromRoot.value());
  }
  
  public CodePointOffset toCodePointOffsetInSubSource() {
    return new CodePointOffset(super.value());
  }

  
  @Override
  public CodePointIndexWithNegativeValue create(int i) {
    return new CodePointIndexWithNegativeValue(i,offsetFromRoot);
  }

  @Override
  @Deprecated
  public CodePointIndexWithNegativeValue create(IntegerValue<?> i) {
    return new CodePointIndexWithNegativeValue(i,offsetFromRoot);
  }
  
  @Override
  @Deprecated
  public int value() {
    throw new UnsupportedOperationException("must specify offset");
  }
  
  public CodePointOffset offsetFromRoot() {
    return offsetFromRoot;
  }

  public int valueWithOffsetFromRoot() {
    return super.value()+offsetFromRoot().value();
  }
  
  public int valueInSubSource() {
    return super.value();
  }
  public int value(SourceKind sourceKind) {
    
    return sourceKind.isSubSource() ? 
        valueInSubSource():
        valueWithOffsetFromRoot();
  }
  
  public int value(Source source) {
    
    return source.sourceKind().isSubSource() ? 
        valueInSubSource():
        valueWithOffsetFromRoot();
  }
}