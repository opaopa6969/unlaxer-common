package org.unlaxer.parser;

import java.util.List;
import java.util.function.Supplier;

public interface LazyParserChildSpecifier extends LazyInstance{
	
	public Supplier<Parser> getLazyParser();
	
	@Override
	public default void prepareChildren(List<Parser> childrenContainer) {
		
		
		if(childrenContainer.isEmpty()){
			childrenContainer.add(getLazyParser().get());
		}
	}
}