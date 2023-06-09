package org.unlaxer;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.unlaxer.Token.SearchFirst;
import org.unlaxer.parser.ascii.PlusParser;
import org.unlaxer.parser.posix.DotParser;
import org.unlaxer.parser.posix.HashParser;

public class TokenTest {

	@Test
	public void testFlatten() {
		
		
		List<Token> third = List.of(new Token(TokenKind.matchOnly, new RangedString(0), new HashParser()));
		List<Token> second = List.of(
			new Token(TokenKind.matchOnly, third, new DotParser(),0),
			new Token(TokenKind.matchOnly, third, new DotParser(),0)
		);
		Token root = new Token(TokenKind.matchOnly, second, new PlusParser(),0);
		{
			List<Token> flatten = root.flatten(SearchFirst.Breadth);
			String collect = flatten.stream()
					.map(token->token.parser.getName().getSimpleName())
					.collect(Collectors.joining(","));
			
			System.out.println(collect);
			
			assertEquals("PlusParser,DotParser,DotParser,HashParser,HashParser",collect);
		}
		{
			List<Token> flatten = root.flatten(SearchFirst.Depth);
			String collect = flatten.stream()
					.map(token->token.parser.getName().getSimpleName())
					.collect(Collectors.joining(","));
			
			System.out.println(collect);
			
			assertEquals("PlusParser,DotParser,HashParser,DotParser,HashParser",collect);
		}
	}

}
