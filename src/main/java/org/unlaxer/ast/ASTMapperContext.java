package org.unlaxer.ast;

import java.util.Objects;
import java.util.Optional;

import org.unlaxer.Token;

public interface ASTMapperContext{

  public static ASTMapperContext create(ASTMapper... astMappers) {
    final ASTMapper[] safeMappers = astMappers == null ? new ASTMapper[0] : astMappers.clone();

    return new ASTMapperContext() {

      @Override
      public Token toAST(Token parsedToken) {
        Objects.requireNonNull(parsedToken, "parsedToken");

        Token current = parsedToken;

        for (ASTMapper astMapper : safeMappers) {
          if (astMapper == null) continue;

          // ★「いまの current」に対してマッチするか見る
          if (!astMapper.canASTMapping(current)) continue;

          Token mapped = astMapper.toAST(this, current);

          // ★同一インスタンスなら「適用できなかった」扱いで次へ
          if (mapped == current) continue;

          // ★変換できたら、その結果を次の mapper に流す（パイプライン）
          current = mapped;
        }

        return current;
      }

      @Override
      public Optional<Token> toASTexpectsMapping(Token parsedToken) {
        Objects.requireNonNull(parsedToken, "parsedToken");

        Token result = toAST(parsedToken);
        return (result == parsedToken) ? Optional.empty() : Optional.of(result);
      }

    };
  }

  Token toAST(Token parsedToken);

  Optional<Token> toASTexpectsMapping(Token parsedToken);
}
