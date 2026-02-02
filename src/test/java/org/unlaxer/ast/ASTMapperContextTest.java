package org.unlaxer.ast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;
import org.unlaxer.Source.SourceKind;
import org.unlaxer.StringSource;
import org.unlaxer.Token;
import org.unlaxer.TokenKind;
import org.unlaxer.parser.AbstractParser;
import org.unlaxer.parser.ChildOccurs;
import org.unlaxer.parser.Parser;
import org.unlaxer.parser.Parsers;

/**
 * Tests for {@link ASTMapperContext} baseline behavior.
 *
 * Phase-1 goal:
 * - Context can be created with 0..N mappers
 * - If a mapper matches, it is applied
 * - If no mapper matches, token is returned as-is
 * - Optional-returning variant behaves consistently
 */
public class ASTMapperContextTest {

  /** Parser that does NOT implement ASTMapper. */
  static class PlainParser extends AbstractParser {
    private static final long serialVersionUID = 1L;

    @Override
    public Parser createParser() {
      return this;
    }

    @Override
    public void prepareChildren(Parsers childrenContainer) {
      // TODO Auto-generated method stub
      
    }

    @Override
    public ChildOccurs getChildOccurs() {
      // TODO Auto-generated method stub
      return null;
    }
  }

  /** Result parser used to mark that mapping happened. */
  static class AstResultParser extends AbstractParser {
    private static final long serialVersionUID = 1L;

    @Override
    public Parser createParser() {
      return this;
    }

    @Override
    public void prepareChildren(Parsers childrenContainer) {
      // TODO Auto-generated method stub
      
    }

    @Override
    public ChildOccurs getChildOccurs() {
      // TODO Auto-generated method stub
      return null;
    }
  }

  /**
   * Parser that also works as an ASTMapper.
   * This matches the current default logic of {@link ASTMapper#canASTMapping(Token)}
   * which compares token.parser.getClass() and mapper.getClass().
   */
  static class ParserWithMapper extends AbstractParser implements ASTMapper {
    private static final long serialVersionUID = 1L;

    @Override
    public Parser createParser() {
      return this;
    }

    @Override
    public Token toAST(ASTMapperContext context, Token parsedToken) {
      // mark mapping by replacing parser with a different class.
      return parsedToken.newWithReplacedParser(new AstResultParser());
    }

    @Override
    public void prepareChildren(Parsers childrenContainer) {
      // TODO Auto-generated method stub
      
    }

    @Override
    public ChildOccurs getChildOccurs() {
      // TODO Auto-generated method stub
      return null;
    }
  }

  @Test
  public void toAST_returnsSameToken_whenNoMapperMatches() {
    ASTMapperContext ctx = ASTMapperContext.create(new ParserWithMapper());

    Token parsed = new Token(TokenKind.consumed,
        StringSource.create("x", SourceKind.root),
        new PlainParser());

    Token ast = ctx.toAST(parsed);

    // No mapping should happen, return original instance.
    assertSame(parsed, ast);
    assertEquals(PlainParser.class, ast.getParser().getClass());
  }

  @Test
  public void toAST_appliesMapper_whenMapperMatches() {
    ParserWithMapper mapper = new ParserWithMapper();
    ASTMapperContext ctx = ASTMapperContext.create(mapper);

    Token parsed = new Token(TokenKind.consumed,
        StringSource.create("x", SourceKind.root),
        mapper);

    Token ast = ctx.toAST(parsed);

    assertNotSame(parsed, ast);
    assertEquals(AstResultParser.class, ast.getParser().getClass());
  }

  @Test
  public void toASTexpectsMapping_returnsEmpty_whenNoMapperMatches() {
    ASTMapperContext ctx = ASTMapperContext.create(new ParserWithMapper());

    Token parsed = new Token(TokenKind.consumed,
        StringSource.create("x", SourceKind.root),
        new PlainParser());

    Optional<Token> maybeAst = ctx.toASTexpectsMapping(parsed);
    assertTrue(maybeAst.isEmpty());
  }

  @Test
  public void toASTexpectsMapping_returnsPresent_whenMapperMatches() {
    ParserWithMapper mapper = new ParserWithMapper();
    ASTMapperContext ctx = ASTMapperContext.create(mapper);

    Token parsed = new Token(TokenKind.consumed,
        StringSource.create("x", SourceKind.root),
        mapper);

    Optional<Token> maybeAst = ctx.toASTexpectsMapping(parsed);
    assertTrue(maybeAst.isPresent());
    assertEquals(AstResultParser.class, maybeAst.get().getParser().getClass());
  }

  @Test
  public void create_allowsNullOrNullEntries_andBehavesAsEmpty() {
    ASTMapperContext ctx1 = ASTMapperContext.create((ASTMapper[]) null);
    ASTMapperContext ctx2 = ASTMapperContext.create((ASTMapper) null);

    Token parsed = new Token(TokenKind.consumed,
        StringSource.create("x", SourceKind.root),
        new PlainParser());

    assertSame(parsed, ctx1.toAST(parsed));
    assertSame(parsed, ctx2.toAST(parsed));
    assertTrue(ctx1.toASTexpectsMapping(parsed).isEmpty());
    assertTrue(ctx2.toASTexpectsMapping(parsed).isEmpty());
  }
}
