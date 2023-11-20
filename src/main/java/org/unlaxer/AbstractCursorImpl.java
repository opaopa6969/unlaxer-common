package org.unlaxer;

import java.io.Serializable;

import org.unlaxer.util.NameSpecifier;

public abstract class AbstractCursorImpl<T extends Cursor<T>> implements Serializable,Cursor<T> {
	
	private static final long serialVersionUID = -4419856259856233251L;
	
	NameSpecifier nameSpace;
	LineNumber lineNumber;
	CodePointIndex position;
	CodePointIndexInLine positionInLine;
	CursorKind cursorKind;
	
	AbstractCursorImpl(CursorKind cursorKind) {
		super();
		lineNumber = new LineNumber(0);
		position = new CodePointIndex(0);
		positionInLine = new CodePointIndexInLine(0);
		nameSpace = NameSpecifier.of("");
		this.cursorKind = cursorKind;
	}
	public AbstractCursorImpl(Cursor<?> cursor) {
		nameSpace = cursor.getNameSpace();
		lineNumber = cursor.getLineNumber();
		positionInLine = cursor.getPositionInLine();
		position = cursor.getPosition();
	}
	
  abstract T thisObject();
  
  @Override
  public T setNameSpace(NameSpecifier nameSpace) {
    this.nameSpace = nameSpace;
    return thisObject();
  }
  @Override
  public LineNumber getLineNumber() {
    return lineNumber;
  }
  
  @Override
  public T setLineNumber(LineNumber lineNumber) {
    this.lineNumber = lineNumber;
    return thisObject();
  }
  
  @Override
  public CodePointIndex getPosition() {
    return position;
  }
  @Override
  public T setPosition(CodePointIndex position) {
    this.position = position;
    return thisObject();
  }
  @Override
  public T addPosition(CodePointOffset adding) {
    this.position = position.newWithAdd(adding);
    return thisObject();
  }
  @Override
  public CodePointIndexInLine getPositionInLine() {
    return positionInLine;
  }
  @Override
  public T setPositionInLine(CodePointIndexInLine positionInLine) {
    this.positionInLine = positionInLine;
    return thisObject();
  }
  
  @Override
  public T incrementLineNumber() {
    lineNumber = lineNumber.newWithIncrements();
    positionInLine = new CodePointIndexInLine(0);
    return thisObject();
  }
  @Override
  public T incrementPosition() {
    position = position.newWithIncrements();
    positionInLine = positionInLine.newWithIncrements();
    return thisObject();
  }
  
  @Override
  public NameSpecifier getNameSpace() {
    return nameSpace;
  }
  
  @Override
  public CursorKind cursorKind() {
    return cursorKind;
  }
  
  @Override
  public String toString() {
    return "[L:" + lineNumber + ",X:" + getPositionInLine()+",P:"+getPosition()+"]";
  }
}