package org.unlaxer.parser.combinator;

import org.junit.Test;
import org.unlaxer.ParserTestBase;
import org.unlaxer.parser.Parsers;
import org.unlaxer.parser.elementary.WordParser;

public class LazyChoiceTest extends ParserTestBase{
  
  static class TestLazyChoie extends LazyChoice{

    @Override
    public Parsers getLazyParsers() {
      return Parsers.of(
          new WordParser("a"),
          new WordParser("b")
      );
    }
    
  }

  @Test
  public void test() {
    
    TestLazyChoie testLazyChoie = new TestLazyChoie();
    
    testAllMatch(testLazyChoie, "a");
    testAllMatch(testLazyChoie, "b");
    testUnMatch(testLazyChoie, "c");
    
    
  }

}
