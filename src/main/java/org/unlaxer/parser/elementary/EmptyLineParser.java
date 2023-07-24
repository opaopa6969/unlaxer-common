package org.unlaxer.parser.elementary;
import java.util.List;

import org.unlaxer.parser.Parser;
import org.unlaxer.parser.Parsers;
import org.unlaxer.parser.combinator.LazyChain;
import org.unlaxer.parser.combinator.MatchOnly;
import org.unlaxer.parser.combinator.ZeroOrMore;
import org.unlaxer.parser.posix.SpaceParser;

public class EmptyLineParser extends LazyChain{
  
  @Override
  public List<Parser> getLazyParsers() {
    return new Parsers(
        Parser.get(StartOfLineParser.class),
        new ZeroOrMore(Parser.get(SpaceParser.class))
          .newWithTerminator( 
              new MatchOnly(Parser.get(LineTerminatorParser.class))
        ),
        Parser.get(LineTerminatorParser.class)
    );
  }
}