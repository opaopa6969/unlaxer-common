package org.unlaxer.parser.elementary;

import org.unlaxer.Name;
import org.unlaxer.parser.StaticParser;

public class WildCardStringWithoutParser extends SingleStringParser implements StaticParser {

	private static final long serialVersionUID = -3386398191774012367L;
	
	String[] excludes;

	public WildCardStringWithoutParser(String joinedExcludes) {
		this(null,joinedExcludes);
	}
	
	public WildCardStringWithoutParser(String... excludes) {
		super();
		this.excludes = excludes;
		checkSingleString();
	}


	public WildCardStringWithoutParser(Name name, String joinedExcludes) {
		super(name);
		int length = joinedExcludes.length();
		excludes = new String[length];
		for(int i = 0 ; i < length ; i++){
			excludes[i] = joinedExcludes.substring(i, i+1);
		}
	}
	
	public WildCardStringWithoutParser(Name name, String... excludes) {
		super(name);
		this.excludes = excludes;
		checkSingleString();
	}
	
	void checkSingleString() {
		for (String string : excludes) {
			if(string.length() != 1) {
				throw new IllegalArgumentException("must be specify single length string");
			}
		}
	}


	@Override
	public boolean isMatch(String target) {
		for(String exclude : excludes){
			if(exclude.equals(target)){
				return false;
			}
		}
		return true;
	}
}