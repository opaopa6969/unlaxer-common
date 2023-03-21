package org.unlaxer;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.unlaxer.parser.Parser;

public interface ParserFinderFromRoot extends ParserHierarchy{
	
	public default Optional<Parser> findFirstFromRoot(Predicate<Parser> predicate) {
		return findFromRoot(predicate).findFirst();
	}
	
	public default Stream<Parser> findFromRoot(Predicate<Parser> predicate) {
		List<Parser> flatten = getRoot().flatten();
		return flatten.stream().filter(predicate);
	}
	
}