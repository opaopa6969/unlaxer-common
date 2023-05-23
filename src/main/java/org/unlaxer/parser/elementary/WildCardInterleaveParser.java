package org.unlaxer.parser.elementary;

import java.util.List;
import java.util.function.Supplier;

import org.unlaxer.parser.Parser;
import org.unlaxer.parser.Parsers;
import org.unlaxer.parser.combinator.Choice;
import org.unlaxer.parser.combinator.LazyZeroOrMore;

public abstract class WildCardInterleaveParser extends LazyZeroOrMore{
  
  abstract List<Parser> interleaveParsers();
  
  @Override
  public Supplier<Parser> getLazyParser() {
    Parsers parsers = new Parsers(interleaveParsers());
    parsers.add(Parser.get(WildCardCharacterParser.class));
    return ()->   new Choice(parsers);
  }

}
