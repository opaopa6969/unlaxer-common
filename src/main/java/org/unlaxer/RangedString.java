package org.unlaxer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.unlaxer.util.SimpleBuilder;

class _RangedString{
	
	public final CursorRange range;
	public Optional<Source> token;
	
  public _RangedString(Cursor cursor) {
    this(new CursorRange(cursor));
  }
	
	public _RangedString(CursorRange range, Optional<Source> token) {
		super();
		this.range = range;
		this.token = token;
	}
	
	public _RangedString(CursorRange range, Source token) {
		super();
		this.range = range;
		this.token = Optional.of(token);
	}

	
	public _RangedString(CursorRange cursorRange) {
		super();
		this.range = cursorRange;
		this.token = Optional.empty();
	}
	
	 public _RangedString() {
      super();
      this.range = new CursorRange();
      this.token = Optional.empty();
    }
	 
	 public _RangedString(List<Token> tokens) {
	   
	   super();
	   
	    if(tokens.isEmpty()){
	      token = Optional.empty();
	      range = new CursorRange();
	    }else {
	      Optional.of(
	          tokens.stream()
	          .map(Token::getToken)
	          .filter(Optional::isPresent)
	          .map(Optional::get)
	          .collect(Collectors.joining()));
	      
	      CursorRange combinedCursorRange = TokenList.combinedCursorRange(tokens);
	      range = combinedCursorRange;
	    }
	 }

	// TODO
//	public RangedString(Cursor cursor, String token) {
//		super();
//		this.range = new CursorRange(cursor. , startIndex + token.length());
//		this.token = Optional.of(token);
//	}
	
  public static List<_RangedString> tokeizeWithLineTerminator(String text) {

    List<_RangedString> list = new ArrayList<>();

    SimpleBuilder builder = new SimpleBuilder();

    boolean previousIsCR = false;

    Cursor startCursor = new CursorImpl();
    Cursor endCursor = new CursorImpl();

    int codePointCount = text.codePointCount(0, text.length());

    for (int i = 0; i < codePointCount; i++) {

      int codePoint = text.codePointAt(i);

      endCursor.incrementPosition();

      if (previousIsCR && codePoint != 0x0a) {
        list.add(new _RangedString(new CursorRange(startCursor, endCursor), new StringSource(builder.toString())));
        startCursor = new CursorImpl(endCursor);
        endCursor.incrementLineNumber();
        builder = new SimpleBuilder();
        previousIsCR = false;
      }

      char[] chars = Character.toChars(codePoint);
      builder.append(chars);

      if (codePoint == 0x0a) {
        list.add(new _RangedString(new CursorRange(startCursor, endCursor), builder.toSource()));
        startCursor = new CursorImpl(endCursor);
        endCursor.incrementLineNumber();
      }

      if (codePoint == 0x0d) {
        previousIsCR = true;
      }
    }
    return list;
  }

  @Override
  public String toString() {
    return SymbolMap.replaceSymbol(token.orElse(Source.EMPTY),SymbolMap.lf)
        +range.toString();
  }
}