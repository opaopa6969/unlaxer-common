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
  public EndExclusiveCursor resolveLineNumber(RootPositionResolver rootPositionResolver) {
    
    CodePointIndex _codePointIndex = position.isZero() ? 
        position : 
        position.newWithDecrements();
    
    setLineNumber(rootPositionResolver.lineNumberFrom(_codePointIndex));
    return thisObject();
  }

  @Override
  EndExclusiveCursor thisObject() {
    return this;
  }
}