package org.unlaxer;

public interface CodePointIndexInterface {//extends CodePointIndexFromParent , CodePointIndexFromRoot{
  
  public int rawValue();
  
  public IndexKind indexKind();
  
  public boolean isPresent(IndexKind indexKind);
  
  public int value(IndexKind indexKind);
}