package org.unlaxer;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class RangedStringTest {

  @Test
  public void test() {
    
    String word ="a";
    
    String surrogatePare = "𪛊";
    
    String cr = new String( new byte[] {0x0d});
    String lf = new String(new byte[] {0x0a});
    String crlf = new String(new byte[] {0x0d , 0x0a});
    
    String lines =
        word + cr + 
        word + crlf + 
        word + lf + 
        word + crlf+
        lf+
        cr+
        surrogatePare+cr+
        surrogatePare+crlf+
        surrogatePare+lf+
        surrogatePare
        ;
    
    List<RangedString> tokeizeWithLineTerminator = RangedString.tokeizeWithLineTerminator(lines);
    
    assertEquals(word+cr, tokeizeWithLineTerminator.get(0).token.get());
    assertEquals(word+crlf, tokeizeWithLineTerminator.get(1).token.get());
    assertEquals(word+lf, tokeizeWithLineTerminator.get(2).token.get());
    assertEquals(word+crlf, tokeizeWithLineTerminator.get(3).token.get());
    assertEquals(lf, tokeizeWithLineTerminator.get(4).token.get());
    assertEquals(cr, tokeizeWithLineTerminator.get(5).token.get());
    assertEquals(surrogatePare+cr, tokeizeWithLineTerminator.get(6).token.get());
    assertEquals(surrogatePare+crlf, tokeizeWithLineTerminator.get(7).token.get());
    assertEquals(surrogatePare+lf, tokeizeWithLineTerminator.get(8).token.get());
    assertEquals(surrogatePare, tokeizeWithLineTerminator.get(9).token.get());
  }
}
