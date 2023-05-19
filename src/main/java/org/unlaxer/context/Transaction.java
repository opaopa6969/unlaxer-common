package org.unlaxer.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.unlaxer.Committed;
import org.unlaxer.ParserCursor;
import org.unlaxer.RangedString;
import org.unlaxer.Source;
import org.unlaxer.Token;
import org.unlaxer.TokenKind;
import org.unlaxer.TransactionElement;
import org.unlaxer.parser.CollectingParser;
import org.unlaxer.parser.LazyInstance;
import org.unlaxer.parser.MetaFunctionParser;
import org.unlaxer.parser.Parser;
import org.unlaxer.parser.combinator.ChoiceInterface;
import org.unlaxer.parser.combinator.NonOrdered;

public interface Transaction extends TransactionListenerContainer , Source , ParseContextBase{
	

	public default TransactionElement getCurrent() {
		return getTokenStack().peekFirst();
	}
	
	public default Optional<TransactionElement> getPrevious() {
		Iterator<TransactionElement> iterator = getTokenStack().iterator();
		iterator.next();
		return iterator.hasNext() ? Optional.of(iterator.next()) : Optional.empty();
	}
	
	public default void consume(int length) {
		getCurrent().consume(length);
	}

	public default void matchOnly(int length) {
		getCurrent().matchOnly(length);
	}
	
	public default void begin(Parser parser) {
		getTokenStack().push(new TransactionElement(getCurrent().getParserCursor()));
		onBegin(get(), parser);
	}
	
	Collection<AdditionalCommitAction> getActions();
	
  default void addActions(AdditionalCommitAction... additionalCommitActions) {
    
    addActions(
      Stream.of(additionalCommitActions).collect(Collectors.toList())
    );
  }
  
  void addActions(List<AdditionalCommitAction> additionalCommitActions);
	
  /**
   * @param parser that processed tokens
   * @param tokenKind commit token's kind
   * @param actions effect ParseContext at committing phase if needed
   * @return last added Tokens
   */
  public default Committed commit(Parser parser, TokenKind tokenKind , AdditionalCommitAction... actions) {
    
    List<AdditionalCommitAction> allActions = new ArrayList<>();
    
    allActions.addAll(getActions());
    allActions.addAll(getActions());
    
    if(actions.length >0) {
      
      for (AdditionalCommitAction action :actions) {
        allActions.add(action);
      }
    }
      
    ParseContext parseContext = get();
    
    for (AdditionalCommitAction action : allActions) {
      if (action instanceof AdditionalPreCommitAction) {
        ((AdditionalPreCommitAction) action).effect(parser, parseContext);
      }
    }

    TransactionElement current = getTokenStack().pollFirst();
    TransactionElement parent = getCurrent();
    parent.setCursor(new ParserCursor(current.getParserCursor(),false));
    
    boolean outputCollected = doCreateMetaToken() || 
        false == parser instanceof MetaFunctionParser || 
        parser instanceof LazyInstance;

    Committed committed;
    
    int position = parent.getPosition(tokenKind);

    if (parser instanceof CollectingParser && outputCollected) {
      
      Token collected = ((CollectingParser) parser).collect(
          current.tokens, position , tokenKind , tokenKind.passFilter);
      parent.tokens.add(collected);
      onCommit(parseContext, parser, Arrays.asList(collected));
      committed = new Committed(collected, current.tokens);

    } else {

      parent.tokens.addAll(current.tokens);
      onCommit(parseContext, parser, current.tokens);
      committed = new Committed(current.tokens);
    }

    for (AdditionalCommitAction action : getActions()) {
      if (action instanceof AdditionalPostCommitAction) {
        ((AdditionalPostCommitAction) action)
          .effect(parser, parseContext , committed);
      }
    }
    return committed;
  }
    
	public default void rollback(Parser parser) {
		if (parser instanceof ChoiceInterface) {
			getChosenParserByChoice().remove(parser);
		}
		if (parser instanceof NonOrdered) {
			getOrderedParsersByNonOrdered().remove(parser);
		}
		TransactionElement pollFirst = getTokenStack().pollFirst();
		onRollback(get(), parser , pollFirst.getTokens());
	}
	
	public default String getRemain(TokenKind tokenKind) {
		int position = getPosition(tokenKind);
		return getSource().peek(position, getLength() - position).token.orElse("");
	}

	public default String getConsumed(TokenKind tokenKind) {
		int position = getPosition(tokenKind);
		return getSource().peek(0, position).token.orElse("");
	}

	public default boolean allMatched() {
		return getPosition(TokenKind.matchOnly) == getSource().getLength();
	}

	public default boolean allConsumed() {
		return getPosition(TokenKind.consumed) == getSource().getLength();
	}

	public default RangedString peek(TokenKind tokenKind, int length) {
		return peek(getCurrent().getPosition(tokenKind), length);
	}

	public default int getConsumedPosition() {
		return getPosition(TokenKind.consumed);
	}

	public default int getMatchedPosition() {
		return getPosition(TokenKind.matchOnly);
	}

	public default int getPosition(TokenKind tokenKind) {
		return getCurrent().getPosition(tokenKind);
	}
	
	public default Optional<Parser> getChosen(ChoiceInterface choice) {
		return Optional.ofNullable(getChosenParserByChoice().get(choice));
	}

	public default List<Parser> getOrdered(NonOrdered nonOrdered) {
		return getOrderedParsersByNonOrdered().get(nonOrdered);
	}
	
	interface AdditionalCommitAction{}
	
	public interface AdditionalPreCommitAction extends AdditionalCommitAction {
		public void effect(Parser parser, ParseContext parseContext);
	}
	
	public interface AdditionalPostCommitAction extends AdditionalCommitAction {
		public void effect(Parser parser, ParseContext parseContext , Committed committed);
	}
	
	//check parser equality with instance is expected ? check case of equality by class is better for ?
	//cause , specify Predicate<Parser>
	public default Optional<Token> getMatchedToken(Predicate<Parser> targetParserPredicate) {

		return getTokenStack().stream()//
				.flatMap(transactionElement -> transactionElement.getTokens().stream())//
				.flatMap(token -> token.flatten().stream())//
				.filter(token -> targetParserPredicate.test(token.parser))//
				.findFirst();
	}
}