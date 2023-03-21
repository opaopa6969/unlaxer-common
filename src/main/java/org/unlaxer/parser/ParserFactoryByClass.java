package org.unlaxer.parser;

import java.util.HashMap;
import java.util.Map;

import org.unlaxer.util.FactoryBoundCache;

public class ParserFactoryByClass{
	
	static FactoryBoundCache<Class<? extends Parser>, Parser>//
		singletonsByClass = new FactoryBoundCache<>((clazz) -> {
			try {
				Parser parser = clazz.getDeclaredConstructor().newInstance();
				return parser;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	
	@SuppressWarnings("unchecked")
	public static <T extends Parser> T get(Class<T> clazz) {
		T parser = (T) singletonsByClass.get(clazz);
		if(false == initialized(clazz)) {
			initilizedByClass.put(clazz, true);
			parser.initialize();
		}
		return parser;
	}
	
	static Map<Class<? extends Parser> , Boolean> 
		initilizedByClass = new HashMap<>();
	
	static boolean initialized(Class<? extends Parser> clazz) {
		return initilizedByClass.getOrDefault(clazz, false);
	}
}