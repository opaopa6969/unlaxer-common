package org.unlaxer;

public class RangeWithCursor extends Range{
  
  public final Cursor cursor;

  public RangeWithCursor(Cursor cursor) {
    super();
    this.cursor = cursor;
  }

  public RangeWithCursor(int startIndexInclusive, int endIndexExclusive , Cursor cusCursor) {
    super(startIndexInclusive, endIndexExclusive);
    this.cursor = cusCursor;
  }

  public RangeWithCursor(int startIndexInclusive , Cursor cursor) {
    super(startIndexInclusive);
    this.cursor = cursor;
  }
}