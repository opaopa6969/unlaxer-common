package org.unlaxer;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class SourceTest {

  @Test
  public void test() {
    
    List<StringSource> list = List.of(new StringSource("abc") , new StringSource("def"));
    
    Source collect = list.stream().collect(Source.joining());
    
    System.out.println(collect);
    
    
    assertEquals("abcdef" , collect.toString());
    
  }

}
