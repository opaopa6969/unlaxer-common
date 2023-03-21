package org.unlaxer;

public class ErrorMessage implements RangedContent<String>{
	
	Range position;
	
	String message;

	public ErrorMessage(Range position, String message) {
		super();
		this.position = position;
		this.message = message;
	}

	@Override
	public Range getRange() {
		return position;
	}

	@Override
	public String getContent() {
		return message;
	}
}