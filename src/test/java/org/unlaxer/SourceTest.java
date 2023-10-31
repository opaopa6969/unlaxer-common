package org.unlaxer;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class SourceTest {

  @Test
  public void test() {

    {
      String collects = List.of("abc","def").stream().collect(Collectors.joining());
      assertEquals("abcdef" , collects);
    }
    {
      List<StringSource> list = List.of(StringSource.createDetachedSource("abc") , StringSource.createDetachedSource("def"));
      Source collect = list.stream().collect(Source.joining(","));
      System.out.println(collect);
      assertEquals("abc,def" , collect.toString());
    }
    
    {
      List<StringSource> list = List.of(StringSource.createDetachedSource("abc") , StringSource.createDetachedSource("def"));
      Source collect = list.stream().collect(Source.joining());
      System.out.println(collect);
      assertEquals("abcdef" , collect.toString());
    }
  }

}
