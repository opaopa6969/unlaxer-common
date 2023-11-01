package org.unlaxer;

import java.util.stream.Stream;

public interface RootPositionResolver extends CodePointIndexToStringIndex , CodePointIndexToLineNumber , StringIndexToCodePointIndex{
  CursorRange rootCursorRange();

  Stream<Source> lines(Source root);

  Size lineSize();
}