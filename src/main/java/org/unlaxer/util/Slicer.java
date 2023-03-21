package org.unlaxer.util;

import java.util.function.Function;
import java.util.function.Supplier;

import org.unlaxer.Range;

public class Slicer implements Supplier<String>{
	
	private String word;
	
	private int beginIndexInclusive;
	private int endIndexExclusive;
	private int step;
	

	public Slicer(String word) {
		super();
		this.word = word;
		beginIndexInclusive = 0;
		endIndexExclusive = word.length();
		step=1;
	}
	
	
	/**
	 * @param beginIndexInclusive if beginIndexInclusive less than 0 then position relative from tail
	 *        (like python slice style)
	 * @return this object
	 */
	public Slicer begin(int beginIndexInclusive){
		this.beginIndexInclusive = beginIndexInclusive < 0 ?
				word.length() + beginIndexInclusive:
				beginIndexInclusive;
		return this;
	}
	
	public Slicer begin(Function<String,Integer> positionSpecifier){
		beginIndexInclusive = positionSpecifier.apply(word);
		return this;
	}
	
	/**
	 * @param endIndexExclusive if endIndexExclusive less than 0 then position relative from tail
	 *        (like python slice style)
	 * @return this object
	 */
	public Slicer end(int endIndexExclusive){
		this.endIndexExclusive = endIndexExclusive < 0 ?
				word.length() + endIndexExclusive:
				endIndexExclusive;
		return this;
	}
	
	public Slicer end(Function<String,Integer> positionSpecifier){
		endIndexExclusive = positionSpecifier.apply(word);
		return this;
	}
	
	public Slicer range(Function<String,Range> rangeSpecifier){
		Range range = rangeSpecifier.apply(word);
		begin(range.startIndexInclusive);
		end(range.endIndexExclusive);
		return this;
	}
	
	public Slicer step(int step){
		this.step = step;
		return this;
	}
	
	public Slicer invalidate(){
		begin(0);
		end(0);
		return this;
	}

	// TODO improve performance see AbstractStringBuilder#reverse
	@Override
	public String get() {
		
		if(step == 1){
			
			return word.substring(beginIndexInclusive, endIndexExclusive);
		}else if(step ==0){
			
			return "";
		}
		int start = step < 0 ? endIndexExclusive-1 : beginIndexInclusive;
		int end = step < 0 ? beginIndexInclusive : endIndexExclusive ;
		StringBuilder builder = new StringBuilder();
		if(step < 0){
			
			for(int i = start ; i >= end ; i = i + step){
				builder.append(word.substring(i,i+1));
			}
		}else{
			
			for(int i = start ; i < end ; i = i + step){
				builder.append(word.substring(i,i+1));
			}
		}
		return builder.toString();
	}
	
	public String reverse() {
		return reverse(true);
	}
	
	public String reverse(boolean reverse) {
		
		return reverse ? //
				new StringBuilder(get()).reverse().toString():
				get();
	}
	
	public int length(){
		return word.length();
	}
	
	public Slicer pythonian(String colonSeparatedValue){
		
		String[] splits = colonSeparatedValue.split(":",3);
		if(splits.length>2 && false ==splits[2].isEmpty()){
			step(Integer.parseInt(splits[2]));
		}
		if(splits.length>1 && false ==splits[1].isEmpty()){
			end(Integer.parseInt(splits[1]));
		}
		if(false ==splits[0].isEmpty()){
			begin(Integer.parseInt(splits[0]));
		}
		return this;
	}
}