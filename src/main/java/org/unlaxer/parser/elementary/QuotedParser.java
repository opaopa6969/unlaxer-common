package org.unlaxer.parser.elementary;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.unlaxer.Name;
import org.unlaxer.Token;
import org.unlaxer.parser.Parser;
import org.unlaxer.parser.Parsers;
import org.unlaxer.parser.combinator.Choice;
import org.unlaxer.parser.combinator.LazyChain;
import org.unlaxer.parser.combinator.LazyZeroOrMore;
import org.unlaxer.parser.combinator.NotPropagatableSource;
import org.unlaxer.parser.combinator.ParserWrapper;
import org.unlaxer.parser.combinator.ZeroOrMore;

public class QuotedParser extends LazyChain {
	
	public enum Parts{
		leftQuote,
		contents,
		rightQuote,
		;
		Name nameInstance;
		
		private Parts(){
			nameInstance = Name.of(this);
		}
		public Name get(){
			return nameInstance;
		}
	}
	
	private static final long serialVersionUID = 8673007906829254945L;
	
	Parser quoteParser;
	
	public QuotedParser(Parser quoteParser) {
		super();
		this.quoteParser = quoteParser;
	}
	

	public QuotedParser(Name name,Parser quoteParser) {
		super(name);
		this.quoteParser = quoteParser;
	}


	public static final Name contentsName = Name.of(QuotedParser.class, Parts.contents.get());

	@Override
	public List<Parser> getLazyParsers() {
		return new Parsers(
			new ParserWrapper(
				Name.of(QuotedParser.class, Parts.leftQuote.get()),
				quoteParser
			),
			new ZeroOrMore(contentsName,
				new Choice(
					new EscapeInQuotedParser(),
					new NotPropagatableSource(quoteParser)
				)
			),
			new ParserWrapper(
				Name.of(QuotedParser.class, Parts.rightQuote.get()),
				quoteParser
			)
		);
	}
	
	
	public static class QuotedContentsParser extends LazyZeroOrMore{

		private static final long serialVersionUID = 2072779746097816750L;


		public QuotedContentsParser(Parser quoteParser) {
			super(QuotedParser.contentsName);
			this.quoteParser = quoteParser;
		}
		
		Parser contents;
		Parser quoteParser;

		@Override
		public void initialize() {
			contents = new Choice(
				new EscapeInQuotedParser(),
				new NotPropagatableSource(quoteParser)
			);
		}


		@Override
		public Supplier<Parser> getLazyParser() {
			return ()-> contents;
		}

		@Override
		public Optional<Parser> getLazyTerminatorParser() {
			return Optional.empty();
		}
	}
	
	public static String contents(Token thisParsersToken) {
//		Name target = Parts.contents.get();
		Optional<Token> collect = thisParsersToken.flatten().stream()
//			.peek(token->System.out.println(TokenPrinter.get(token)))
			.filter(token->token.parser.getName().equals(QuotedParser.contentsName))
			.findFirst();
//		String collect = thisParsersToken.children.stream()
//			.filter(token->token.getParser().findFirstToParent(parser->parser.getName() == target).isPresent())
//			.map(Token::getToken)
//			.filter(Optional::isPresent)
//			.map(Optional::get)
//			.collect(Collectors.joining());
		
		String contents = collect
				.flatMap(Token::getToken)
				//FIXME! this is work around for BUG...
				.orElseGet(()->thisParsersToken.tokenString.map(quoted->quoted.substring(1, quoted.length()-1)).orElse(""));
		return contents;
		
	}
}