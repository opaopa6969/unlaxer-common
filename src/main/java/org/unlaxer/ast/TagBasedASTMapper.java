package org.unlaxer.ast;

import org.unlaxer.Token;
import org.unlaxer.TokenList;
import org.unlaxer.parser.combinator.ChainInterface;
import org.unlaxer.parser.combinator.Occurs;
import org.unlaxer.parser.elementary.ParenthesesParser;
import java.util.stream.Collectors;

public class TagBasedASTMapper implements ASTMapper {

    @Override
    public boolean canASTMapping(Token token) {
        return true; 
    }

    @Override
    public Token toAST(ASTMapperContext context, Token token) {
        // 1. 【原子保護】Operandタグ（識別子など）は、これ以上分解せず「単語」として確定させる
        if (token.parser.hasTag(ASTNodeKind.Operand.tag())) {
            return new Token(token.tokenKind, token.source, token.parser, new TokenList());
        }

        // 2. 【子要素の再帰処理】
        // 中間ノードをフラット化する前に、まずはボトムアップで子をすべてAST化する
        TokenList astChildren = TokenList.of(
            token.getAstNodeChildren().stream()
                .map(context::toAST)
                .collect(Collectors.toList())
        );

        // 3. 【構造のフラット化（ノイズ除去）】
        // タグが「一つも」設定されていない Chain や Occurs のみ、中身を引き上げる
        TokenList flatChildren = new TokenList();
        for (Token child : astChildren) {
            if (isMeaninglessNoise(child)) {
                flatChildren.addAll(child.getAstNodeChildren());
            } else {
                flatChildren.add(child);
            }
        }
        
        Token current = token.newCreatesOf(flatChildren);
        ASTNodeKind kind = current.parser.astNodeKind();

        // 4. 【セマンティック変換】

        // A. 演算子の結合 (Infix: a.b / Postfix: func(args))
        if (kind == ASTNodeKind.Operator || current.parser.hasTag(ASTNodeKind.Operator.tag())) {
            return foldSemantic(current);
        }

        // B. リストの掃除 (Successorタグがあれば、Operand以外を消去)
        if (kind.toString().contains("Successor")) {
            return cleanList(current);
        }

        // C. 不要な階層の剥離 (Choiceの結果や、演算に使われなかった括弧)
        if (shouldPassThrough(current)) {
            TokenList children = current.getAstNodeChildren();
            if (children.isEmpty()) return current;
            if (current.parser instanceof ParenthesesParser && children.size() >= 3) {
                return children.get(1); // ( expr ) -> expr
            }
            return children.get(0);
        }

        return current;
    }

    private boolean isMeaninglessNoise(Token t) {
        // タグが一つもなく、かつ構造用ノードの場合のみフラット化（解体）を許可する
        return (t.parser instanceof ChainInterface || t.parser instanceof Occurs) 
                && t.parser.getTags().isEmpty() 
                && t.parser.astNodeKind() == ASTNodeKind.NotSpecified;
    }

    private boolean shouldPassThrough(Token t) {
        // Operatorタグがあるものは結合の主役なので、ここでは剥がさない
        if (t.parser.hasTag(ASTNodeKind.Operator.tag())) return false;
        ASTNodeKind kind = t.parser.astNodeKind();
        return kind == ASTNodeKind.ChoicedOperator || kind == ASTNodeKind.ChoicedOperand 
               || t.parser instanceof ParenthesesParser;
    }

    private Token foldSemantic(Token token) {
        TokenList children = token.getAstNodeChildren();
        if (children.size() < 2) return token;
        
        var it = children.iterator();
        Token result = it.next();

        while (it.hasNext()) {
            Token opCandidate = it.next();
            
            if (it.hasNext()) {
                // [A, ., B] -> .(A, B)
                Token right = it.next();
                result = opCandidate.newCreatesOf(result, right);
            } else if (opCandidate.parser.hasTag(ASTNodeKind.Operator.tag())) {
                // [math.sin, (x,y)] -> (x,y) を親にして結合
                TokenList callElements = new TokenList();
                callElements.add(result); // 関数名
                // 括弧ノード(opCandidate)の中に隠れている引数をすべて救出
                callElements.addAll(extractActualOperands(opCandidate));
                result = opCandidate.newCreatesOf(callElements);
            }
        }
        return result;
    }

    private TokenList extractActualOperands(Token t) {
        TokenList operands = new TokenList();
        if (t.parser.hasTag(ASTNodeKind.Operand.tag())) {
            operands.add(t);
        } else {
            for (Token child : t.getAstNodeChildren()) {
                operands.addAll(extractActualOperands(child));
            }
        }
        return operands;
    }

    private Token cleanList(Token token) {
        return token.newCreatesOf(TokenList.of(
            token.getAstNodeChildren().stream()
                .filter(t -> t.parser.hasTag(ASTNodeKind.Operand.tag()))
                .collect(Collectors.toList())
        ));
    }
}