package org.unlaxer;

import static org.junit.Assert.*;

import org.junit.Test;

public class CursorRangeTest {

  @Test
  public void test() {
    CodePointIndex position0 = new CodePointIndex(0);
    CodePointIndex position5 = new CodePointIndex(5);
    CodePointIndex position10 = new CodePointIndex(10);
    LineNumber lineNumber0 = new LineNumber(0);
    LineNumber lineNumber5 = new LineNumber(5);
    LineNumber lineNumber10 = new LineNumber(10);
    CursorRange cursorRange0 = CursorRange.of(position0, lineNumber0, position5, lineNumber5);
    CursorRange cursorRange1 = CursorRange.of(position5, lineNumber5, position10, lineNumber10);
    
    assertTrue(cursorRange0.lt(cursorRange1));
    assertTrue(cursorRange0.lessThan(cursorRange1));
    
    assertTrue(cursorRange1.graterThan(cursorRange0));
    assertTrue(cursorRange1.gt(cursorRange0));

  }

}
