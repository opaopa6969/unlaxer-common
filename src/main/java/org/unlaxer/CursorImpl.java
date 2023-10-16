package org.unlaxer;

import java.io.Serializable;

import org.unlaxer.util.NameSpecifier;

public class CursorImpl implements Serializable, Cursor{
	
	private static final long serialVersionUID = -4419856259856233251L;
	
	private NameSpecifier nameSpace;
	private LineNumber lineNumber;
	private Index position;
	private Index positionInLine;
	
	public CursorImpl() {
		super();
		lineNumber = new LineNumber(0);
		position = new Index(0);
		positionInLine = new Index(0);
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
	public Cursor setNameSpace(NameSpecifier nameSpace) {
		this.nameSpace = nameSpace;
		return this;
	}
	@Override
	public LineNumber getLineNumber() {
		return lineNumber;
	}
	
	@Override
	public Cursor setLineNumber(LineNumber lineNumber) {
		this.lineNumber = lineNumber;
		return this;
	}
	
	@Override
	public Index getPosition() {
		return position;
	}
	@Override
	public Cursor setPosition(Index position) {
		this.position = position;
		return this;
	}
	@Override
	public Cursor addPosition(Index adding) {
		this.position = new Index(this.position.value + adding.value);
		return this;
	}
	@Override
	public Index getPositionInLine() {
		return positionInLine;
	}
	@Override
	public Cursor setPositionInLine(Index positionInLine) {
		this.positionInLine = positionInLine;
		return this;
	}
	
  @Override
  public String toString() {
    return "[L:" + lineNumber + ",X:" + getPositionInLine()+",P:"+getPosition()+"]";
  }
  @Override
  public Cursor incrementLineNumber() {
    lineNumber = new LineNumber(lineNumber.value+1);
    positionInLine = new Index(0);
    return this;
  }
  @Override
  public Cursor incrementPosition() {
    position = new Index(position.value+1);
    positionInLine = new Index(positionInLine.value+1);
    return this;
  }
}