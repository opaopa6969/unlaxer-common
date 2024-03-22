package org.unlaxer;

import org.unlaxer.base.IntegerValueInterface;

public interface CodePointIndexInterface<E extends CodePointIndexInterface<E>> extends IntegerValueInterface<E>{//extends CodePointIndexFromParent , CodePointIndexFromRoot{
  
  public int rawValue();
  
  public IndexKind indexKind();
  
  public boolean isPresent(IndexKind indexKind);
  
  public int value(IndexKind indexKind);
}