package org.unlaxer;

import java.io.Serializable;

import org.unlaxer.util.NameSpecifier;

public class CursorImpl implements Serializable, Cursor{
	
	private static final long serialVersionUID = -4419856259856233251L;
	
	private NameSpecifier nameSpace;
	private LineNumber lineNumber;
	private CodePointIndex position;
	private CodePointOffset positionInLine;
	
	public CursorImpl() {
		super();
		lineNumber = new LineNumber(0);
		position = new CodePointIndex(0);
		positionInLine = new CodePointOffset(0);
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
	public CodePointIndex getPosition() {
		return position;
	}
	@Override
	public Cursor setPosition(CodePointIndex position) {
		this.position = position;
		return this;
	}
	@Override
	public Cursor addPosition(CodePointOffset adding) {
		this.position = position.add(adding);
		return this;
	}
	@Override
	public CodePointOffset getPositionInLine() {
		return positionInLine;
	}
	@Override
	public Cursor setPositionInLine(CodePointOffset positionInLine) {
		this.positionInLine = positionInLine;
		return this;
	}
	
  @Override
  public String toString() {
    return "[L:" + lineNumber + ",X:" + getPositionInLine()+",P:"+getPosition()+"]";
  }
  @Override
  public Cursor incrementLineNumber() {
    lineNumber = lineNumber.increments();
    positionInLine = new CodePointOffset(0);
    return this;
  }
  @Override
  public Cursor incrementPosition() {
    position = position.increments();
    positionInLine = positionInLine.increments();
    return this;
  }
  @Override
  public Cursor resolveLineNumber(RootPositionResolver rootPositionResolver) {
    setLineNumber(rootPositionResolver.lineNumberFrom(position));
    return this;
  }
}