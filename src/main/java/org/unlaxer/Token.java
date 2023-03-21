package org.unlaxer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.unlaxer.listener.OutputLevel;
import org.unlaxer.parser.Parser;
import org.unlaxer.reducer.TagBasedReducer.NodeKind;
import org.unlaxer.util.FactoryBoundCache;

public class Token implements Serializable{
	
	private static final long serialVersionUID = -2232289508694932061L;

	static final FactoryBoundCache<Integer, RangedString> empties = 
			new FactoryBoundCache<>(RangedString::new);
	
	static final FactoryBoundCache<Token, String> displayString = 
			new FactoryBoundCache<>(
					token->TokenPrinter.get(token,0,OutputLevel.detail,false));

	
	public final Optional<String> tokenString;
	public final Parser parser;
	public final Range tokenRange;
	
	public Optional<Token> parent;
	private final List<Token> originalChildren;
	//TODO make private
	public  final List<Token> filteredChildren;
	public final TokenKind tokenKind;
	
	
	public Token(TokenKind tokenKind , RangedString token, Parser parser) {
		this(tokenKind , token , parser , new ArrayList<>());
	}
	
	public Token(TokenKind tokenKind , List<Token> tokens , Parser parser , int position) {
		this(tokenKind , 
			createRangedString(tokens , position),
			parser,
			tokens);
	}
	
	public Token(TokenKind tokenKind , RangedString token, Parser parser , List<Token> children) {
		super();
		this.tokenKind = tokenKind;
		this.tokenString = token.token;
		this.tokenRange = token.range;
		this.parser = parser;
		this.originalChildren = children;
		parent= Optional.empty();
		children.stream().forEach(child->child.parent = Optional.of(this));
		this.filteredChildren = children.stream()
			.filter(childToken->false == childToken.parser.hasTag(NodeKind.notNode.getTag()))
			.collect(Collectors.toList());
	}
	
	public static Token empty(TokenKind tokenKind , int position , Parser parser){
		return new Token(tokenKind , empties.get(position),parser);
	}
	
	public Optional<String> getToken() {
		return tokenString;
	}
	
	public Range getTokenRange() {
		return tokenRange;
	}
	
	public RangedString getRangedString(){
		return new RangedString(tokenRange , tokenString);
	}
	
	public Parser getParser(){
		return parser;
	}

	static RangedString createRangedString(List<Token> tokens, int position){
		
		if(tokens.isEmpty()){
			return new RangedString(position);
		}
		
		Optional<String> token = Optional.of(
			tokens.stream()
				.map(Token::getToken)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.joining()));
		
		int startIndex = tokens.get(0).tokenRange.startIndexInclusive;
		int endIndex = tokens.get(tokens.size()-1).tokenRange.endIndexExclusive;
		return new RangedString(new Range(startIndex, endIndex), token);
	}
	
	public List<Token> flatten(){
		List<Token> list = new ArrayList<Token>();
		list.add(this);
		for(Token child :originalChildren){
			list.addAll(child.flatten());
		}
		return list;
	}
	
	@Override
	public String toString() {
		return displayString.get(this);
	}
	
	public boolean isTerminalSymbol(){
		return parser.forTerminalSymbol();
	}

	public TokenKind getTokenKind() {
		return tokenKind;
	}
	
	public Token newWithReplacedParser(Parser replace){
		if(false == originalChildren.isEmpty()){
			throw new IllegalArgumentException("not support collected token");
		}
		return new Token(tokenKind,new RangedString(tokenRange, tokenString),replace);
	}
	
	public Token newCreatesOf(List<Token> newChildrens) {
		
		Token newToken = new Token(tokenKind , newChildrens , parser , tokenRange.startIndexInclusive);
		return newToken;
	}
	
	public Token newCreatesOf(Token... newChildrens) {
		
		return newCreatesOf(Arrays.asList(newChildrens));
	}
}