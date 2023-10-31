package org.unlaxer;

import org.junit.Test;

public class StringSourceTest {

  @Test
  public void test() {
    
    StringSource stringSource = StringSource.createRootSource("ABC");
    
    Source peek = stringSource.peek(new CodePointIndex(1), new CodePointLength(0));
    
    CursorRange range = peek.cursorRange();
    
    System.out.println(range);
    String orElse = peek.toString();
    System.out.println("a:" + orElse);
    
    if(orElse.isBlank()) {
      
      System.out.println("blank!!");
    }
  }

}
