package org.unlaxer;

import java.io.Serializable;

import org.unlaxer.util.NameSpecifier;

public class CursorImpl implements Serializable, Cursor{
	
	private static final long serialVersionUID = -4419856259856233251L;
	
	private NameSpecifier nameSpace;
	private int lineNumber;
	private int position;
	private int positionInLine;
	
	public CursorImpl() {
		super();
		lineNumber =0;
		position = 0;
		positionInLine = 0;
		nameSpace =NameSpecifier.of("");
	}
	public CursorImpl(Cursor cursor) {
		nameSpace = cursor.getNameSpace();
		lineNumber = cursor.getLineNumber();
		positionInLine = cursor.getPositionInLine();
		position = cursor.getPosition();
	}
	@Override
	public NameSpecifier getNameSpace() {
		return nameSpace;
	}
	@Override
	public void setNameSpace(NameSpecifier nameSpace) {
		this.nameSpace = nameSpace;
	}
	@Override
	public int getLineNumber() {
		return lineNumber;
	}
	@Override
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	@Override
	public int getPosition() {
		return position;
	}
	@Override
	public void setPosition(int position) {
		this.position = position;
	}
	@Override
	public void addPosition(int adding) {
		this.position += adding;
	}
	@Override
	public int getPositionInLine() {
		return positionInLine;
	}
	@Override
	public void setPositionInLine(int positionInLine) {
		this.positionInLine = positionInLine;
	}
	
  @Override
  public String toString() {
    return "[L:" + lineNumber + ",X:" + getPositionInLine()+",P:"+getPosition()+"]";
  }
}