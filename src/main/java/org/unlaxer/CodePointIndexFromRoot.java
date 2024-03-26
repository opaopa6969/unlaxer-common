package org.unlaxer;

public interface CodePointIndexFromRoot<T extends CodePointIndexFromRoot<T>> extends CodePointIndexInterface<T>{
  int indexFromRootAsNumber();
  CodePointIndex indexFromRoot();
}