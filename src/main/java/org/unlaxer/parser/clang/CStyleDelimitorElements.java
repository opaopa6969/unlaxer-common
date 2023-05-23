package org.unlaxer.parser.clang;

import java.util.List;

import org.unlaxer.parser.Parser;
import org.unlaxer.parser.Parsers;
import org.unlaxer.parser.combinator.LazyChoice;
import org.unlaxer.parser.elementary.SpaceDelimitor;

public class CStyleDelimitorElements extends LazyChoice{

  @Override
  public List<Parser> getLazyParsers() {
    
    return new Parsers(
        Parser.get(BlockComment.class),
        Parser.get(CPPComment.class),
        Parser.get(SpaceDelimitor.class)
    );
  }
  
}
