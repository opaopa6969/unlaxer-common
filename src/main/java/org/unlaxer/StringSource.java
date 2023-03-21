package org.unlaxer;

public class StringSource implements Source{
	
	private static final long serialVersionUID = 566340401655249596L;
	
	private String source;
	

	public StringSource(String source) {
		super();
		this.source = source;
	}

	@Override
	public RangedString peek(int startIndexInclusive, int length) {
		
		if(startIndexInclusive + length > source.length()){
			return new RangedString(startIndexInclusive);
		}

		return new RangedString(
				new Range(startIndexInclusive,startIndexInclusive+length),
				source.substring(startIndexInclusive, startIndexInclusive+length));
	}

	@Override
	public int getLength() {
		return source.length();
	}

	@Override
	public String toString() {
		return source;
	}
}