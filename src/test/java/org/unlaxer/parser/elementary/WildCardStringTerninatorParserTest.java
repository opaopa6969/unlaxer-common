package org.unlaxer.parser.elementary;

import org.junit.Test;
import org.unlaxer.ParserTestBase;
import org.unlaxer.listener.OutputLevel;
import org.unlaxer.parser.combinator.Chain;

public class WildCardStringTerninatorParserTest extends ParserTestBase{

  @Test
  public void test() {
    
    setLevel(OutputLevel.detail);
    {
      
      WordParser abc = new WordParser("abc");
      WildCardStringTerninatorParser wildCardStringTerninatorParser = new WildCardStringTerninatorParser("\n");
      LineTerminatorParser lineTerminatorParser = new LineTerminatorParser();
      
      Chain wildCardAbc = new Chain(wildCardStringTerninatorParser , lineTerminatorParser , abc);
      Chain wildCardLT= new Chain(wildCardStringTerninatorParser , lineTerminatorParser);
      
      testAllMatch(wildCardLT, "nikuniku\n");
      testAllMatch(wildCardAbc, "nikuniku\nabc");
    }
    
    {
      
      WordParser abc = new WordParser("abc");
      WildCardStringTerninatorParser wildCardStringTerninatorParser = new WildCardStringTerninatorParser(new LineTerminatorParser());
      LineTerminatorParser lineTerminatorParser = new LineTerminatorParser();

      Chain wildCard= new Chain(wildCardStringTerninatorParser);
      Chain wildCardAbc = new Chain(wildCardStringTerninatorParser , lineTerminatorParser , abc);
      Chain wildCardLT= new Chain(wildCardStringTerninatorParser , lineTerminatorParser);
      
      testAllMatch(wildCard, "nikuniku");
      testAllMatch(wildCardLT, "nikuniku\n");
      testAllMatch(wildCardAbc, "nikuniku\nabc");
    }
  }

}
