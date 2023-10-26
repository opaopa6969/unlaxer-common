package sample;

import java.util.stream.Stream;

import org.junit.Test;
import org.unlaxer.ParserTestBase;
import org.unlaxer.Range;
import org.unlaxer.listener.OutputLevel;
import org.unlaxer.parser.combinator.Chain;
import org.unlaxer.parser.combinator.MatchOnly;
import org.unlaxer.parser.combinator.OneOrMore;
import org.unlaxer.parser.posix.AsciiParser;
import org.unlaxer.parser.referencer.MatchedTokenParser;

public class Usage003_01_Palidrome extends ParserTestBase {
	
	@Test
	public void testPalindrome() {
		
		setLevel(OutputLevel.simple);
		
		
		OneOrMore words = new OneOrMore(
			AsciiParser.class
		);
		MatchOnly wordLookahead = new MatchOnly(words);
		MatchedTokenParser matchedTokenParser = new MatchedTokenParser(wordLookahead);
		{
			Chain chain = new Chain(
				wordLookahead,
				matchedTokenParser
			);
			testAllMatch(chain, "abcba",true);
		}
		
		Stream.of(
			sliceWithWord(wordLookahead, matchedTokenParser),
			sliceWithSlicer(wordLookahead, matchedTokenParser),
			effectReverse(wordLookahead, matchedTokenParser),
			sliceReverse(wordLookahead, matchedTokenParser),
			sliceReverseWithPythonian(wordLookahead, matchedTokenParser)
			)
			.forEach(chain->{
				testAllMatch(chain, "a",true);
				testAllMatch(chain, "abcba",true);
				testAllMatch(chain, "abccba",true);
				testAllMatch(chain, "aa",true);
				testUnMatch(chain, "ab",true);
				
			});	
		
	}
	
	private Chain sliceWithWord(MatchOnly wordLookahead, MatchedTokenParser matchedTokenParser) {
		
		return new Chain(
			//abcba
			wordLookahead,
			//ab
			matchedTokenParser.sliceWithWord(word->{
				boolean hasPivot = word.length() % 2 ==1;
				int halfSize = (word.length() - (hasPivot ? 1:0))/2;
				return new Range(0,halfSize);
			}),
			//  c
			matchedTokenParser.sliceWithWord(word->{
				boolean hasPivot = word.length() % 2 ==1;
				int halfSize = (word.length() - (hasPivot ? 1:0))/2;
				return hasPivot ? new Range(halfSize,halfSize+1): new Range(0,0);
			}),
			//ab->reverse
			matchedTokenParser.slice(word->{
				boolean hasPivot = word.length() % 2 ==1;
				int halfSize = (word.length() - (hasPivot ? 1:0))/2;
				return new Range(0,halfSize);
			},true)
		);
	}
	
	private Chain sliceWithSlicer(MatchOnly wordLookahead, MatchedTokenParser matchedTokenParser) {

		return new Chain(
			//abcba
			wordLookahead,
			//ab
			matchedTokenParser.slice(slicer->{
				boolean hasPivot = slicer.length() % 2 ==1;
				slicer.end((slicer.length() - (hasPivot ? 1:0))/2);
			}),
			//  c
			matchedTokenParser.slice(slicer->{
				boolean hasPivot = slicer.length() % 2 ==1;
				int halfSize = (slicer.length() - (hasPivot ? 1:0))/2;
				if(hasPivot){
					slicer.begin(halfSize).end(halfSize+1);
				}else{
					slicer.invalidate();
				}
			}),
			//ab->reverse
			matchedTokenParser.slice(slicer->{
				boolean hasPivot = slicer.length() % 2 ==1;
				int halfSize = (slicer.length() - (hasPivot ? 1:0))/2;
				slicer.end(halfSize).step(-1);//step=-1 <- for reverse
			})
		);
	}
	
	private Chain effectReverse(MatchOnly wordLookahead, MatchedTokenParser matchedTokenParser) {
		return new Chain(
			//abcba
			wordLookahead,
			//abcba->reverse
			matchedTokenParser.effect(word->new StringBuilder(word).reverse().toString())
		);
	}
	

	private Chain sliceReverse(MatchOnly wordLookahead, MatchedTokenParser matchedTokenParser) {
		return new Chain(
			//abcba
			wordLookahead,
			//abcba->reverse
			matchedTokenParser.slice(slicer->slicer.step(-1))
		);
	}

	
	private Chain sliceReverseWithPythonian(MatchOnly wordLookahead, MatchedTokenParser matchedTokenParser) {
		return new Chain(
			//abcba
			wordLookahead,
			//abcba->reverse
			matchedTokenParser.slice(slicer->slicer.pythonian("::-1"))
		);
	}

}