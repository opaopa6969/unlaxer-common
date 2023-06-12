package org.unlaxer;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.unlaxer.listener.OutputLevel;
import org.unlaxer.parser.Parser;
import org.unlaxer.reducer.TagBasedReducer.NodeKind;
import org.unlaxer.util.FactoryBoundCache;
import org.unlaxer.util.NullSafetyConcurrentHashMap;

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
	//TODO make private and rename astNodeChildren
	public  final List<Token> filteredChildren; // astNodeChildren
	
	private Map<Name,Object> extraObjectByName = new NullSafetyConcurrentHashMap<>();

	public enum ChildrenKind{
		original,
		astNodes
	}
	
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
	
	// TODO too specialized...?
//	Predicate<Token> AST_NODES = token -> token.parser.hasTag(NodeKind.node.getTag()); <-こちらだと動かない。そもそもTagなしの状態もありってことかな？
  Predicate<Token> AST_NODES = token -> false == token.parser.hasTag(NodeKind.notNode.getTag());

	
	public Token(TokenKind tokenKind , RangedString token, Parser parser , List<Token> children) {
		super();
		this.tokenKind = tokenKind;
		this.tokenString = token.token;
		this.tokenRange = token.range;
		this.parser = parser;
		this.originalChildren = children;
		parent= Optional.empty();
		children.stream().forEach(child->{
			child.parent = Optional.of(this);
//			child.parser.setParent(parser);
		});
		this.filteredChildren = children.stream()
			.filter(AST_NODES)
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

	public static RangedString createRangedString(List<Token> tokens, int position){
		
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
		return flatten(SearchFirst.Depth ,ChildrenKind.astNodes);
	}
	
	public List<Token> flatten(SearchFirst breadthOrDepth){
		return flatten(breadthOrDepth , ChildrenKind.astNodes);
	}
	
	public List<Token> flatten(SearchFirst breadthOrDepth , ChildrenKind childrenKind){
		return breadthOrDepth == SearchFirst.Depth ?
				flattenDepth(childrenKind) : flattenBreadth(childrenKind);
	}
	
	public List<Token> flattenDepth(ChildrenKind childrenKind){
		List<Token> list = new ArrayList<Token>();
		list.add(this);
		for(Token child :children(childrenKind)){
			list.addAll(child.flattenDepth(childrenKind));
		}
		return list;
	}
	
	public List<Token> flattenBreadth(ChildrenKind childrenKind){
		List<Token> list = new ArrayList<Token>();
		Deque<Token> deque = new ArrayDeque<Token>();
		deque.add(this);
		while (false == deque.isEmpty()) {
			Token poll = deque.poll();
			list.add(poll);
			if(false ==poll.children(childrenKind).isEmpty()) {
				deque.addAll(poll.children(childrenKind));
			}
		}
		return list;
	}

	
	public enum SearchFirst{
		Breadth,
		Depth
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
		return newWithReplacedParser(replace , ChildrenKind.astNodes);
	}
	public Token newWithReplacedParser(Parser replace , ChildrenKind childrenKind){
		if(false == children(childrenKind).isEmpty()){
			throw new IllegalArgumentException("not support collected token");
		}
		return new Token(tokenKind,new RangedString(tokenRange, tokenString),replace);
	}
	
	List<Token> children(ChildrenKind kind){
		return kind == ChildrenKind.astNodes ? 
				filteredChildren :
				originalChildren;
	}
	
	public Token newCreatesOf(List<Token> newChildrens) {
		
		Token newToken = new Token(tokenKind , newChildrens , parser , tokenRange.startIndexInclusive);
		return newToken;
	}
	
	public Token newCreatesOf(Token... newChildrens) {
		
		return newCreatesOf(Arrays.asList(newChildrens));
	}
	
	public Token getChild(Predicate<Token> predicates) {
		return getChild(predicates , ChildrenKind.astNodes);
	}
	
	public Token getChild(Predicate<Token> predicates , ChildrenKind childrenKind) {
		return children(childrenKind).stream().filter(predicates).findFirst().orElseThrow();
	}
	
	public int getChildIndex(Predicate<Token> predicates) {
		return getChildIndex(ChildrenKind.astNodes, predicates);
	}

	public int getChildIndex(ChildrenKind childrenKind, Predicate<Token> predicates) {
		
		int index=0;
		for (Token token : children(childrenKind)) {
			if(predicates.test(token)) {
				return index;
			}
			index++;
		}
		throw new IllegalArgumentException("predicates is not match");
	}
	
	public Token getChildWithParser(Predicate<Parser> predicatesWithTokensParser) {
		return getChildWithParser(predicatesWithTokensParser,ChildrenKind.astNodes);
	}
	
	public Token getChildWithParser(Predicate<Parser> predicatesWithTokensParser , ChildrenKind childrenKind) {
		return children(childrenKind).stream().filter(token-> predicatesWithTokensParser.test(token.parser)).findFirst().orElseThrow();
	}
	
	public int getChildIndexWithParser(Predicate<Parser> predicatesWithTokensParser) {
		return getChildIndexWithParser(ChildrenKind.astNodes, predicatesWithTokensParser);
	}
	
	public int getChildIndexWithParser(ChildrenKind childrenKind, Predicate<Parser> predicatesWithTokensParser) {
		
		int index=0;
		for (Token token : children(childrenKind)) {
			if(predicatesWithTokensParser.test(token.parser)) {
				return index;
			}
			index++;
		}
		throw new IllegalArgumentException("predicates is not match");
	}

	
	public Token getChildWithParser(Class<? extends Parser> parserClass) {
		return getChildWithParser(parser -> parser.getClass() == parserClass);
	}
	
	public int getChildIndexWithParser(Class<? extends Parser> parserClass) {
		return getChildIndexWithParser(ChildrenKind.astNodes, parserClass);
	}
	
	public int getChildIndexWithParser(ChildrenKind childrenKind, Class<? extends Parser> parserClass) {
		
		return getChildIndexWithParser(childrenKind , parser -> parser.getClass() == parserClass);
	}
	
	public Optional<Token> getChildAsOptional(Predicate<Token> predicates ) {
		return getChildAsOptional(predicates , ChildrenKind.astNodes);
	}
	public Optional<Token> getChildAsOptional(Predicate<Token> predicates ,ChildrenKind childrenKind) {
		return children(childrenKind).stream().filter(predicates).findFirst();
	}
	
	public Optional<Token> getChildWithParserAsOptional(Predicate<Parser> predicatesWithTokensParser){
		return getChildWithParserAsOptional(predicatesWithTokensParser , ChildrenKind.astNodes);
	}
	public Optional<Token> getChildWithParserAsOptional(Predicate<Parser> predicatesWithTokensParser,
			ChildrenKind childrenKind) {
		return children(childrenKind).stream().filter(token-> predicatesWithTokensParser.test(token.parser)).findFirst();
	}
	
	public Optional<Token> getChildWithParserAsOptional(Class<? extends Parser> parserClass) {
		return getChildWithParserAsOptional(parser -> parser.getClass() == parserClass);
	}
	
	public Stream<Token> getChildren(Predicate<Token> predicates) {
		return getChildren(predicates , ChildrenKind.astNodes);
	}
	public Stream<Token> getChildren(Predicate<Token> predicates , ChildrenKind childrenKind) {
		return children(childrenKind).stream().filter(predicates);
	}
	
	public Stream<Token> getChildrenWithParser(Predicate<Parser> predicatesWithTokensParser){
		return getChildrenWithParser(predicatesWithTokensParser , ChildrenKind.astNodes);
	}
	public Stream<Token> getChildrenWithParser(Predicate<Parser> predicatesWithTokensParser,
			ChildrenKind childrenKind) {
		return children(childrenKind).stream().filter(token-> predicatesWithTokensParser.test(token.parser));
	}
	
	public Stream<Token> getChildrenWithParser(Class<? extends Parser> parserClass) {
		return getChildrenWithParser(parser -> parser.getClass() == parserClass);
	}
	
	public List<Token> getChildrenAsList(Predicate<Token> predicates) {
		return getChildren(predicates).collect(Collectors.toList());
	}
	
	public List<Token> getChildrenWithParserAsList(Predicate<Parser> predicatesWithTokensParser) {
		return getChildrenWithParser(predicatesWithTokensParser).collect(Collectors.toList());
	}
	
	public List<Token> getChildrenWithParserAsList(Class<? extends Parser> parserClass) {
		return getChildrenWithParserAsList(parser -> parser.getClass() == parserClass);
	}
	
	public Token getChildFromOriginal(int index) {
		return children(ChildrenKind.original).get(index);
	}
	
	public Token getChildFromAstNodes(int index) {
		return filteredChildren.get(index);
	}

	public List<Token> getOriginalChildren() {
		return originalChildren;
	}

	public List<Token> getAstNodeChildren() {
		return filteredChildren;
	}
	
	public Token addChildren(Token...tokens) {
		
		for (Token token : tokens) {
			originalChildren.add(token);
			if(AST_NODES.test(token)) {
				filteredChildren.add(token);
			}
		}
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public<T> Optional<T> getExtraObject(Name name , Class<T> objectClass) {
		return Optional.ofNullable( (T)extraObjectByName.get(name));
	}
	
	public void setExtraObject(Name name , Object object) {
		extraObjectByName.put(name , object);
	}
	
	@SuppressWarnings("unchecked")
	public <T> Optional<T>  removeExtraObject(Name name , Class<T> objectClass) {
		return Optional.ofNullable( (T)extraObjectByName.remove(name));
	}
	
	public boolean removeExtraObject(Name name) {
		var preset = extraObjectByName.remove(name);
		return preset != null ; 
	}
}