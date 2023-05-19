package org.unlaxer.parser.clang;

import java.util.List;

import org.unlaxer.Name;
import org.unlaxer.parser.ChainParsers;
import org.unlaxer.parser.Parser;
import org.unlaxer.parser.combinator.Choice;
import org.unlaxer.parser.combinator.LazyChain;
import org.unlaxer.parser.elementary.EndOfSourceParser;
import org.unlaxer.parser.elementary.LineTerminatorParser;
import org.unlaxer.parser.elementary.WordParser;

public class CPPComment extends LazyChain{

  // parser for // comment
  public CPPComment() {
    super();
  }

  public CPPComment(Name name) {
    super(name);
  }
  
  @Override
  public List<Parser> getLazyParsers() {
    return new ChainParsers(
      new WordParser("//"),
      new Choice(
          Parser.get(EndOfSourceParser.class),//,
          Parser.get(LineTerminatorParser.class)
      )
    );
  }

}