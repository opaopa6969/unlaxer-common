package org.unlaxer.parser.elementary;
import org.unlaxer.parser.Parser;
import org.unlaxer.parser.Parsers;
import org.unlaxer.parser.combinator.LazyChain;
import org.unlaxer.parser.combinator.MatchOnly;
import org.unlaxer.parser.combinator.ZeroOrMore;
import org.unlaxer.parser.posix.SpaceParser;

public class EmptyLineParser extends LazyChain{
  
	private static final long serialVersionUID = -2954119020777951724L;

@Override
  public Parsers getLazyParsers() {
    return new Parsers(
        Parser.get(StartOfLineParser.class),
        new ZeroOrMore(SpaceParser.class)
          .newWithTerminator( 
              new MatchOnly(Parser.get(LineTerminatorParser.class))
        ),
        Parser.get(LineTerminatorParser.class)
    );
  }
}