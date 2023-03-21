package org.unlaxer.parser;

import org.unlaxer.listener.OutputLevel;

public class ParserPrinter{
	
	public static String get(Parser parser , OutputLevel level){
		String parserString = level == OutputLevel.detail ? 
				parser.getParentPath()+"/"+ parser.getName().toString():
				parser.getName().toString();
		return String.format("parser:%s", parserString);
	}
}