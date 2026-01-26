package org.unlaxer.ast;

import static org.junit.Assert.*;

import org.junit.Test;
import org.unlaxer.Source.SourceKind;
import org.unlaxer.StringSource;
import org.unlaxer.Token;
import org.unlaxer.TokenKind;
import org.unlaxer.parser.AbstractParser;
import org.unlaxer.parser.ChildOccurs;
import org.unlaxer.parser.Parser;
import org.unlaxer.parser.Parsers;

public class ASTMapperContextPipelineTest {

  // ---- dummy parsers ----

  static class P0 extends AbstractParser {
    private static final long serialVersionUID = 1L;
    @Override public Parser createParser() { return this; }
    @Override public void prepareChildren(Parsers c) {}
    @Override public ChildOccurs getChildOccurs() { return ChildOccurs.none; }
  }

  static class P1 extends AbstractParser {
    private static final long serialVersionUID = 1L;
    @Override public Parser createParser() { return this; }
    @Override public void prepareChildren(Parsers c) {}
    @Override public ChildOccurs getChildOccurs() { return ChildOccurs.none; }
  }

  static class P2 extends AbstractParser {
    private static final long serialVersionUID = 1L;
    @Override public Parser createParser() { return this; }
    @Override public void prepareChildren(Parsers c) {}
    @Override public ChildOccurs getChildOccurs() { return ChildOccurs.none; }
  }

  private static Token tok(String s, Parser p) {
    return new Token(
        TokenKind.consumed,
        StringSource.create(s, SourceKind.root),
        p
    );
  }

  // ---- mappers ----

  /** Matches P0 but does NOT transform (returns same). */
  static class NoopMapper implements ASTMapper {
    @Override
    public boolean canASTMapping(Token t) {
      return t.getParser() instanceof P0;
    }

    @Override
    public Token toAST(ASTMapperContext c, Token t) {
      return t; // same
    }
  }

  /** Transforms P0 -> P1 */
  static class FirstTransformMapper implements ASTMapper {
    @Override
    public boolean canASTMapping(Token t) {
      return t.getParser() instanceof P0;
    }

    @Override
    public Token toAST(ASTMapperContext c, Token t) {
      return t.newWithReplacedParser(new P1());
    }
  }

  /** Transforms P1 -> P2 */
  static class SecondTransformMapper implements ASTMapper {
    @Override
    public boolean canASTMapping(Token t) {
      return t.getParser() instanceof P1;
    }

    @Override
    public Token toAST(ASTMapperContext c, Token t) {
      return t.newWithReplacedParser(new P2());
    }
  }

  // ---- tests ----

  @Test
  public void noopMapper_doesNotBlockNextMapper() {
    Token src = tok("x", new P0());

    ASTMapperContext ctx = ASTMapperContext.create(
        new NoopMapper(),
        new FirstTransformMapper()
    );

    Token ast = ctx.toAST(src);

    assertNotSame(src, ast);
    assertTrue(ast.getParser() instanceof P1);
  }

  @Test
  public void transformedToken_flowsToNextMapper() {
    Token src = tok("x", new P0());

    ASTMapperContext ctx = ASTMapperContext.create(
        new FirstTransformMapper(),
        new SecondTransformMapper()
    );

    Token ast = ctx.toAST(src);

    assertNotSame(src, ast);
    assertTrue(ast.getParser() instanceof P2);
  }

  @Test
  public void returnsSame_whenNoMapperTransforms() {
    Token src = tok("x", new P0());

    ASTMapperContext ctx = ASTMapperContext.create(
        new NoopMapper()
    );

    Token ast = ctx.toAST(src);

    assertSame(src, ast);
  }

  @Test
  public void toASTexpectsMapping_reflectsPipelineResult() {
    Token src = tok("x", new P0());

    ASTMapperContext ctx = ASTMapperContext.create(
        new NoopMapper(),
        new FirstTransformMapper()
    );

    assertTrue(ctx.toASTexpectsMapping(src).isPresent());
  }
}
