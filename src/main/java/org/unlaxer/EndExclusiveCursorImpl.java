package org.unlaxer;

import org.unlaxer.Cursor.EndExclusiveCursor;
import org.unlaxer.Source.SourceKind;

public class EndExclusiveCursorImpl extends AbstractCursorImpl<EndExclusiveCursor> implements EndExclusiveCursor{
  
  public EndExclusiveCursorImpl(SourceKind sourceKind, PositionResolver positionResolver,
<<<<<<< HEAD
      CodePointIndex position) {
    super(CursorKind.endExclusive, sourceKind, positionResolver, position);
=======
      AttachedCodePointIndex position , CodePointOffset offsetFromRoot) {
    super(CursorKind.endExclusive, sourceKind, positionResolver, position , offsetFromRoot);
>>>>>>> 6614c86145cd00541269b18010372831b907fbda
  }
  
  public EndExclusiveCursorImpl(PositionResolver positionResolver) {
    super(CursorKind.endExclusive,SourceKind.root,positionResolver);
  }
  
  public EndExclusiveCursorImpl(SourceKind sourceKind , PositionResolver positionResolver) {
    super(CursorKind.endExclusive,sourceKind,positionResolver);
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

  @Override
  public EndExclusiveCursor copy() {
    return new EndExclusiveCursorImpl(sourceKind , positionResolver , position);
  }
  
  @Override
  public EndExclusiveCursor newWithAddPosition(CodePointOffset adding) {
    return copy().addPosition(adding);
  }
  
}