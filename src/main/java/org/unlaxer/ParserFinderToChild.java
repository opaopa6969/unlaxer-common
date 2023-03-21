package org.unlaxer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.unlaxer.parser.Parser;

public interface ParserFinderToChild extends ParserHierarchy{
	
	public default Optional<Parser> findFirstToChild(Predicate<Parser> predicate) {
		return findToChild(predicate).findFirst();
	}
	
	public default Stream<Parser> findToChild(Predicate<Parser> predicate) {
		List<Parser> flatten = flatten(RecursiveMode.childrenOnly);
		return flatten.stream().filter(predicate);
	}
	
	public default List<Parser> flatten(){
		return flatten(RecursiveMode.containsRoot);
	}
	
	public default List<Parser> flatten(RecursiveMode recursiveMode){
		List<Parser> list = new ArrayList<Parser>();
		if(recursiveMode.isContainsRoot()){
			list.add(getThisParser());
		}
		for(Parser child :getChildren()){
			list.addAll(child.flatten(recursiveMode));
		}
		return list;
	}
}