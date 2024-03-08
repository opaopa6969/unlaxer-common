package org.unlaxer;

public interface CodePointIndexInterface {//extends CodePointIndexFromParent , CodePointIndexFromRoot{
  
  public int rawValue();
  
  public IndexKind indexKind();
  
  public enum IndexKind {
    fromRoot,
    fromParent,
    thisSource,
    ;
    public boolean isFromRoot() {
      return this == fromRoot;
    }
    public boolean isFromParent() {
      return this == fromParent;
    }
    public boolean isThisSource() {
      return this == thisSource;
    }
  }
  
  public boolean isPresent(IndexKind indexKind);
  
  public int value(IndexKind indexKind);
}