package org.unlaxer;

import org.unlaxer.util.NameSpecifier;

public interface Cursor {

	NameSpecifier getNameSpace();

	Cursor setNameSpace(NameSpecifier nameSpace);

	LineNumber getLineNumber();

	Cursor setLineNumber(LineNumber lineNumber);
	
  Cursor incrementLineNumber();

	CodePointIndex getPosition();

	Cursor setPosition(Index position);
	
  Cursor incrementPosition(); 

	Cursor addPosition(Index adding);

	CodePointOffset getPositionInLine();

	Cursor setPositionInLine(Index positionInLine);

}