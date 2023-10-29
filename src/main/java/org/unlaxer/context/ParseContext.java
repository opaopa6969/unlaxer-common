package org.unlaxer.context;

import java.io.Closeable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.unlaxer.CodePointIndex;
import org.unlaxer.CodePointLength;
import org.unlaxer.CursorImpl;
import org.unlaxer.CursorRange;
import org.unlaxer.LineNumber;
import org.unlaxer.Name;
import org.unlaxer.ParserCursor;
import org.unlaxer.RangedString;
import org.unlaxer.Source;
import org.unlaxer.TransactionElement;
import org.unlaxer.listener.ParserListener;
import org.unlaxer.listener.ParserListenerContainer;
import org.unlaxer.listener.TransactionListener;
import org.unlaxer.parser.GlobalScopeTree;
import org.unlaxer.parser.Parser;
import org.unlaxer.parser.Parsers;
import org.unlaxer.parser.combinator.ChoiceInterface;
import org.unlaxer.parser.combinator.NonOrdered;

public class ParseContext implements 
	Closeable, Transaction,
	ParserListenerContainer,
	GlobalScopeTree , ParserContextScopeTree{

	private static final long serialVersionUID = 1202780890703131636L;

	// TODO store successfully token's <position,tokens> map
	boolean doMemoize;

	public final Source source;

	boolean createMetaToken = true;
	
	Map<Name, ParserListener> parserListenerByName = new LinkedHashMap<>();
	
	Map<Name, TransactionListener> listenerByName = new LinkedHashMap<>();

	final Deque<TransactionElement> tokenStack = new ArrayDeque<TransactionElement>();

	//FIXME change store to ScopeTree
	public Map<ChoiceInterface, Parser> chosenParserByChoice = new HashMap<>();
	
	//FIXME change store to ScopeTree
	public Map<NonOrdered, Parsers> orderedParsersByNonOrdered = new HashMap<>();
	
	Map<Parser, Map<Name, Object>> scopeTreeMapByParser = new HashMap<>();
	
	Map<Name, Object> globalScopeTreeMap = new HashMap<>();
	
	Collection<AdditionalCommitAction> actions;

	public ParseContext(Source source, ParseContextEffector... parseContextEffectors) {
		this.source = source;
		actions = new ArrayList<>();
		tokenStack.add(new TransactionElement(new ParserCursor()));

		for (ParseContextEffector parseContextEffector : parseContextEffectors) {
			parseContextEffector.effect(this);
		}
		onOpen(this);
	}
	
	@Override
	public Deque<TransactionElement> getTokenStack(){
		return tokenStack;
	}

	@Override
	public void close() {
		if (tokenStack.size() != 1) {
			throw new IllegalStateException("transaction nest is illegal. check source code.");
		}
		onClose(this);
	}

	@Override
	public int getLength() {
		return source.getLength();
	}
	
	@Override
	public RangedString peek(int startIndexInclusive, int length) {
		return source.peek(startIndexInclusive, length);
	}

	@Override
	public Map<Name, TransactionListener> getTransactionListenerByName() {
		return listenerByName;
	}

	@Override
	public Map<Parser, Map<Name, Object>> getParserContextScopeTreeMap() {
		return scopeTreeMapByParser;
	}

	@Override
	public Map<Name, Object> getGlobalScopeTreeMap() {
		return globalScopeTreeMap;
	}

	@Override
	public Map<Name, ParserListener> getParserListenerByName() {
		return parserListenerByName;
	}

	@Override
	public ParseContext get() {
		return this;
	}

	@Override
	public boolean doCreateMetaToken() {
		return createMetaToken;
	}

	@Override
	public Map<ChoiceInterface, Parser> getChosenParserByChoice() {
		return chosenParserByChoice;
	}

	@Override
	public Map<NonOrdered, Parsers> getOrderedParsersByNonOrdered() {
		return orderedParsersByNonOrdered;
	}

	@Override
	public Source getSource() {
		return source;
	}

  @Override
  public Collection<AdditionalCommitAction> getActions() {
    return actions;
  }

  @Override
  public void addActions(List<AdditionalCommitAction> additionalCommitActions) {
    actions.addAll(additionalCommitActions);
  }

  @Override
  public LineNumber getLineNUmber(CodePointIndex Position) {
    return source.getLineNUmber(Position);
  }
  
  public CursorRange createCursorRange(CodePointIndex startIndexInclusive , CodePointLength codePointLength) {
    new CursorRange(new CursorImpl(null))
  }
}
