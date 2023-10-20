package org.unlaxer;

import java.util.Arrays;
import java.util.NavigableMap;
import java.util.TreeMap;

public class StringSource implements Source{
	
	private static final long serialVersionUID = 566340401655249596L;
	
	private String source;
	private int[] codePoints;
	NavigableMap<CodePointIndex, LineNumber> lineNumberByIndex = new TreeMap<>();
	

	public StringSource(String source) {
		super();
		this.source = source;
		codePoints = source.codePoints().toArray();
    int codePointCount = codePoints.length;
    
    LineNumber lineNumber = new LineNumber(0);
    CodePointIndex index = new CodePointIndex(0);
    lineNumberByIndex.put(index, lineNumber);
    
    
    for (int i = 0; i < codePointCount; i++) {
      int codePointAt = codePoints[i];
      
      if(codePointAt != SymbolMap.lf.codes[0] && 
          codePointAt != SymbolMap.cr.codes[0]) {
        continue;
      }
      if(codePointAt == SymbolMap.cr.codes[0] && 
          codePointCount-1!=i && codePoints[i+1] ==SymbolMap.lf.codes[0]) {
          i++;
      }
      lineNumberByIndex.put(new CodePointIndex(i+1), lineNumber.increments());
    }
	}
	
	public LineNumber getLineNUmber(CodePointIndex Position) {
	  return lineNumberByIndex.floorEntry(Position).getValue();
	}

	@Override
	public RangedString peek(int startIndexInclusive, int length) {
		
		if(startIndexInclusive + length > codePoints.length){
		  CodePointIndex index = new CodePointIndex(startIndexInclusive);
		  CursorRange cursorRange = new CursorRange(new CursorImpl()
		      .setPosition(index)
		      .setLineNumber(getLineNUmber(index))
		  );
			return new RangedString(cursorRange);
		}
		
		CodePointIndex startIndex = new CodePointIndex(startIndexInclusive);
		CodePointIndex endIndex = new CodePointIndex(startIndexInclusive+length);
    CursorRange cursorRange = new CursorRange(
        new CursorImpl()
          .setPosition(startIndex)
          .setLineNumber(getLineNUmber(startIndex)),
        new CursorImpl()
          .setPosition(endIndex)
          .setLineNumber(getLineNUmber(endIndex))
        
    );


		return new RangedString(
		    cursorRange,
				subString(new Index(startIndexInclusive), new Length(length)));
	}
	
	public int[] subCodePoints(Index startIndexInclusive, Index endIndexExclusive) {
	  return Arrays.copyOfRange(codePoints, startIndexInclusive.value , endIndexExclusive.value);
	}
	
  public String subString(Index startIndexInclusive, Index endIndexExclusive) {
    return new String(codePoints, startIndexInclusive.value , endIndexExclusive.value - startIndexInclusive.value);
  }
  
  public String subString(Index startIndexInclusive, Length length) {
    return new String(codePoints, startIndexInclusive.value , length.value);
  }


	@Override
	public int getLength() {
		return codePoints.length;
	}

	@Override
	public String toString() {
		return source;
	}
}