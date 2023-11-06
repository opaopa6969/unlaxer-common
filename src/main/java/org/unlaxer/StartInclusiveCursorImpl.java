package org.unlaxer;

import org.unlaxer.Cursor.StartInclusiveCursor;

public class StartInclusiveCursorImpl extends AbstractCursorImpl<StartInclusiveCursor> implements StartInclusiveCursor{
  
  public StartInclusiveCursorImpl() {
    super(CursorKind.startInclusive);
  }
  
  public StartInclusiveCursorImpl(StartInclusiveCursor cursor) {
    super(cursor);
  }
  
  public StartInclusiveCursorImpl(EndExclusiveCursor cursor) {
    super(cursor);
  }
  
  @Override
  StartInclusiveCursor thisObject() {
    return this;
  }

}