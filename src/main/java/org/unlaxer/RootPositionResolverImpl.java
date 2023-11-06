package org.unlaxer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Stream;

import org.unlaxer.Cursor.EndExclusiveCursor;
import org.unlaxer.Cursor.StartInclusiveCursor;

public class RootPositionResolverImpl implements RootPositionResolver{
  
  final NavigableMap<CodePointIndex, LineNumber> lineNumberByIndex = new TreeMap<>();
  final Map<CodePointIndex,StringIndex> stringIndexByCodePointIndex = new HashMap<>();
  final Map<StringIndex,CodePointIndex> codePointIndexByStringIndex = new HashMap<>();
  final List<CursorRange> cursorRanges = new ArrayList<>();
  final CursorRange cursorRange;
  
  public RootPositionResolverImpl(int[] codePoints) {
    int codePointCount = codePoints.length;
    
    LineNumber lineNumber = new LineNumber(0);
    CodePointIndex index = new CodePointIndex(0);
    CodePointIndex startIndex = new CodePointIndex(0);
    CodePointIndex previousStartIndex;
    lineNumberByIndex.put(index, lineNumber);
    
    StringIndex stringIndex = new StringIndex(0);
    CodePointIndex codePointIndex = new CodePointIndex(0);
    
    for (int i = 0; i < codePointCount; i++) {
      codePointIndex = codePointIndex.newWithAdd(i);
      stringIndexByCodePointIndex.put(codePointIndex, stringIndex);
      codePointIndexByStringIndex.put(stringIndex,codePointIndex);
    
      int codePointAt = codePoints[i];
      
      int adding = Character.isBmpCodePoint(codePointAt) ? 1:2;
      stringIndex = stringIndex.newWithAdd(adding);
      
      if(codePointAt == SymbolMap.lf.codes[0]) {
        previousStartIndex = startIndex;
        startIndex = new CodePointIndex(i+1);
        cursorRanges.add(CursorRange.of(previousStartIndex, lineNumber, startIndex, lineNumber));
        lineNumber = lineNumber.newWithIncrements();
        lineNumberByIndex.put(startIndex, lineNumber);
      }else if(codePointAt == SymbolMap.cr.codes[0]) {
        if(codePointCount-1!=i && codePoints[i+1] ==SymbolMap.lf.codes[0]) {
          i++;
          previousStartIndex = startIndex;
          startIndex = new CodePointIndex(i+1);
          cursorRanges.add(CursorRange.of(previousStartIndex, lineNumber, startIndex, lineNumber));
          lineNumber = lineNumber.newWithIncrements();
          lineNumberByIndex.put(startIndex, lineNumber);
          
          stringIndex = stringIndex.newWithAdd(1);
          stringIndexByCodePointIndex.put(codePointIndex.newWithAdd(1), stringIndex);
          codePointIndexByStringIndex.put(stringIndex,codePointIndex.newWithAdd(1));
        }else {
          previousStartIndex = startIndex;
          startIndex = new CodePointIndex(i+1);
          cursorRanges.add(CursorRange.of(previousStartIndex, lineNumber, startIndex, lineNumber));
          lineNumber = lineNumber.newWithIncrements();
          lineNumberByIndex.put(startIndex, lineNumber);
        }
      }
    }

    StartInclusiveCursor start = new StartInclusiveCursorImpl();
    CodePointIndex position = new CodePointIndex(codePointCount);
    EndExclusiveCursor end = new EndExclusiveCursorImpl().setPosition(position).setLineNumber(lineNumber);
    cursorRange = new CursorRange(start, end);
    if(cursorRanges.size()>0) {
      CursorRange last = cursorRanges.get(cursorRanges.size()-1);
      if(last.lessThan(codePointIndex) && startIndex.lessThan(position)) {
        cursorRanges.add(CursorRange.of(startIndex, lineNumber, position, lineNumber));
      }
    }
  }
  
  @Override
  public Size lineSize() {
    return new Size(cursorRanges.size());
  }
  
  @Override
  public Stream<Source> lines(Source root){
    return cursorRanges.stream()
      .map(root::subSource);
  }

  @Override
  public StringIndex stringIndexFrom(CodePointIndex codePointIndex) {
    return stringIndexByCodePointIndex.get(codePointIndex);
  }

  @Override
  public LineNumber lineNumberFrom(CodePointIndex codePointIndex) {
    return lineNumberByIndex.floorEntry(codePointIndex).getValue();
  }

  @Override
  public CodePointIndex codePointIndexFrom(StringIndex stringIndex) {
    return codePointIndexByStringIndex.get(stringIndex);
  }

  @Override
  public CursorRange rootCursorRange() {
    return cursorRange;
  }
}