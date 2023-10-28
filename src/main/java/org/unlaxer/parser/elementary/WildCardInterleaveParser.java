package org.unlaxer.parser.elementary;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.unlaxer.Cursor;
import org.unlaxer.CursorRange;
import org.unlaxer.RangedString;
import org.unlaxer.Token;
import org.unlaxer.TokenKind;
import org.unlaxer.parser.Parser;
import org.unlaxer.parser.Parsers;
import org.unlaxer.parser.combinator.Choice;
import org.unlaxer.parser.combinator.ChoiceInterface;
import org.unlaxer.parser.combinator.LazyZeroOrMore;
import org.unlaxer.util.SimpleBuilder;
import org.unlaxer.util.annotation.TokenExtractor;

public abstract class WildCardInterleaveParser extends LazyZeroOrMore{
  
  abstract List<Parser> interleaveParsers();
  
  @Override
  public Supplier<Parser> getLazyParser() {
    Parsers parsers = Parsers.of(interleaveParsers());
    parsers.add(Parser.get(WildCardCharacterParser.class));
    return ()->   new Choice(parsers);
  }
  
  
  @TokenExtractor
  public List<Token> getParsedWithConcattedCharcter(Token thisParserParsed) {
    
    List<Token> concatted = new ArrayList<>();
    List<Token> flatten = thisParserParsed.filteredChildren;
    
    SimpleBuilder characters = new SimpleBuilder();
    Cursor current = null;
    
    for (Token token : flatten) {
      token = ChoiceInterface.choiced(token);
      if(token.parser instanceof SingleCharacterParser) {
        if(current == null) {
          current = token.tokenRange.startIndexInclusive;
        }
        token.tokenSource.ifPresent(characters::append);
      }else {
        if(characters.length()>0) {
          Token token2 = new Token(
              TokenKind.consumed, 
              new RangedString(new CursorRange(current , token.tokenRange.endIndexExclusive), characters.toSource()), 
              Parser.get(WildCardStringParser.class)
           );
          concatted.add(token2);
          characters = new SimpleBuilder();
          current = null;
        }
        concatted.add(token);
      }
    }
    return concatted;
  }

}
