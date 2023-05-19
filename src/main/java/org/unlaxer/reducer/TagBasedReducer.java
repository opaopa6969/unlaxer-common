package org.unlaxer.reducer;

import org.unlaxer.Tag;
import org.unlaxer.Taggable;
import org.unlaxer.parser.Parser;

public class TagBasedReducer extends AbstractTokenReducer {
	
	
	public enum NodeKind {
		node,
		notNode,
		;
		
		public NodeKind of(String value){
			if(value == null || "".equals(value)){
				return node;
			}
			for(NodeKind nodeKind : values()){
				if(nodeKind.name().equals(value)){
					return nodeKind;
				}
			}
			throw new IllegalArgumentException();
		}
		
		public Tag getTag(){
			return Tag.of(this);
		}
		
		public void addTag(Taggable taggable){
			taggable.addTag(getTag());
		}
		
		public void removeTag(Taggable taggable){
			taggable.removeTag(getTag());
		}
	}


	@Override
	public boolean doReduce(Parser parser) {
		return parser.hasTag(NodeKind.notNode.getTag());
	}


}
