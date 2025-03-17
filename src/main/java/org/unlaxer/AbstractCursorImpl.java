package org.unlaxer;

import java.io.Serializable;

import org.unlaxer.Source.SourceKind;

public abstract class AbstractCursorImpl<T extends Cursor<T>> implements Serializable,Cursor<T> {
	
	private static final long serialVersionUID = -4419856259856233251L;
	
	AttachedCodePointIndex position;
	final CursorKind cursorKind;
	final SourceKind sourceKind;
	final PositionResolver positionResolver;
//	final CodePointOffset offsetFromRoot;
	
	AbstractCursorImpl(CursorKind cursorKind , SourceKind sourceKind , PositionResolver positionResolver) {
<<<<<<< HEAD
		this(cursorKind,sourceKind,positionResolver,new CodePointIndex(0 , new CodePointOffset(0)) );
	}
	
  AbstractCursorImpl(CursorKind cursorKind , SourceKind sourceKind , 
	     PositionResolver positionResolver , CodePointIndex position) {
=======
		this(cursorKind,sourceKind,positionResolver,
		    new AttachedCodePointIndex(0,positionResolver.rootSource()) , new CodePointOffset(0));
	}
	
  AbstractCursorImpl(CursorKind cursorKind , SourceKind sourceKind , 
	     PositionResolver positionResolver , AttachedCodePointIndex position , CodePointOffset offsetFromRoot) {
>>>>>>> 6614c86145cd00541269b18010372831b907fbda
	    super();
	    this.cursorKind = cursorKind;
	    this.sourceKind = sourceKind;
	    this.positionResolver = positionResolver;
	    this.position = position;
  }
	
	public AbstractCursorImpl(Cursor<?> cursor) {
		position = cursor.position();
		cursorKind = cursor.cursorKind();
		sourceKind = cursor.sourceKind();
		positionResolver = cursor.positionResolver();
	}
	
  abstract T thisObject();
  
  @Override
  public LineNumber lineNumber() {
    return positionResolver.lineNumberFrom(position);
  }
  
  
  @Override
<<<<<<< HEAD
  public CodePointIndex position() {
=======
  public AttachedCodePointIndex position() {
>>>>>>> 6614c86145cd00541269b18010372831b907fbda
    return position;
  }
  
//  @Override
//  public CodePointIndex positionInSub() {
//    return sourceKind.isRoot() ?
//        position:
//        position.newWithMinus(offsetFromRoot());
//  }
//  
//  @Override
//  public CodePointIndex positionInRoot() {
//    return position;
//  }

  
  @Override
  public T setPosition(AttachedCodePointIndex position) {
    this.position = position;
    return thisObject();
  }
  @Override
  public T addPosition(CodePointOffset adding) {
    this.position = position.newWithAdd(adding);
    return thisObject();
  }
  @Override
  public CodePointIndexInLine positionInLine() {
    return positionResolver.codePointIndexInLineFrom(position);
  }
  
  @Override
  public T incrementPosition() {
    position = position.newWithIncrements();
    return thisObject();
  }
  
  @Override
  public CursorKind cursorKind() {
    return cursorKind;
  }
  
  @Override
  public PositionResolver positionResolver() {
    return positionResolver;
  }

  @Override
  public SourceKind sourceKind() {
    return sourceKind;
  }
  
  
  
  @Override
  public CodePointOffset offsetFromRoot() {
    return position.offsetFromRoot();
  }

  @Override
  public String toString() {
    return "[L:" + lineNumber() + ",X:" + positionInLine()+",PS:"+position.valueInSubSource()+",PR:"+position.valueWithOffsetFromRoot()+",O:"+offsetFromRoot().value()+"]";
  }
}