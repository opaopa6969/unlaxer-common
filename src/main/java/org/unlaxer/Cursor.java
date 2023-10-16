package org.unlaxer;

import org.unlaxer.util.NameSpecifier;

public interface Cursor {

	NameSpecifier getNameSpace();

	Cursor setNameSpace(NameSpecifier nameSpace);

	LineNumber getLineNumber();

	Cursor setLineNumber(LineNumber lineNumber);
	
  Cursor incrementLineNumber();

	Index getPosition();

	Cursor setPosition(Index position);
	
  Cursor incrementPosition(); 

	Cursor addPosition(Index adding);

	Index getPositionInLine();

	Cursor setPositionInLine(Index positionInLine);

}