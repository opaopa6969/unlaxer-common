package org.unlaxer;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.unlaxer.listener.OutputLevel;
import org.unlaxer.parser.ErrorMessageParser;
import org.unlaxer.parser.Parser;

public class TokenPrinter{
	
//	public enum DebugLevel{simple,detail}

	
	public static void output(Token token , PrintStream out){
		output(token , out, 0 , OutputLevel.simple,true);
	}

	
	public static void output(Token token , PrintStream out, int level){
		output(token , out, level ,OutputLevel.simple,true);
	}
	
	public static void output(Token token , PrintStream out, OutputLevel detailLevel){
		output(token, out, 0 ,detailLevel , true);
	}

	static String EMPTY="<EMPTY>";
//	static String EMPTY="'' ";
	
	public static void output(Token token , PrintStream out, int level, 
			OutputLevel detailLevel,boolean outputChildren){
		
		Optional<String> tokenString = token.getToken();
		
		for(int i = 0 ; i < level ; i++){
			out.print(" ");
		}
		Range tokenRange = token.getTokenRange();
		Parser parser = token.parser;
		if(detailLevel == OutputLevel.detail){
			
			out.format("%s (%d - %d): %s%s%s", 
				tokenString.map(TokenPrinter::quote).orElse(EMPTY) ,//
				tokenRange.startIndexInclusive,//
				tokenRange.endIndexExclusive, //
				parser.getName(),
				getInverted(token),//
				outputChildren ? "\n":"");
			
		}else if(detailLevel == OutputLevel.simple){
			
			out.format("%s : %s%s%s" , //
				tokenString.map(TokenPrinter::quote).orElse(EMPTY) ,
				parser.getName(),
				getInverted(token),//
				outputChildren ? "\n":"");
		}
		if(false == outputChildren){
			return ;
		}
		level++;
		for(Token original : token.filteredChildren){
			output(original,out,level,detailLevel,true);
		}
	}
		
	static String quote(String word){
		return "'" +word + "'";
	}
	
	static String getInverted(Token token){
		return token.parser.getInvertMatchFromParent()?"(inverted)":"";
	}
	
	public static String get(Token token){
		return get(token , 0 ,OutputLevel.simple,true);
	}
	
	public static String get(Token token , int level){
		return get(token , level ,OutputLevel.simple,true);
	}
	
	public static String get(Token token , OutputLevel detailLevel){
		return get(token , 0 ,detailLevel ,true);
	}
	
	public static String get(Token token , int level, 
			OutputLevel detailLevel, boolean outputChildren) {
		
		try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			PrintStream printStream = new PrintStream(byteArrayOutputStream,false,"UTF-8");){
			
			output(token, printStream , level , detailLevel ,outputChildren);
			return byteArrayOutputStream.toString("UTF-8");
			
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static List<ErrorMessage> getErrorMessages(Token targetToken){
		return targetToken.flatten().stream()
			.filter(token-> token.parser instanceof ErrorMessageParser)
			.map(token->new ErrorMessage(token.tokenRange, ((ErrorMessageParser)token.parser).get()))
			.collect(Collectors.toList());
	}
	
	public static String get(String header , List<Token> tokens){
		String collect = tokens.stream()
			.map(Token::getToken)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.collect(Collectors.joining());
		return String.format("%s:%s ", header , collect);
	}

}