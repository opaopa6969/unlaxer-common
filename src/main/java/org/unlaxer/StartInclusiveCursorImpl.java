package org.unlaxer;

import org.unlaxer.Cursor.StartInclusiveCursor;
import org.unlaxer.Source.SourceKind;

public class StartInclusiveCursorImpl extends AbstractCursorImpl<StartInclusiveCursor> implements StartInclusiveCursor{
  
  public StartInclusiveCursorImpl(SourceKind sourceKind, PositionResolver positionResolver,
<<<<<<< HEAD
      CodePointIndex position) {
    super(CursorKind.startInclusive, sourceKind, positionResolver, position);
=======
      AttachedCodePointIndex position , CodePointOffset offsetFromRoot) {
    super(CursorKind.startInclusive, sourceKind, positionResolver, position , offsetFromRoot);
>>>>>>> 6614c86145cd00541269b18010372831b907fbda
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
    return new StartInclusiveCursorImpl(sourceKind , positionResolver , position);
  }
  
  @Override
  public StartInclusiveCursor newWithAddPosition(CodePointOffset adding) {
    return copy().addPosition(adding);
  }
  
}