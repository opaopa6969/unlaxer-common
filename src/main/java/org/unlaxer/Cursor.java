package org.unlaxer;

import org.unlaxer.util.NameSpecifier;

public interface Cursor {

	NameSpecifier getNameSpace();

	void setNameSpace(NameSpecifier nameSpace);

	int getLineNumber();

	void setLineNumber(int lineNumber);

	int getPosition();

	void setPosition(int position);

	void addPosition(int adding);

	int getPositionInLine();

	void setPositionInLine(int positionInLine);

}