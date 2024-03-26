package org.unlaxer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
      List<Source> list = List.of(StringSource.createDetachedSource("abc") , StringSource.createDetachedSource("def"));
      Source collect = list.stream().collect(Source.joining(","));
      System.out.println(collect);
      assertEquals("abc,def" , collect.toString());
    }
    
    {
      List<Source> list = List.of(StringSource.createDetachedSource("abc") , StringSource.createDetachedSource("def"));
      Source collect = list.stream().collect(Source.joining());
      System.out.println(collect);
      assertEquals("abcdef" , collect.toString());
    }
    
    {
      Source source = StringSource.createRootSource("1");
      Source subSource = source.subSource(new CodePointIndex(0,source), new CodePointIndex(0,source));
      System.out.println("subSource:" + subSource.sourceAsString());
    }
    
    
    {
      try {
        Source source = StringSource.createRootSource("");
        Source subSource = source.subSource(new CodePointIndex(0,source), new CodePointIndex(-1,source));
        System.out.println("subSource:" + subSource.sourceAsString());
        fail();
        
      }catch (Exception e) {
        e.printStackTrace();
      }
    }
    assertFalse(StringSource.createRootSource("").isPresent());
    assertTrue(StringSource.createRootSource("").isEmpty());
    
    {
      Source createRootSource = StringSource.createRootSource("ABC");
      
      Source subSource = createRootSource.subSource(new CodePointIndex(1,createRootSource) , new CodePointIndex(3,createRootSource));
      Source substring = createRootSource.subSource(new CodePointIndex(1,createRootSource));
      
      System.out.println(subSource);
      System.out.println(substring);
      assertEquals(subSource, substring);
    }
  }
}
