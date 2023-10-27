package org.unlaxer;

import org.junit.Test;

public class StringSourceTest {

  @Test
  public void test() {
    
    StringSource stringSource = new StringSource("ABC");
    
    RangedString peek = stringSource.peek(new CodePointIndex(1), new CodePointLength(0));
    
    CursorRange range = peek.range;
    
    System.out.println(range);
    String orElse = peek.token.orElse("null!!");
    System.out.println("a:" + orElse);
    
    if(orElse.isBlank()) {
      
      System.out.println("blank!!");
    }
  }

}
