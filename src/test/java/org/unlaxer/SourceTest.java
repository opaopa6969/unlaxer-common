package org.unlaxer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.unlaxer.Cursor.EndExclusiveCursor;
import org.unlaxer.Cursor.StartInclusiveCursor;
import org.unlaxer.Source.SourceKind;

public class SourceTest {

  @Test
  public void test() {
    
    CodePointOffset codePointOffset = new CodePointOffset(0);

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
    
    {
      StringSource source = StringSource.createRootSource("1");
      Source subSource = source.subSource(new CodePointIndex(0,codePointOffset), new CodePointIndex(0,codePointOffset));
      System.out.println("subSource:" + subSource.sourceAsString());
    }
    
    
    {
      try {
        StringSource source = StringSource.createRootSource("");
        Source subSource = source.subSource(new CodePointIndex(0,codePointOffset), new CodePointIndex(-1,codePointOffset));
        System.out.println("subSource:" + subSource.sourceAsString());
        fail();
        
      }catch (Exception e) {
        //java.lang.StringIndexOutOfBoundsException: offset 0, count -1, length 0 が出るのが正しい
        e.printStackTrace();
      }
    }
    assertFalse(StringSource.createRootSource("").isPresent());
    assertTrue(StringSource.createRootSource("").isEmpty());
    
    {
      StringSource createRootSource = StringSource.createRootSource("ABC");
      
      Source subSource = createRootSource.subSource(new CodePointIndex(1,codePointOffset) , new CodePointIndex(3,codePointOffset));
      Source substring = createRootSource.subSource(new CodePointIndex(1,codePointOffset));
      
      System.out.println(subSource);
      System.out.println(substring);
      assertEquals(subSource, substring);
    }
    
    
    {
      Source createRootSource = StringSource.createRootSource("ABCDEF");

      
      System.out.println("rootSource CursorRange:" + createRootSource.cursorRange());
      
      Source subSource = createRootSource.subSource(new CodePointIndex(3,codePointOffset));
      
      CodePointLength codePointLength = subSource.codePointLength();
      System.out.println(codePointLength);
      
      assertEquals(3, codePointLength.value());

      CursorRange cursorRange = subSource.cursorRange();
      
      System.out.println("subSource CursorRange:" + cursorRange);
      
      StartInclusiveCursor startIndexInclusive = cursorRange.startIndexInclusive();
      CodePointIndex position = startIndexInclusive.position();
      
      assertEquals(3, position.value(SourceKind.root));
      assertEquals(0, position.value(SourceKind.subSource));
      
      EndExclusiveCursor endExclusiveCursor = cursorRange.endIndexExclusive();

      CodePointIndex position2 = endExclusiveCursor.position();
      assertEquals(3, position2.value(SourceKind.subSource));
      assertEquals(6, position2.value(SourceKind.root));
      
    }
  }
  
  public static void main(String[] args) {
    
    CodePointOffset codePointOffset = new CodePointOffset(0);
    StringSource createRootSource = StringSource.createRootSource("ABCDEF");
    Source subSource = createRootSource.subSource(new CodePointIndex(3,codePointOffset));
    System.out.println(subSource);
    
    CodePointLength codePointLength = subSource.codePointLength();
    System.out.println(codePointLength);
    
    CursorRange cursorRange = subSource.cursorRange();
    
    System.out.println(cursorRange);
    StartInclusiveCursor startIndexInclusive = cursorRange.startIndexInclusive();
    
    CodePointIndex position = startIndexInclusive.position();
    System.out.println(position.valueWithOffsetFromRoot());
    
  }
}

