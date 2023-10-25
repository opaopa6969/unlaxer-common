package org.unlaxer;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.unlaxer.listener.OutputLevel;
import org.unlaxer.parser.Suggest;
import org.unlaxer.parser.SuggestableParser;
import org.unlaxer.parser.Suggests;
import org.unlaxer.parser.SuggestsCollectorParser;
import org.unlaxer.parser.ascii.MinusParser;
import org.unlaxer.parser.ascii.PlusParser;
import org.unlaxer.parser.combinator.Chain;
import org.unlaxer.parser.combinator.Choice;
import org.unlaxer.parser.posix.DigitParser;

public class SuggestsCollectorParserTest extends ParserTestBase{

	@Test
	public void testGetSuggests() {
		
		setLevel(OutputLevel.detail);
		Chain chain = new Chain(
			new DigitParser(),
			new PlusParser(),
			new Choice(
				CosParser.class,
				SinParser.class,
				SqrtParser.class,
				SuggestsCollectorParser.class
			)
		);
		
		TestResult testPartialMatch = testPartialMatch(chain, "1+s", "1+");
		Token rootToken = testPartialMatch.parsed.getRootToken();
		List<RangedContent<Suggests>> rangedContents = 
				SuggestsCollectorParser.getRangedContents(rootToken, SuggestsCollectorParser.class);

		check(rangedContents);
	}
	
	@Test
	public void testParseStopWhenSuggestsCollectorActived() {
		
		setLevel(OutputLevel.detail);
		Chain chain = new Chain(
			new DigitParser(),
			new PlusParser(),
			new Choice(
				CosParser.class,
				SinParser.class,
				SqrtParser.class,
				SuggestsCollectorParser.class
			),
			new MinusParser(),
			new DigitParser()
		);
		
		TestResult testPartialMatch = testPartialMatch(chain, "1+s-5", "1+");
		Token rootToken = testPartialMatch.parsed.getRootToken();
		List<RangedContent<Suggests>> rangedContents = 
				SuggestsCollectorParser.getRangedContents(rootToken, SuggestsCollectorParser.class);

		check(rangedContents);
	}

	private void check(List<RangedContent<Suggests>> rangedContents) {
		rangedContents.stream()
			.forEach(rangedContent->{
				rangedContent.getContent().stream()
					.forEach(suggest->{
						
						System.out.println("parser:" + suggest.suggestedBy);
						suggest.words.stream()
							.forEach(word->{
								System.out.println("suggestword:" + word);
							});
					});;
			});
		
		List<String> suggestWords = rangedContents.stream()
				.map(RangedContent::getContent)
				.flatMap(Suggests::stream)
				.map(Suggest.class::cast)
				.flatMap(suggest->suggest.getWords().stream())
				.collect(Collectors.toList());
		
		assertEquals(2, suggestWords.size());
		assertTrue(suggestWords.contains("sin"));
		assertTrue(suggestWords.contains("sqrt"));
	}

	
	/**
	 * this parser is sample for suggest. 
	 * if you want to see applicative implementation , calculator Project org.unlaxer.sample.calc.parser.function.SinParser
	 */
	public class SinParser extends SuggestableParser {

		private static final long serialVersionUID = 5911697205587011643L;

		public SinParser() {
			super(true, "sin");
		}
			
		@Override
		public String getSuggestString(String matchedString) {
			return "(".concat(matchedString).concat(")");
		}

	}

	public class SqrtParser extends SuggestableParser {

		private static final long serialVersionUID = -6097760458963414195L;

		public SqrtParser() {
			super(true, "sqrt");
		}
			
		@Override
		public String getSuggestString(String matchedString) {
			return "(".concat(matchedString).concat(")");
		}

	}

	
	public class CosParser extends SuggestableParser {

		private static final long serialVersionUID = 5850124685962927999L;

		public CosParser() {
			super(true, "cos");
		}
			
		@Override
		public String getSuggestString(String matchedString) {
			return "(".concat(matchedString).concat(")");
		}
	}

}
