package org.unlaxer;

public class ParserCursor{
	
	final Cursor consumed;
	final Cursor matched;
	
	public ParserCursor(ParserCursor parserCursor ,boolean resetMatched){
		consumed = new CursorImpl(parserCursor.consumed);
		matched = new CursorImpl(parserCursor.matched);
		if(resetMatched){
			resetMatchedWithConsumed(consumed, matched);
		}
	}
	
	public ParserCursor() {
		super();
		consumed = new CursorImpl();
		matched = new CursorImpl();
	}
	

	public ParserCursor(Cursor consumed, Cursor matched , boolean resetMatched) {
		super();
		this.consumed = consumed;
		this.matched = matched;
		if(resetMatched){
			resetMatchedWithConsumed(consumed, matched);
		}
	}

	public void addPosition(int adding){
	  addPosition(new Index(adding));
	}
	
	 public void addPosition(Index adding){
	    consumed.addPosition(adding);
//	    matched.addPosition(adding);
	    matched.setPosition(consumed.getPosition());
	  }

	
	public void addMatchedPosition(int adding){
		matched.addPosition(new Index(adding));
	}
	
	 public void addMatchedPosition(Index adding){
	   addMatchedPosition(adding);
  }
	
	public Cursor getCursor(TokenKind tokenKind){
		return tokenKind == TokenKind.consumed ? consumed : matched;
	}
	
	public CodePointIndex getPosition(TokenKind tokenKind){
		return getCursor(tokenKind).getPosition(); 
	}
	
	void resetMatchedWithConsumed(Cursor consumed, Cursor matched){
		matched.setPosition(consumed.getPosition());
		matched.setLineNumber(consumed.getLineNumber());
	}
	
}