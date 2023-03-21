package org.unlaxer.parser.posix;

import org.unlaxer.parser.StaticParser;
import org.unlaxer.parser.elementary.MappedSingleCharacterParser;

public class SpaceParser extends MappedSingleCharacterParser implements StaticParser{

	private static final long serialVersionUID = 95516864251035105L;

	public SpaceParser() {
		super(new char[]{32,9,10,11,12,13});
		//" \t\n\r\f\v"
	}
	
	public final static SpaceParser SINGLETON = new SpaceParser();
	
}