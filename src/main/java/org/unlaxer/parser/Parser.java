package org.unlaxer.parser;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Supplier;

import org.unlaxer.Parsed;
import org.unlaxer.ParserPath;
import org.unlaxer.TaggableAccessor;
import org.unlaxer.TokenKind;
import org.unlaxer.ast.ASTMapper.ASTNodeKind;
import org.unlaxer.context.ParseContext;

public interface Parser extends //
    PropagatableDestination, //
    TaggableAccessor, //
//	Taggable , //
    ParserPath, //
//	ParserHierarchy , //
//	ParserFinder,//
//	Initializable,
    Serializable {

  // FIXME make Parsed parse(ParseContext parseContext) only. use to get tokenKind
  // and invertMatch
  public Parsed parse(ParseContext parseContext, TokenKind tokenKind, boolean invertMatch);

  public default Parsed parse(ParseContext parseContext) {
    return parse(parseContext, getTokenKind(), false);
  }

  public default TokenKind getTokenKind() {
    return overrideTokenKind().orElse(TokenKind.consumed);
  }

  public Parser setTokenKind(TokenKind tokenKind);

  public default Parser setCosumedTokenKind() {
    
    return setTokenKind(TokenKind.consumed);
  }

  public default Parser setMatchONlyTokenKind() {
    
    return setTokenKind(TokenKind.matchOnly);
  }

  public Optional<TokenKind> overrideTokenKind();

  public default boolean forTerminalSymbol() {
    return this instanceof TerminalSymbol;
  }

  public default boolean equalsByClass(Parser other) {
    return getClass().equals(other.getClass());
  }

  public default Parser getChild() {
    return getChildren().get(0);
  }

  @Override
  default Parser getThisParser() {
    return this;
  }

  public NodeReduceMarker getNodeReduceMarker();

  public static <T extends Parser> T get(Class<T> clazz) {
    return ParserFactoryByClass.get(clazz);
  }

  public static <T extends Parser> T get(ASTNodeKind nodeKind, Class<T> clazz) {
    return ParserFactoryByClass.get(nodeKind, clazz);
  }

  public static <T extends Parser> T get(Supplier<? extends Parser> supplier) {
    return ParserFactoryBySupplier.get(supplier);
  }

}