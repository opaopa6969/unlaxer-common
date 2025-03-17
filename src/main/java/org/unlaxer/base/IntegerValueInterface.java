package org.unlaxer.base;

import java.util.function.Predicate;
import java.util.function.Supplier;

public interface IntegerValueInterface<T extends IntegerValueInterface<T>> {

  T newWithIncrements();

  T newWithDecrements();

  T newWithAdd(int adding);

  T newWithPlus(int adding);

  T newWithMinus(int minusing);

  T newWithMultiply(int multiplying);

  T newWithDivide(int dividing);

  T newWithAdd(IntegerValue<?> adding);

  T newWithPlus(IntegerValue<?> adding);

  T newWithMinus(IntegerValue<?> minusing);

  T newWithMultiply(IntegerValue<?> multiplying);

  T newWithDivide(IntegerValue<?> dividing);

  boolean greaterThean(IntegerValue<?> right);

  boolean gt(IntegerValue<?> right);

  boolean greaterEqual(IntegerValue<?> right);

  boolean ge(IntegerValue<?> right);

  boolean lessEquals(IntegerValue<?> right);

  boolean le(IntegerValue<?> right);

  boolean lessThan(IntegerValue<?> right);

  boolean lt(IntegerValue<?> right);

  boolean equals(IntegerValue<?> right);

  boolean eq(IntegerValue<?> right);

  boolean ne(IntegerValue<?> right);

  boolean gt(int right);

  boolean greaterThean(int right);

  boolean greaterEqual(int right);

  boolean ge(int right);

  boolean lessEquals(int right);

  boolean le(int right);

  boolean lessThan(int right);

  boolean lt(int right);

  boolean equals(int right);

  boolean eq(int right);

  boolean ne(int right);

  boolean isNegative();

  boolean isPositive();

  boolean isGreaterThanZero();

  boolean isZero();

  T create(IntegerValue<?> i);

  T createIfMatch(Predicate<T> predicate, Supplier<T> supplier);

}