package org.unlaxer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IndexTest {

  @Test
  public void test() {
    
    

    Source rootSource = StringSource.createRootSource("abcdefgh");
    
    
    assertEquals(8 , new Index(3).newWithAdd(5).value());
    assertEquals(8 , new Index(3).newWithAdd(new CodePointIndex(5,rootSource)).value());
    
  }

}
