package org.unlaxer.listener;

import java.util.Arrays;
import java.util.List;

import org.unlaxer.Token;
import org.unlaxer.context.ParseContext;
import org.unlaxer.parser.Parser;


public interface TransactionListener extends BreakPointHolder{
	
	public void setLevel(OutputLevel level);
	public void onOpen(ParseContext parseContext);
	public void onBegin(ParseContext parseContext , Parser parser);
	public void onCommit(ParseContext parseContext , Parser parser , List<Token> committedTokens);
	public void onRollback(ParseContext parseContext , Parser parser, List<Token> rollbackedTokens);
	public void onClose(ParseContext parseContext);
	
	public default void onCommit(ParseContext parseContext , Parser parser , Token committedToken){
		onCommit(parseContext, parser, Arrays.asList(committedToken));
	}
	

	/**
	 * set break point on this method if you needs 
	 */
	@BreakPointMethod
	public default void onOpenBreakPoint(){}

	/**
	 * set break point on this method if you needs 
	 */
	@BreakPointMethod
	public default void onBeginBreakPoint(){}

	/**
	 * set break point on this method if you needs 
	 */
	@BreakPointMethod
	public default void onCommitBreakPoint(){}

	/**
	 * set break point on this method if you needs 
	 */
	@BreakPointMethod
	public default void onRollbackBreakPoint(){}

	/**
	 * set break point on this method if you needs 
	 */
	@BreakPointMethod
	public default void onCloseBreakPoint(){}
	
	/**
	 * set break point on this method if you needs 
	 */
	@BreakPointMethod
	public default void onUpdateTransactionBreakPoint(){}
}