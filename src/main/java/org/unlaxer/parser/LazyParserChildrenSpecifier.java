package org.unlaxer.parser;

import java.util.List;
import java.util.Optional;

import org.unlaxer.RecursiveMode;
import org.unlaxer.reducer.TagBasedReducer.NodeKind;

public interface LazyParserChildrenSpecifier extends LazyInstance {
	
	public List<Parser> getLazyParsers();

	@Override
	public default void prepareChildren(List<Parser> childrenContainer) {
		
		if(childrenContainer.isEmpty()){
			List<Parser> lazyParsers = getLazyParsers();
//			if(lazyParsers == null) {
//				initialize();
//				lazyParsers = getLazyParsers();
//			}
			childrenContainer.addAll(lazyParsers);
			
			Optional<RecursiveMode> notAstNodeSpecifier = getNotAstNodeSpecifier();
			notAstNodeSpecifier.ifPresent(recursiveMode->{
				if(recursiveMode.isContainsRoot()){
					addTag(NodeKind.notNode.getTag());
				}
				addTagRecurciveChildrenOnly(NodeKind.notNode.getTag());
			});
		}
	}
	
	public Optional<RecursiveMode> getNotAstNodeSpecifier();
}

