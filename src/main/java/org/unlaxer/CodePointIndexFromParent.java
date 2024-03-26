package org.unlaxer;

public interface CodePointIndexFromParent<T extends CodePointIndexFromParent<T>> extends CodePointIndexInterface<T>{//extends IntegerValueInterface<CodePointIndexFromParent>{
  int indexFromParentAsNumber();
  CodePointIndex indexFromParent();
}