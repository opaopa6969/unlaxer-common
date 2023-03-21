package org.unlaxer;

import java.util.List;

import org.unlaxer.parser.Parser;

public interface RecursiveTaggable extends Taggable{
	
	public default Parser addTagRecurciveChildrenOnly(Tag... addeds){
		return addTag(true , RecursiveMode.childrenOnly, addeds);
	}
	
	public default Parser addTagRecurcive(Tag... addeds){
		return addTag(true , RecursiveMode.containsRoot, addeds);
	}
	
	public default Parser addTag(boolean recursive ,RecursiveMode recursiveMode , Tag... addeds){
		
		if(recursive){
			List<Parser> flatten = getThisParser().flatten(recursiveMode);
			for (Parser parser : flatten) {
				parser.addTag(addeds);
			}
		}else{
			addTag(addeds);
		}
		return getThisParser();
	}
	
	public default Parser removeTagRecurciveChildrenOnly(Tag... removes){
		return removeTag(true , RecursiveMode.childrenOnly, removes);
	}
	
	public default Parser removeTagRecurcive(Tag... removes){
		return removeTag(true , RecursiveMode.containsRoot , removes);
	}
	
	public default Parser removeTag(boolean recursive , RecursiveMode recursiveMode
			, Tag... removes){
		
		if(recursive){
			List<Parser> flatten = getThisParser().flatten(recursiveMode);
			for (Parser parser : flatten) {
				parser.removeTag(removes);
			}
		}else{
			removeTag(removes);
		}
		return getThisParser();
	}
	
	
	public interface PropagatedTaggable{
		
	}
	
	
}