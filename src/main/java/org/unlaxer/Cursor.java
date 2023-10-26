package org.unlaxer;

import org.unlaxer.util.NameSpecifier;

public interface Cursor {

	NameSpecifier getNameSpace();

	Cursor setNameSpace(NameSpecifier nameSpace);

	LineNumber getLineNumber();

	Cursor setLineNumber(LineNumber lineNumber);
	
  Cursor incrementLineNumber();

	CodePointIndex getPosition();

	Cursor setPosition(CodePointIndex position);
	
  Cursor incrementPosition(); 

	Cursor addPosition(CodePointOffset adding);

	CodePointOffset getPositionInLine();

	Cursor setPositionInLine(CodePointOffset positionInLine);

}