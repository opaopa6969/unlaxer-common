package org.unlaxer.listener;

public enum OutputLevel{
	none,
	simple,
	detail,
	;
	public boolean isNone(){
		return this == none;
	}
	public boolean isSimple(){
		return this == simple;
	}
	public boolean isDetail(){
		return this == detail;
	}
}