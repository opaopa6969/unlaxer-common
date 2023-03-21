package org.unlaxer.parser;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public interface LazyOccursParserSpecifier extends LazyInstance{
	
	public Supplier<Parser> getLazyParser();
	
	public Optional<Parser> getLazyTerminatorParser();
	
	@Override
	public default void prepareChildren(List<Parser> childrenContainer) {
		
		if(childrenContainer.isEmpty()){
			childrenContainer.add(getLazyParser().get());
			getLazyTerminatorParser().ifPresent(childrenContainer::add);
		}
	}

	
}