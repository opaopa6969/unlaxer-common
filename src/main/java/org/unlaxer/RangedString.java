package org.unlaxer;
import java.util.Optional;

public class RangedString{
	
	public final Range range;
	public Optional<String> token;
	
	public RangedString(Range range, Optional<String> token) {
		super();
		this.range = range;
		this.token = token;
	}
	
	public RangedString(Range range, String token) {
		super();
		this.range = range;
		this.token = Optional.of(token);
	}

	
	public RangedString(int startIndex) {
		super();
		this.range = new Range(startIndex);
		this.token = Optional.empty();
	}
	
	public RangedString(int startIndex , String token) {
		super();
		this.range = new Range(startIndex , startIndex + token.length());
		this.token = Optional.of(token);
	}
}