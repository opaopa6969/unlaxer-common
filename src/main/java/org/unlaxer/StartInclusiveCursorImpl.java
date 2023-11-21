package org.unlaxer;

import org.unlaxer.Cursor.StartInclusiveCursor;
import org.unlaxer.Source.SourceKind;

public class StartInclusiveCursorImpl extends AbstractCursorImpl<StartInclusiveCursor> implements StartInclusiveCursor{
  
  public StartInclusiveCursorImpl(CursorKind cursorKind, SourceKind sourceKind, PositionResolver positionResolver,
      CodePointIndex position , CodePointOffset offsetFromRoot) {
    super(cursorKind, sourceKind, positionResolver, position , offsetFromRoot);
  }

  public StartInclusiveCursorImpl(PositionResolver positionResolver) {
    super(CursorKind.startInclusive,SourceKind.root,positionResolver);
  }
  
  public StartInclusiveCursorImpl(SourceKind sourceKind , PositionResolver positionResolver) {
    super(CursorKind.startInclusive,sourceKind,positionResolver);
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

  @Override
  public StartInclusiveCursor copy() {
    return new StartInclusiveCursorImpl(cursorKind,  sourceKind , positionResolver , position , offsetFromRoot);
  }
  
  @Override
  public StartInclusiveCursor newWithAddPosition(CodePointOffset adding) {
    return copy().addPosition(adding);
  }
  
}