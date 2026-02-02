package org.unlaxer.ast;

import org.junit.Test;
import org.unlaxer.Source;
import org.unlaxer.StringSource;
import org.unlaxer.Token;
import org.unlaxer.TokenPrinter;
import org.unlaxer.context.ParseContext;
import org.unlaxer.listener.OutputLevel;
import org.unlaxer.parser.Parser;
import org.unlaxer.parser.clang.IdentifierParser;
import org.unlaxer.parser.combinator.Chain;
import org.unlaxer.parser.combinator.ZeroOrMore;
import org.unlaxer.parser.elementary.ParenthesesParser;
import org.unlaxer.parser.posix.CommaParser;
import org.unlaxer.parser.posix.DotParser;

public class ComplexASTTest {

  @Test
  public void testMethodCallAST() {
      // 識別子 (TerminalSymbolなので、中身の 'm','a','t','h' は隠蔽されるはず)
      Parser id = Parser.get(IdentifierParser.class).addTag(ASTNodeKind.Operand.tag());
      Parser dot = Parser.get(DotParser.class).addTag(ASTNodeKind.Operator.tag());
      Parser comma = Parser.get(CommaParser.class).addTag(ASTNodeKind.Operator.tag());

      // 引数リスト: カンマ区切り
      Parser argList = new Chain(
          id,
          new ZeroOrMore(new Chain(comma, id))
      ).setASTNodeKind(ASTNodeKind.OneOrMoreOperandSuccessor);

      // 引数括弧自体を「演算子（関数適用）」として扱うのがコツです
      Parser parenArgs = new ParenthesesParser(argList)
          .addTag(ASTNodeKind.Operator.tag()); 

      // メソッド呼び出し全体
      // 構造: math(ID) .(DOT) sin(ID) (x,y)(PAREN)
      Parser methodCall = new Chain(
          id, dot, id, parenArgs
      ).setASTNodeKind(ASTNodeKind.Operator);

      Source source = StringSource.createRootSource("math.sin(x,y)");
      ParseContext context = new ParseContext(source);
      Token rawToken = methodCall.parse(context).getRootToken();

      ASTMapperContext mapperContext = ASTMapperContext.create(new TagBasedASTMapper());
      Token ast = mapperContext.toAST(rawToken);

      System.out.println("=== Refined Method Call AST ===");
      System.out.println(TokenPrinter.get(ast, 0, OutputLevel.detail, true));
  }
  
}