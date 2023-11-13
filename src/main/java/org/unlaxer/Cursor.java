package org.unlaxer;

import org.unlaxer.util.NameSpecifier;

public interface Cursor<T extends Cursor<T>> {
  
  public enum CursorKind{
    startInclusive,
    endExclusive
  }

	NameSpecifier getNameSpace();

	T setNameSpace(NameSpecifier nameSpace);

	LineNumber getLineNumber();
	
//	SubLineNumber getLineNumberOnThisSequence();

	T setLineNumber(LineNumber lineNumber);
	
  T incrementLineNumber();

	CodePointIndex getPosition();
	
//	SubCodePointIndex getPositionOnThisSequence();

	T setPosition(CodePointIndex position);
	
  T incrementPosition(); 

	T addPosition(CodePointOffset adding);

	CodePointIndexInLine getPositionInLine();

	T setPositionInLine(CodePointIndexInLine positionInLine);
	
	T resolveLineNumber(RootPositionResolver rootPositionResolver);
	
	CursorKind cursorKind();
	
	public interface EndExclusiveCursor extends Cursor<EndExclusiveCursor>{
	  
	  default CursorKind cursorKind() {
	    return CursorKind.endExclusive;
	  }
	}
	
	public interface StartInclusiveCursor extends Cursor<StartInclusiveCursor>{
    
    default CursorKind cursorKind() {
      return CursorKind.startInclusive;
    }
  }
}