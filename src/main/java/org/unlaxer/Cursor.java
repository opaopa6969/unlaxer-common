package org.unlaxer;

import org.unlaxer.util.NameSpecifier;

public interface Cursor {

	NameSpecifier getNameSpace();

	Cursor setNameSpace(NameSpecifier nameSpace);

	LineNumber getLineNumber();
	
//	SubLineNumber getLineNumberOnThisSequence();

	Cursor setLineNumber(LineNumber lineNumber);
	
  Cursor incrementLineNumber();

	CodePointIndex getPosition();
	
//	SubCodePointIndex getPositionOnThisSequence();

	Cursor setPosition(CodePointIndex position);
	
  Cursor incrementPosition(); 

	Cursor addPosition(CodePointOffset adding);

	CodePointOffset getPositionInLine();

	Cursor setPositionInLine(CodePointOffset positionInLine);
	
	Cursor resolveLineNumber(RootPositionResolver rootPositionResolver);

}