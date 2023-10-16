package org.unlaxer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RangedString{
	
	public final CursorRange range;
	public Optional<String> token;
	
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

	// TODO
//	public RangedString(Cursor cursor, String token) {
//		super();
//		this.range = new CursorRange(cursor. , startIndex + token.length());
//		this.token = Optional.of(token);
//	}
	
	public static List<RangedString> tokeizeWithLineTerminator(String text){
	  
	  List<RangedString> list = new ArrayList<>();
	  
	  StringBuilder builder = new StringBuilder();
	  

	  boolean previousIsCR = false;
	  
	  Cursor startCursor= new CursorImpl();
    Cursor endCursor= new CursorImpl();
    
    int codePointCount = text.codePointCount(0, text.length());
    
    for(int i = 0 ; i < codePointCount ; i++) {
      
      int codePoint = text.codePointAt(i);
      
      endCursor.incrementPosition();
      
      if(previousIsCR && codePoint != 0x0a) {
        list.add(new RangedString(new CursorRange(startCursor, endCursor), builder.toString()));
        startCursor = new CursorImpl(endCursor);
        endCursor.incrementLineNumber();
        builder = new StringBuilder();
        previousIsCR = false;
      }
      
      
      char[] chars = Character.toChars(codePoint);
      builder.append(chars);
      
      if(codePoint == 0x0a) {
        list.add(new RangedString(new CursorRange(startCursor, endCursor), builder.toString()));
        startCursor = new CursorImpl(endCursor);
        endCursor.incrementLineNumber();
      }

      if(codePoint == 0x0d) {
        previousIsCR=true;
      }
    }
    return list;
}
	
	
}