package org.unlaxer;

import org.unlaxer.Cursor.EndExclusiveCursor;

public class EndExclusiveCursorImpl extends AbstractCursorImpl<EndExclusiveCursor> implements EndExclusiveCursor{
  
  public EndExclusiveCursorImpl() {
    super(CursorKind.endExclusive);
  }
  
  public EndExclusiveCursorImpl(EndExclusiveCursor cursor) {
    super(cursor);
  }
  
  public EndExclusiveCursorImpl(StartInclusiveCursor cursor) {
    super(cursor);
  }

  @Override
  EndExclusiveCursor thisObject() {
    return this;
  }
}