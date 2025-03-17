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
import org.unlaxer.Source.SourceKind;

public class PositionResolverImpl implements PositionResolver {
  
  final NavigableMap<CodePointIndexFromRoot, LineNumber> lineNumberByIndex = new TreeMap<>();
  final Map<CodePointIndexFromRoot,StringIndex> stringIndexByCodePointIndex = new HashMap<>();
  final Map<CodePointIndexFromRoot,CodePointIndexInLine> codePointIndexInLineByCodePointIndex = new HashMap<>();
  final Map<StringIndex,AttachedCodePointIndex> codePointIndexByStringIndex = new HashMap<>();
  final List<CursorRange> cursorRanges = new ArrayList<>();
  final CursorRange cursorRange;
  final Source rootSource;
  
  
//  final RootPositionResolver rootPositionResolver;
  
//  public static RootPositionResolver createRootPositionResolver(int[] codePoints){
//    return new PositionResolverImpl(codePoints, null, new CodePointOffset(0));
//  }
//  
//  public static SubPositionResolver createSubPositionReslover(
//      int[] codePoints,
//      RootPositionResolver rootPositionResolver,
//      CodePointOffset offsetFromRoot){
//    return new PositionResolverImpl(codePoints, null, new CodePointOffset(0));
//  }

  
  PositionResolverImpl(Source rootSource , int[] codePoints){
    this.rootSource = rootSource ;
//      RootPositionResolver rootPositionResolver,
//      CodePointOffset offsetFromRoot) {
//    boolean isRoot = rootPositionResolver == null ;
    int codePointCount = codePoints.length;
//    this.rootPositionResolver = isRoot ? this : rootPositionResolver;
    
    CodePointOffset offsetFromRoot = new CodePointOffset(0);
    
    LineNumber lineNumber = new LineNumber(0);
<<<<<<< HEAD
    CodePointIndex startIndex = new CodePointIndex(0, offsetFromRoot);
    CodePointIndex previousStartIndex;
    lineNumberByIndex.put(startIndex, lineNumber);
    
    StringIndex stringIndex = new StringIndex(0);
    CodePointIndex codePointIndex = new CodePointIndex(0, offsetFromRoot);
    CodePointIndexInLine codePointOffsetInline = new CodePointIndexInLine(0);
    
    for (int i = 0; i < codePointCount; i++) {
      codePointIndex = new CodePointIndex(i , offsetFromRoot);
=======
    AttachedCodePointIndex startIndex = new AttachedCodePointIndex(0,rootSource);
    AttachedCodePointIndex previousStartIndex;
    lineNumberByIndex.put(startIndex, lineNumber);
    
    StringIndex stringIndex = new StringIndex(0);
    AttachedCodePointIndex codePointIndex = new AttachedCodePointIndex(0,rootSource);
    CodePointIndexInLine codePointOffsetInline = new CodePointIndexInLine(0);
    
    for (int i = 0; i < codePointCount; i++) {
      codePointIndex = new AttachedCodePointIndex(i,rootSource);
>>>>>>> 6614c86145cd00541269b18010372831b907fbda
      stringIndexByCodePointIndex.put(codePointIndex, stringIndex);
      codePointIndexByStringIndex.put(stringIndex,codePointIndex);
      codePointIndexInLineByCodePointIndex.put(codePointIndex, codePointOffsetInline);
    
      int codePointAt = codePoints[i];
      
      int adding = Character.isBmpCodePoint(codePointAt) ? 1:2;
      stringIndex = stringIndex.newWithAdd(adding);
      
      if(codePointAt == SymbolMap.lf.codes[0]) {
        
        previousStartIndex = startIndex;
<<<<<<< HEAD
        startIndex = new CodePointIndex(i+1 , offsetFromRoot);
=======
        startIndex = new AttachedCodePointIndex(i+1,rootSource);
>>>>>>> 6614c86145cd00541269b18010372831b907fbda
        cursorRanges.add(
            CursorRange.of(
              previousStartIndex,
              startIndex,
              CodePointOffset.ZERO,
              SourceKind.subSource,
              this
            )
        );
        lineNumber = lineNumber.newWithIncrements();
        lineNumberByIndex.put(startIndex, lineNumber);
        codePointOffsetInline = new CodePointIndexInLine(0);
        continue;
        
      }else if(codePointAt == SymbolMap.cr.codes[0]) {
        
        if(codePointCount-1!=i && codePoints[i+1] ==SymbolMap.lf.codes[0]) {
          i++;
          previousStartIndex = startIndex;
<<<<<<< HEAD
          startIndex = new CodePointIndex(i+1 , offsetFromRoot);
=======
          startIndex = new AttachedCodePointIndex(i+1 , rootSource);
>>>>>>> 6614c86145cd00541269b18010372831b907fbda
          cursorRanges.add(
              CursorRange.of(
                previousStartIndex,
                startIndex,
                CodePointOffset.ZERO,
                SourceKind.subSource,
                this
              )
          );
          lineNumber = lineNumber.newWithIncrements();
          lineNumberByIndex.put(startIndex, lineNumber);
          
          stringIndex = stringIndex.newWithAdd(1);
          stringIndexByCodePointIndex.put(codePointIndex.newWithAdd(1), stringIndex);
          codePointIndexByStringIndex.put(stringIndex,codePointIndex.newWithAdd(1));
        }else {
          previousStartIndex = startIndex;
<<<<<<< HEAD
          startIndex = new CodePointIndex(i+1 , offsetFromRoot);
=======
          startIndex = new AttachedCodePointIndex(i+1,rootSource);
>>>>>>> 6614c86145cd00541269b18010372831b907fbda
          cursorRanges.add(
              CursorRange.of(
                previousStartIndex,
                startIndex,
                CodePointOffset.ZERO,
                SourceKind.subSource,
                this
              )
          );
          lineNumber = lineNumber.newWithIncrements();
          lineNumberByIndex.put(startIndex, lineNumber);
        }
        codePointOffsetInline = new CodePointIndexInLine(0);
        continue;
      }
      codePointOffsetInline = codePointOffsetInline.newWithIncrements();
    }

    StartInclusiveCursor start = new StartInclusiveCursorImpl(SourceKind.root,this);//.addPosition(offsetFromRoot);
    
<<<<<<< HEAD
    CodePointIndex position = new CodePointIndex(codePointCount , offsetFromRoot);//.newWithAdd(offsetFromRoot);
=======
    AttachedCodePointIndex position = new AttachedCodePointIndex(codePointCount,rootSource);//.newWithAdd(offsetFromRoot);
>>>>>>> 6614c86145cd00541269b18010372831b907fbda
    
    EndExclusiveCursor end = new EndExclusiveCursorImpl(SourceKind.root,this)
        .setPosition(position);
    cursorRange = new CursorRange(start, end);
    codePointIndexInLineByCodePointIndex.put(position, new CodePointIndexInLine(0));
    lineNumberByIndex.put(position, lineNumber);
    if(cursorRanges.size()>0) {
      CursorRange last = cursorRanges.get(cursorRanges.size()-1);
      if(last.lessThan(codePointIndex) && startIndex.lessThan(position)) {
        
        cursorRanges.add(
            CursorRange.of(
              startIndex,
              position,
              CodePointOffset.ZERO,
              SourceKind.subSource,
              this
            )
        );
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
  public StringIndex stringIndexInRootFrom(CodePointIndexFromRoot codePointIndex) {
    
//    if(rootPositionResolver == this) {
//      return stringIndexByCodePointIndex.get(codePointIndexInSubSource);
//    }
//    return rootPositionResolver.stringIndexInRootFrom(codePointIndexInSubSource.newWithPlus(offsetFromRoot));
    return stringIndexByCodePointIndex.get(codePointIndex);
  }

  @Override
  public LineNumber lineNumberFrom(CodePointIndexFromRoot codePointIndex) {
//    return rootPositionResolver.lineNumberFrom(codePointIndex.newWithPlus(offsetFromRoot));
    return lineNumberByIndex.floorEntry(codePointIndex).getValue();
  }

//  @Override
//  public CodePointIndex codePointIndexFrom(StringIndex stringIndex) {
//    return rootPositionResolver.codePointIndexFrom(stringIndex);
//  }

  @Override
  public CursorRange rootCursorRange() {
    return cursorRange;
  }

  @Override
  public StringIndex subStringIndexFrom(CodePointIndexFromRoot subCodePointIndex) {
    return stringIndexByCodePointIndex.get(subCodePointIndex);
  }

  @Override
  public CodePointIndexInLine codePointIndexInLineFrom(CodePointIndexFromRoot codePointIndex) {
    return codePointIndexInLineByCodePointIndex.get(codePointIndex);
  }

  @Override
  public AttachedCodePointIndex rootCodePointIndexFrom(StringIndex stringIndex) {
    return codePointIndexByStringIndex.get(stringIndex);
  }

  @Override
  public Source rootSource() {
    return rootSource;
  }
  
}