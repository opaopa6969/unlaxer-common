package org.unlaxer;

import java.util.function.Predicate;
import java.util.function.Supplier;

import org.unlaxer.Source.SourceKind;
import org.unlaxer.base.IntegerValue;

public class CodePointIndex extends IntegerValue<CodePointIndex>{
  
  final CodePointOffset offsetFromRoot;

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

  public CodePointIndex(int value , CodePointOffset offsetFromRoot) {
    super(value);
    this.offsetFromRoot = offsetFromRoot;
  }
  
  public CodePointIndex(IntegerValue<?> value , CodePointOffset offsetFromRoot) {
    super(value);
    this.offsetFromRoot = offsetFromRoot;
  }
  
  public CodePointIndex(IntegerValue<?> value , Source source) {
    super(value);
    this.offsetFromRoot = source.offsetFromRoot();
  }
  
  public CodePointIndex(CodePointIndex codePointIndex) {
    super(codePointIndex.valueInSubSource());
    this.offsetFromRoot = codePointIndex.offsetFromRoot;
  }
  
  public CodePointIndex(CodePointIndexWithNegativeValue codePointIndex) {
    super(codePointIndex.valueInSubSource());
    this.offsetFromRoot = codePointIndex.offsetFromRoot;
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
  
  @Override
  public CodePointIndex create(int i) {
    return new CodePointIndex(i , offsetFromRoot);
  }

  @Override
  public CodePointIndex create(IntegerValue<?> i) {
    return new CodePointIndex(i , offsetFromRoot);
  }

  public CodePointOffset toCodePointOffsetWithOffsetFromRoot() {
    return new CodePointOffset(super.value()+offsetFromRoot.value());
  }
  
  public CodePointOffset toCodePointOffsetInSubSource() {
    return new CodePointOffset(super.value());
  }

  
  public CodePointIndexInLine toCodePointIndexInLineWithOffsetFromRoot() {
    return new CodePointIndexInLine(super.value()+offsetFromRoot.value());
  }
  
  public CodePointOffset offsetFromRoot() {
    return offsetFromRoot;
  }

  @Override
  @Deprecated
  public int value() {
    throw new UnsupportedOperationException("must specify offset");
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

//  public SourceKind sourceKind() {
//    return sourceKind;
//  }
  
  public int valueWithOffsetFromRoot() {
    return super.value()+offsetFromRoot().value();
  }
  
  public int valueInSubSource() {
    return super.value();
  }
  
  @Override
  public int compareTo(CodePointIndex other) {
    return value(SourceKind.root) - other.value(SourceKind.root);
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CodePointIndex other = (CodePointIndex) obj;
    if (rawValue() != other.rawValue())
      return false;
    return true;
  }
  
  public CodePointIndex newWithIncrements() {
    return create(rawValue()+1);
  }
  
  public CodePointIndex newWithDecrements() {
    return create(rawValue()-1);
  }
  
  public CodePointIndex newWithAdd(int adding) {
    return create(rawValue()+adding);
  }
  
  public CodePointIndex newWithPlus(int adding) {
    return create(rawValue()+adding);
  }
  
  public CodePointIndex newWithMinus(int minusing) {
    return create(rawValue()-minusing);
  }
  
  public CodePointIndex newWithMultiply(int multiplying) {
    return create(rawValue()*multiplying);
  }
  
  public CodePointIndex newWithDivide(int dividing) {
    return create(rawValue()/dividing);
  }
  
  public CodePointIndex newWithAdd(IntegerValue<?> adding) {
    return create(rawValue()+adding.rawValue());
  }
  
  public CodePointIndex newWithPlus(IntegerValue<?> adding) {
    return create(rawValue()+adding.rawValue());
  }

  
  public CodePointIndex newWithMinus(IntegerValue<?> minusing) {
    return create(rawValue()-minusing.rawValue());
  }
  
  public CodePointIndex newWithMultiply(IntegerValue<?> multiplying) {
    return create(rawValue()*multiplying.rawValue());
  }
  
  public CodePointIndex newWithDivide(IntegerValue<?> dividing) {
    return create(rawValue()/dividing.rawValue());
  }

  public boolean greaterThean(IntegerValue<?> right) {
    return gt(right);
  }

  public boolean gt(IntegerValue<?> right) {
    return rawValue() > right.rawValue();
  }

  public boolean greaterEqual(IntegerValue<?> right) {
    return ge(right);
  }

  public boolean ge(IntegerValue<?> right) {
    return rawValue() >= right.rawValue();
  }
  
  public boolean lessEquals(IntegerValue<?> right) {
    return lt(right);
  }
  
  public boolean le(IntegerValue<?> right) {
    return rawValue() <= right.rawValue();
  }
  
  public boolean lessThan(IntegerValue<?> right) {
    return lt(right);
  }
  
  public boolean lt(IntegerValue<?> right) {
    return rawValue() < right.rawValue();
  }
  
  public boolean equals(IntegerValue<?> right) {
    return eq(right);
  }
  
  public boolean eq(IntegerValue<?> right) {
    return rawValue() == right.rawValue();
  }
  
  public boolean notEqual(IntegerValue<?> right) {
    return ne(right);
  }
  
  public boolean ne(IntegerValue<?> right) {
    return rawValue() != right.rawValue();
  }
  
  public boolean gt(int right) {
    return rawValue() > right;
  }
  
  public boolean greaterThean(int right) {
    return gt(right);
  }

  public boolean greaterEqual(int right) {
    return ge(right);
  }
  
  public boolean ge(int right) {
    return rawValue() >= right;
  }
  
  public boolean lessEquals(int right) {
    return lt(right);
  }
  
  public boolean le(int right) {
    return rawValue() <= right;
  }
  
  public boolean lessThan(int right) {
    return lt(right);
  }
  public boolean lt(int right) {
    return rawValue() < right;
  }
  
  public boolean equals(int right) {
    return eq(right);
  }

  public boolean eq(int right) {
    return rawValue() == right;
  }

  public boolean notEqual(int right) {
    return ne(right);
  }

  public boolean ne(int right) {
    return rawValue() != right;
  }
  
  public boolean isNegative() {
    return rawValue()<0;
  }
  
  public boolean isPositive() {
    return rawValue()>=0;
  }
  
  public boolean isGreaterThanZero() {
    return rawValue()>0;
  }

  
  public boolean isZero() {
    return rawValue()==0;
  }
  
  public CodePointIndex createIfMatch(Predicate<CodePointIndex> predicate , Supplier<CodePointIndex> supplier) {
    
    if(predicate.test(this)) {
      
      return supplier.get();
    }
    return this;
  }


}