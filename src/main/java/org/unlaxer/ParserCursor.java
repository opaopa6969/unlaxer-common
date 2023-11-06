package org.unlaxer;

import org.unlaxer.Cursor.EndExclusiveCursor;

public class ParserCursor{
	
	final EndExclusiveCursor consumed;
	final EndExclusiveCursor matched;
	
	public ParserCursor(ParserCursor parserCursor ,boolean resetMatched){
		consumed = new EndExclusiveCursorImpl(parserCursor.consumed);
		matched = new EndExclusiveCursorImpl(parserCursor.matched);
		if(resetMatched){
			resetMatchedWithConsumed(consumed, matched);
		}
	}
	
	public ParserCursor() {
		super();
		consumed = new EndExclusiveCursorImpl();
		matched = new EndExclusiveCursorImpl();
	}
	

	public ParserCursor(EndExclusiveCursor consumed, EndExclusiveCursor matched , boolean resetMatched) {
		super();
		this.consumed = consumed;
		this.matched = matched;
		if(resetMatched){
			resetMatchedWithConsumed(consumed, matched);
		}
	}

	
	 public void addPosition(CodePointOffset adding){
	   consumed.addPosition(adding);
//	    matched.addPosition(adding);
	   matched.setPosition(consumed.getPosition());
	  }

	
	public void addMatchedPosition(CodePointOffset adding){
		matched.addPosition(adding);
	}
	
	 public void addMatchedPosition(Index adding){
	   addMatchedPosition(adding);
  }
	
	public EndExclusiveCursor getCursor(TokenKind tokenKind){
		return tokenKind == TokenKind.consumed ? consumed : matched;
	}
	
	public CodePointIndex getPosition(TokenKind tokenKind){
		return getCursor(tokenKind).getPosition(); 
	}
	
	void resetMatchedWithConsumed(EndExclusiveCursor consumed, EndExclusiveCursor matched){
		matched.setPosition(consumed.getPosition());
		matched.setLineNumber(consumed.getLineNumber());
	}
	
}