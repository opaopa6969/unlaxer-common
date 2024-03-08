package org.unlaxer;

public enum IndexKind {
  root,
  parent,
  thisSource,
  ;
  public boolean isRoot() {
    return this == root;
  }
  public boolean isParent() {
    return this == parent;
  }
  public boolean isThisSource() {
    return this == thisSource;
  }
}