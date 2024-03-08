package org.unlaxer.base;

import java.io.Serializable;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class IntegerValue<T extends IntegerValue<T>>  
implements Comparable<T> , Serializable , MinIntegerValue , MaxIntegerValue , Nullable , MinLength , MaxLength, IntegerValueInterface<T>{

  private final int value;
  
  public IntegerValue(IntegerValue<?> value) {
    this(value.value);
  }

  public IntegerValue(int value) {
    super();
    this.value = value;
    if(minIntegerValue() > value || maxIntegerValue() <value) {
      throw new IllegalArgumentException(
        "value is out of range(" + minIntegerValue() + " - " + maxIntegerValue() + "):" + value);
    }
    
    int numberOfDigits = (int) ((value == 0 ? 0 :Math.log10(value))+1);
    if(minLength() > numberOfDigits|| maxLength() < numberOfDigits) {
      throw new IllegalArgumentException(
        "number of value's digits is out of range(" + minLength() + " - " + maxLength() + "):" + value);
    }
  }
  
  public int value() {
    return value;
  }

  @Override
  public int compareTo(T other) {
    return value - other.value();
  }

  @Override
  public int hashCode() {
    return value;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    @SuppressWarnings("unchecked")
    T other = (T) obj;
    if (value != other.value())
      return false;
    return true;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @Override
  public final boolean nullable() {
    return false;
  }
  
  @Override
  public T newWithIncrements() {
    return create(value+1);
  }
  
  @Override
  public T newWithDecrements() {
    return create(value-1);
  }
  
  @Override
  public T newWithAdd(int adding) {
    return create(value+adding);
  }
  
  @Override
  public T newWithPlus(int adding) {
    return create(value+adding);
  }
  
  @Override
  public T newWithMinus(int minusing) {
    return create(value-minusing);
  }
  
  @Override
  public T newWithMultiply(int multiplying) {
    return create(value*multiplying);
  }
  
  @Override
  public T newWithDivide(int dividing) {
    return create(value/dividing);
  }
  
  @Override
  public T newWithAdd(IntegerValue<?> adding) {
    return create(value+adding.value);
  }
  
  @Override
  public T newWithPlus(IntegerValue<?> adding) {
    return create(value+adding.value);
  }

  
  @Override
  public T newWithMinus(IntegerValue<?> minusing) {
    return create(value-minusing.value);
  }
  
  @Override
  public T newWithMultiply(IntegerValue<?> multiplying) {
    return create(value*multiplying.value);
  }
  
  @Override
  public T newWithDivide(IntegerValue<?> dividing) {
    return create(value/dividing.value);
  }

  @Override
  public boolean greaterThean(IntegerValue<?> right) {
    return gt(right);
  }

  @Override
  public boolean gt(IntegerValue<?> right) {
    return value() > right.value();
  }

  @Override
  public boolean greaterEqual(IntegerValue<?> right) {
    return ge(right);
  }

  @Override
  public boolean ge(IntegerValue<?> right) {
    return value() >= right.value();
  }
  
  @Override
  public boolean lessEquals(IntegerValue<?> right) {
    return lt(right);
  }
  
  @Override
  public boolean le(IntegerValue<?> right) {
    return value() <= right.value();
  }
  
  @Override
  public boolean lessThan(IntegerValue<?> right) {
    return lt(right);
  }
  
  @Override
  public boolean lt(IntegerValue<?> right) {
    return value() < right.value();
  }
  
  @Override
  public boolean equals(IntegerValue<?> right) {
    return eq(right);
  }
  
  @Override
  public boolean eq(IntegerValue<?> right) {
    return value() == right.value();
  }
  
  public boolean notEqual(IntegerValue<?> right) {
    return ne(right);
  }
  
  @Override
  public boolean ne(IntegerValue<?> right) {
    return value() != right.value();
  }
  
  @Override
  public boolean gt(int right) {
    return value() > right;
  }
  
  @Override
  public boolean greaterThean(int right) {
    return gt(right);
  }

  @Override
  public boolean greaterEqual(int right) {
    return ge(right);
  }
  
  @Override
  public boolean ge(int right) {
    return value() >= right;
  }
  
  @Override
  public boolean lessEquals(int right) {
    return lt(right);
  }
  
  @Override
  public boolean le(int right) {
    return value() <= right;
  }
  
  @Override
  public boolean lessThan(int right) {
    return lt(right);
  }
  @Override
  public boolean lt(int right) {
    return value() < right;
  }
  
  @Override
  public boolean equals(int right) {
    return eq(right);
  }

  @Override
  public boolean eq(int right) {
    return value() == right;
  }

  public boolean notEqual(int right) {
    return ne(right);
  }

  @Override
  public boolean ne(int right) {
    return value() != right;
  }
  
  @Override
  public boolean isNegative() {
    return value<0;
  }
  
  @Override
  public boolean isPositive() {
    return value>=0;
  }
  
  @Override
  public boolean isGreaterThanZero() {
    return value>0;
  }

  
  @Override
  public boolean isZero() {
    return value==0;
  }
  
  public abstract T create(int i);
  @Override
  public abstract T create(IntegerValue<?> i);
  
  @Override
  @SuppressWarnings("unchecked")
  public T createIfMatch(Predicate<T> predicate , Supplier<T> supplier) {
    
    if(predicate.test((T)this)) {
      
      return supplier.get();
    }
    return (T)this;
  }
}