package org.unlaxer;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class IndexAndLineNumber{
  
  public final NavigableMap<CodePointIndex, LineNumber> lineNumberByIndex = new TreeMap<>();
  public final Map<CodePointIndex,StringIndex> stringIndexByCodePointIndex = new HashMap<>();
  public final Map<StringIndex,CodePointIndex> codePointIndexByStringIndex = new HashMap<>();
  public final CursorRange cursorRange;
  
  public IndexAndLineNumber(int[] codePoints) {
    int codePointCount = codePoints.length;
    
    LineNumber lineNumber = new LineNumber(0);
    CodePointIndex index = new CodePointIndex(0);
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
        lineNumberByIndex.put(new CodePointIndex(i+1), lineNumber.increments());
      }else if(codePointAt == SymbolMap.cr.codes[0]) {
        if(codePointCount-1!=i && codePoints[i+1] ==SymbolMap.lf.codes[0]) {
          i++;
          lineNumberByIndex.put(new CodePointIndex(i+1), lineNumber.increments());
          
          stringIndex = stringIndex.add(1);
          stringIndexByCodePointIndex.put(codePointIndex.add(1), stringIndex);
          codePointIndexByStringIndex.put(stringIndex,codePointIndex.add(1));
        }else {
          lineNumberByIndex.put(new CodePointIndex(i+1), lineNumber.increments());
        }
      }
    }
    Cursor start = new CursorImpl();
    CodePointIndex position = new CodePointIndex(codePointCount);
    Cursor end = new CursorImpl().setPosition(position).setLineNumber(lineNumber);
    cursorRange = new CursorRange(start, end);
  }
}