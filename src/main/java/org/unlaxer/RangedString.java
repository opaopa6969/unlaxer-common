package org.unlaxer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RangedString{
	
	public final CursorRange range;
	public Optional<String> token;
	
  public RangedString(Cursor cursor) {
    this(new CursorRange(cursor));
  }
	
	public RangedString(CursorRange range, Optional<String> token) {
		super();
		this.range = range;
		this.token = token;
	}
	
	public RangedString(CursorRange range, String token) {
		super();
		this.range = range;
		this.token = Optional.of(token);
	}

	
	public RangedString(CursorRange cursorRange) {
		super();
		this.range = cursorRange;
		this.token = Optional.empty();
	}
	
	 public RangedString() {
      super();
      this.range = new CursorRange();
      this.token = Optional.empty();
    }
	 
	 public RangedString(List<Token> tokens) {
	   
	   super();
	   
	    if(tokens.isEmpty()){
	      token = Optional.empty();
	      range = new CursorRange();
	    }else {
	      token = Optional.of(
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
	
  public static List<RangedString> tokeizeWithLineTerminator(String text) {

    List<RangedString> list = new ArrayList<>();

    StringBuilder builder = new StringBuilder();

    boolean previousIsCR = false;

    Cursor startCursor = new CursorImpl();
    Cursor endCursor = new CursorImpl();

    int codePointCount = text.codePointCount(0, text.length());

    for (int i = 0; i < codePointCount; i++) {

      int codePoint = text.codePointAt(i);

      endCursor.incrementPosition();

      if (previousIsCR && codePoint != 0x0a) {
        list.add(new RangedString(new CursorRange(startCursor, endCursor), builder.toString()));
        startCursor = new CursorImpl(endCursor);
        endCursor.incrementLineNumber();
        builder = new StringBuilder();
        previousIsCR = false;
      }

      char[] chars = Character.toChars(codePoint);
      builder.append(chars);

      if (codePoint == 0x0a) {
        list.add(new RangedString(new CursorRange(startCursor, endCursor), builder.toString()));
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
    return SymbolMap.replaceSymbol(token.orElse(""),SymbolMap.lf)
        +range.toString();
  }
}