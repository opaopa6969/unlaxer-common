package org.unlaxer.util;

import org.unlaxer.CodePointIndex;
import org.unlaxer.CodePointLength;
import org.unlaxer.CodePointOffset;
import org.unlaxer.Range;
import org.unlaxer.Source;

public class SourceUtil {
	
	public static Source newWithDelete(Source word, CodePointIndex position){
	  CodePointOffset offsetFromRoot = position.offsetFromRoot();
		int length = word.length();
		if(position.isNegative() || position.newWithAdd(1).gt(length)){
			return word;
		}
		return word.subSource(new CodePointIndex(0,offsetFromRoot),position).concat(word.subSource(position.newWithPlus(1)));
	}
	
	public static Source newWithDelete(Source word, Range range){
	  CodePointOffset offsetFromRoot = word.offsetFromRoot();
		CodePointLength length = word.codePointLength();
		CodePointIndex startIndexInclusive = new CodePointIndex(range.startIndexInclusive,offsetFromRoot);
		CodePointIndex endIndexExclusive = new CodePointIndex(range.endIndexExclusive,offsetFromRoot);
		SimpleBuilder builder = new SimpleBuilder();
		
		if(startIndexInclusive.isGreaterThanZero()){
			builder.append(word.subSource(new CodePointIndex(0,offsetFromRoot),startIndexInclusive));
		}
		if(endIndexExclusive.le(length)){
			builder.append(word.subSource(endIndexExclusive));
		}
		return builder.toSource();
	}
	
	public static Source newWithDeleteAndInsert(Source word, CodePointIndex position ,Source insertion){
		
		return newWithDeleteAndInsert(word, new Range(position , position.newWithPlus(1)), insertion);
	}

	
	public static Source newWithDeleteAndInsert(Source word, Range range ,Source insertion){
	  CodePointOffset offsetFromRoot = word.offsetFromRoot();
    CodePointLength length = word.codePointLength();
    CodePointIndex startIndexInclusive = new CodePointIndex(range.startIndexInclusive,offsetFromRoot);
    CodePointIndex endIndexExclusive = new CodePointIndex(range.endIndexExclusive,offsetFromRoot);
		SimpleBuilder builder = new SimpleBuilder();
		
		if(startIndexInclusive.isGreaterThanZero()){
			builder.append(word.subSource(new CodePointIndex(0,offsetFromRoot),startIndexInclusive));
		}
		
		builder.append(insertion);
		if(endIndexExclusive.le(length)){
			builder.append(word.subSource(endIndexExclusive));
		}
		return builder.toSource();
	}

	
	public static Source newWithInsert(Source base , Source insertion , CodePointIndex position){
	  
	  CodePointOffset offsetFromRoot = base.offsetFromRoot();
		
		if(base.isEmpty()){
			return insertion;
		}
		
		SimpleBuilder builder = new SimpleBuilder();
		if(position.isGreaterThanZero()){
			builder.append(base.subSource(new CodePointIndex(0,offsetFromRoot),position));
		}
		builder.append(insertion);
		
		if(position.lt(base.length())){
			
			builder.append(base.subSource(position));
		}
		return builder.toSource();
	}
}
