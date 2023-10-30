package org.unlaxer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Stream;

public class IndexAndLineNumber{
  
  public final NavigableMap<CodePointIndex, LineNumber> lineNumberByIndex = new TreeMap<>();
  public final Map<CodePointIndex,StringIndex> stringIndexByCodePointIndex = new HashMap<>();
  public final Map<StringIndex,CodePointIndex> codePointIndexByStringIndex = new HashMap<>();
  public final List<CursorRange> cursorRanges = new ArrayList<>();
  public final CursorRange cursorRange;
  
  public IndexAndLineNumber(int[] codePoints) {
    int codePointCount = codePoints.length;
    
    LineNumber lineNumber = new LineNumber(0);
    CodePointIndex index = new CodePointIndex(0);
    CodePointIndex startIndex = new CodePointIndex(0);
    CodePointIndex previousStartIndex;
    lineNumberByIndex.put(index, lineNumber);
    
    StringIndex stringIndex = new StringIndex(0);
    CodePointIndex codePointIndex = new CodePointIndex(0);
    
    for (int i = 0; i < codePointCount; i++) {
      stringIndexByCodePointIndex.put(codePointIndex, stringIndex);
      codePointIndexByStringIndex.put(stringIndex,codePointIndex);
    
      int codePointAt = codePoints[i];
      
      int adding = Character.isBmpCodePoint(codePointAt) ? 1:2;
      stringIndex = stringIndex.add(adding);
      
      if(codePointAt == SymbolMap.lf.codes[0]) {
        previousStartIndex = startIndex;
        startIndex = new CodePointIndex(i+1);
        cursorRanges.add(CursorRange.of(previousStartIndex, lineNumber, startIndex, lineNumber));
        lineNumberByIndex.put(startIndex, lineNumber.increments());
      }else if(codePointAt == SymbolMap.cr.codes[0]) {
        if(codePointCount-1!=i && codePoints[i+1] ==SymbolMap.lf.codes[0]) {
          i++;
          previousStartIndex = startIndex;
          startIndex = new CodePointIndex(i+1);
          cursorRanges.add(CursorRange.of(previousStartIndex, lineNumber, startIndex, lineNumber));
          lineNumberByIndex.put(startIndex, lineNumber.increments());
          
          stringIndex = stringIndex.add(1);
          stringIndexByCodePointIndex.put(codePointIndex.add(1), stringIndex);
          codePointIndexByStringIndex.put(stringIndex,codePointIndex.add(1));
        }else {
          previousStartIndex = startIndex;
          startIndex = new CodePointIndex(i+1);
          cursorRanges.add(CursorRange.of(previousStartIndex, lineNumber, startIndex, lineNumber));
          lineNumberByIndex.put(startIndex, lineNumber.increments());
        }
      }
    }

    Cursor start = new CursorImpl();
    CodePointIndex position = new CodePointIndex(codePointCount);
    Cursor end = new CursorImpl().setPosition(position).setLineNumber(lineNumber);
    cursorRange = new CursorRange(start, end);
    cursorRanges.add(CursorRange.of(startIndex, lineNumber, position, lineNumber));
  }
  
  public Size lineSize() {
    return new Size(cursorRanges.size());
  }
  
  public Stream<Source> lines(Source root){
    return cursorRanges.stream()
      .map(root::subSource);
  }
}