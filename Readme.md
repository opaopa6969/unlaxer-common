# Unlaxer - simple parser combinator that inspired by [RELAX NG](http://relaxng.org/)
--------------


Unlaxer is a parser combinator written in Java.

* easy to read
* easy to write and debug with IDE
* code first. not notation first
* we love word more than symbol ( 'ZeroOrMore' better than '*' )
* support Optional,Choice,Interleave,Zero(One)OrMore,Group,Ref and more from [RELAX NG](http://relaxng.org/) vocabulary
* support inifinite lookahead
* support backtracking
* support backwords reference
* support java base functional parser/token referencer
* support parsing context into scope tree

## Version
[![Maven Central](https://img.shields.io/maven-central/v/org.unlaxer/unlaxer-common.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22org.unlaxer%22%20AND%20a:%22unlaxer-common%22)

## Depenedencies

see build.gradle

no third pary library required

## Required

* [JavaSDK] - version 8 later for build & execute

## Installation

```sh
git clone git@bitbucket.org:opaopa6969/unlaxer.git unlaxer
cd unlaxer
./gradlew install
```

## Unit test

```sh
./gradlew test
```


## Setup for development with Eclipse 

```sh
./gradlew eclipse
```


this command make eclipse .project file.

after you launched the Eclipse , goto menu
Menu > File > Import > Existing Projects into workspace > select root folder that contains this project
and check 'Search for nested projects' and click 'Finish'

## Setup for development with Idea 

```sh
./gradlew idea
```

## Brief the sample program

You can run sample program to undestand how to use unlaxer.

Sample programa are stored in src/test/java folder for each project.

example org.unlaxer.elementary.MappedSingleCharacterParserTest.java stored in src/test/java/org.unlaxer.elementary.MappedSingleCharacterParserTest.java

```java
public class MappedSingleCharacterParserTest extends ParserTestBase {

  @Test
  public void testExcludes() {

    MappedSingleCharacterParser punctuationParserWithoutParenthesis = 
      new PunctuationParser().newWithout("()");

    OneOrMore parser = new OneOrMore(punctuationParserWithoutParenthesis);

    testAllMatch(parser, "$%&");
    testPartialMatch(parser, "$%(&", "$%");
    testUnMatch(parser, "()");
  }
}
```

this test means

* The parser defined to matched with one or more Punctuation without charactor '(' and ')'

* First test method **testAllMatch(oneOrMore, "$%&");** means parser accepts "$%&" 3 charactors exact match

* Second test method **testPartialMatch(oneOrMore, "$%(&", "$%");** means parser accepts partial of test string. parser dose not match '(' and ')'. cause, parser matched only "$%"

* Third test method **testUnMatch(oneOrMore, "()");** means parser does not match all test string.

After runnning the test. ParserTestBase creates parser log into build/parserTest/org.unlaxer.elementary.MappedSingleCharacterParserTest

```sh
testExcludes_testAllMatch_(1,L16).combined.log
testExcludes_testAllMatch_(1,L16).parse.log
testExcludes_testAllMatch_(1,L16).token.log
testExcludes_testAllMatch_(1,L16).transaction.log
testExcludes_testPartialMatch_(2,L17).combined.log
testExcludes_testPartialMatch_(2,L17).parse.log
testExcludes_testPartialMatch_(2,L17).token.log
testExcludes_testPartialMatch_(2,L17).transaction.log
testExcludes_testUnMatch_(3,L18).combined.log
testExcludes_testUnMatch_(3,L18).parse.log
testExcludes_testUnMatch_(3,L18).token.log
testExcludes_testUnMatch_(3,L18).transaction.log
```
try reading and running test program in unlaxer-common/src/test/java

## Usage 

### Setup your project with gradle

* this product already uploaded to maven central

* add dependency to build.gradle

```groovy

project(':application') {
    dependencies {
        compile ':unlaxer-common:'
    }
}

```

### create Parser

[Usage001_createParserAndParse.java](https://bitbucket.org/opaopa6969/unlaxer/src/HEAD/unlaxer-common/src/test/java/sample/Usage001_createParserAndParse.java?at=master)

Sample parser build as following EBNF grammar.

```
<Clause> ::= [0-9]+([-+*/][0-9]+)*
```

```java
static Parser createDigitsAndOperatorsParser() {
	//<Clause> ::= [0-9]+([-+*/][0-9]+)*
	Chain clauseParser = new Chain(
		new OneOrMore(new DigitParser()),
		new ZeroOrMore(
			new Chain(
				new Choice(
					new PlusParser(),
					new MinusParser(),
					new MultipleParser(),
					new DivisionParser()
				),
				new OneOrMore(new DigitParser())
			)
		)
	);
	return clauseParser;
}
```
* Chain parser needs all children parsers successfully parsed with ordered.
* ZeroOrMore parser needs child parser successfully parsed zero or more times.
* OneOrMore parser needs child parser successfully parsed one or more times.
* Choice parser needs one child of specified children parsers parse successfully parsed.

### Create ParserContext

```java

ParseContext parseContext = new ParseContext(new StringSource("1+2+3"));

```

* parseContext creates with parse target source.
* parseContext accepts additional argument as ParseContextEffector that change context value.(describe later)
* parseContext has execution stack to parsing with backtracking.

### Parse source

```java
Parsed parsed = parser.parse(parseContext);

```
* parser parse with source in parseContext
* parser#parse method return Parsed Object

### How to get parsing result

#### get parsing status
* status is choice of succeeded, failed, and stopped

```java
//get parsing status
System.out.format("parsed status: %s \n" , parsed.status);
```

got

```sh
parsed status: succeeded
```

#### get root token
* token has matched parser and consumed source string and position.
* token is sytax tree's node.
* so, token has children.

```java
//get rootToken
System.out.format("parsed Token: %s \n" , parsed.getRootToken());
```

got

```sh
parsed Token: '1+2+3' (0 - 5): org.unlaxer.PseudoRootParser
```

* '1+2+3' means the consumed source string
* (0 - 5) means the consumed source string position range. 5 is end exclusive index.
* org.unlaxer.PseudoRootParser means the consumed by parser.
    * you expected org.unlaxer.PseudoRootParser ? in this case, parse context created with [CreateMetaTokenSprcifier](https://bitbucket.org/opaopa6969/unlaxer/src/HEAD/unlaxer-common/src/main/java/org/unlaxer/CreateMetaTokenSprcifier.java?at=master&fileviewer=file-view-default).createMetaOff
    * if parse context createMeta mode is off , not create token with [MetaFunctionParser](https://bitbucket.org/opaopa6969/unlaxer/src/HEAD/unlaxer-common/src/main/java/org/unlaxer/MetaFunctionParser.java?at=master)'s subclass.
    * Chain, OneOrMore, ZeroOrMode, Choice, Optional, is MetaFunctionParser.

### Get syntax tree representation

* for debugging or recognize syntax tree with target source.
* TokenPrinter is Utility to representation of Token.

```java
//get tokenTree representation
System.out.format("parsed TokenTree: %s \n" , TokenPrinter.get(parsed.getRootToken()));
```

got

```sh
parsed TokenTree: 
'1+2+3' : org.unlaxer.PseudoRootParser
 '1' : org.unlaxer.posix.DigitParser
 '+' : org.unlaxer.ascii.PlusParser
 '2' : org.unlaxer.posix.DigitParser
 '+' : org.unlaxer.ascii.PlusParser
 '3' : org.unlaxer.posix.DigitParser
```

* you are disappointed ? this results seems flat structure. if you expected tree strucre result, show you way to two solutions lator.

### Create Parser with Named Concrete class (solution 1)

* SimpleExpression parser -> Chain(Number,ZeroOrMore(OperatorAndOperand))
* each parser create with extended [AbstractCollectingParser.java](https://bitbucket.org/opaopa6969/unlaxer/src/HEAD/unlaxer-common/src/main/java/org/unlaxer/combinator/AbstractCollectingParser.java?at=master&fileviewer=file-view-default)
    * CollectingParser implements Token collect(List<Token> tokens, int position , TokenKind tokenKind)
    * each Named Concrete consume and create multi token. collecting parser make one token from multiple tokens
    * see implementation [CollectingParser.java](https://bitbucket.org/opaopa6969/unlaxer/src/HEAD/unlaxer-common/src/main/java/org/unlaxer/CollectingParser.java?at=master&fileviewer=file-view-default)
    * eg. ParserA := OneOrMore(Digit) , 
        * ParserA parse '123' -> token('1'),token('2'),token('3')
        * CollectingParser#collect( token('1'),token('2'),token('3')) -> token('123')

[Usage001_createParserAndParseWithNamedConcrete.java](https://bitbucket.org/opaopa6969/unlaxer/src/HEAD/unlaxer-common/src/test/java/sample/Usage001_createParserAndParseWithNamedConcrete.java?at=master)

```java
static class SimpleExpression extends LazyChain{

	@Override
	public List<Parser> getLazyParsers() {

		return new Parsers(
			new NumberParser(),
			new ZeroOrMore(
				new OperatorAndOperandParser()
			)
		);
	}
}

static class NumberParser extends LazyOneOrMore{

	@Override
	public Parser getLazyParser() {
		return new DigitParser();
	}

	@Override
	public Optional<Parser> getLazyTerminatorParser() {
		return Optional.empty();
	}
}

static class OperatorParser extends LazyChoice{

	@Override
	public List<Parser> getLazyParsers() {
		return new Parsers(
			new PlusParser(),
			new MinusParser(),
			new MultipleParser(),
			new DivisionParser()
		);
	}
}

static class OperatorAndOperandParser extends LazyChain{

	@Override
	public List<Parser> getLazyParsers() {
		return new Parsers(
			new OperatorParser(),
			new NumberParser()
		);
	}
}
```

parse and then got Tree

```sh

parsed TokenTree:
'1+2+3' : sample.Usage001_createParserAndParseWithNamedConcrete$SimpleExpression
 '1' : sample.Usage001_createParserAndParseWithNamedConcrete$NumberParser
  '1' : org.unlaxer.posix.DigitParser
 '+2' : sample.Usage001_createParserAndParseWithNamedConcrete$OperatorAndOperandParser
  '+' : sample.Usage001_createParserAndParseWithNamedConcrete$OperatorParser
   '+' : org.unlaxer.ascii.PlusParser
  '2' : sample.Usage001_createParserAndParseWithNamedConcrete$NumberParser
   '2' : org.unlaxer.posix.DigitParser
 '+3' : sample.Usage001_createParserAndParseWithNamedConcrete$OperatorAndOperandParser
  '+' : sample.Usage001_createParserAndParseWithNamedConcrete$OperatorParser
   '+' : org.unlaxer.ascii.PlusParser
  '3' : sample.Usage001_createParserAndParseWithNamedConcrete$NumberParser
   '3' : org.unlaxer.posix.DigitParser
```


### Create ParserContext with create meta specifier (solution 2)

* specify [CreateMetaTokenSprcifier](https://bitbucket.org/opaopa6969/unlaxer/src/HEAD/unlaxer-common/src/main/java/org/unlaxer/CreateMetaTokenSprcifier.java?at=master&fileviewer=file-view-default).createMetaOn when ParseContext instantiate.

```java

//create parseContext with createMeta specifier
ParseContext parseContext = 
	new ParseContext(
		new StringSource("1+2+3"),
		CreateMetaTokenSprcifier.createMetaOn // <- specify createMetaOn
	);

```

got token tree

```sh
'1+2+3' : org.unlaxer.combinator.Chain
 '1' : org.unlaxer.combinator.OneOrMore
  '1' : org.unlaxer.posix.DigitParser
 '+2+3' : org.unlaxer.combinator.ZeroOrMore
  '+2' : org.unlaxer.combinator.Chain
   '+' : org.unlaxer.combinator.Choice
    '+' : org.unlaxer.ascii.PlusParser
   '2' : org.unlaxer.combinator.OneOrMore
    '2' : org.unlaxer.posix.DigitParser
  '+3' : org.unlaxer.combinator.Chain
   '+' : org.unlaxer.combinator.Choice
    '+' : org.unlaxer.ascii.PlusParser
   '3' : org.unlaxer.combinator.OneOrMore
    '3' : org.unlaxer.posix.DigitPars
```

### Walking tree
### Debug
### Test

## Implementation of fundamentally element
### Token
### ParseContext
### Parser
### TransactionElement
### Referencer
### ScopeTree

## Use Case
### Chain
### Choice
### Optional/OneOrMore/ZeroOrMore/Occurs
### Non Oredered
### Named Parser
### Error Message
### Suggest
### Calculator
### CQL
### Backward Reference
### Palindrome

## Sequence Diagram

[parse sammary](https://bitbucket.org/opaopa6969/unlaxer/raw/HEAD/unlaxer-common/doc/parse-sequence.pdf)

## Parsers

### Terminal Symbol Parsers

### PosixParser
### CombinatorParser

Combinator parser accepts one or more child parser.
This parser returns parsed result from that aggregates each children's parsed result.

* Choice (org.unlaxer.combinator.Choice)
    * in RelaxNG, <choice>

Choice is select parser in children at match first.

sample code:
```java
Choice digitOrSign = new Choice(
    Singletons.get(DigitParser.class),
    Singletons.get(SignParser.class)
);

String[] tests={"1","a","-"};

StringSource source = new StringSource(sourceString);

for(String test: tests){
    try(ParseContext parseContext = new ParseContext(source)){
    	Parsed parsed = parser.parse(parseContext);
        System.out.format("%s : match = %s\n" , test , parsed.success);
    }
}
```
results:
```terminal
1 : match = true
a : match = true
- : match = false
```

* Chain (org.unlaxer.combinator.Chain)
    * in RelaxNG, <group>

* NonOrderd (org.unlaxer.combinator.NOnOrdered)
    * in RelaxNG, <interleave>





## Samples

# Tasks

## TODO
* remove invertMach in Parsed parse(ParseContext parseContext , TokenKind tokenKind ,boolean invertMatch); method
* NotASTNode recursive mode
* parser generator with CodeModel
* Parser interface implementation as trait
* add terminator information with committed and rollbacked when using printer
* implement ParserListener log outputter
* EBN-> parser generator
    * thinking about left recursion problem.
* Name test
* group + group reference
    * Reference Specifier
        * NameBase (css's ID)
        * TagBase (css's class)
        * PathBase (css selector or like xpath)
        * addressing specifier + predicator ?
* NOP parser (for align depth in parser tree)
* Lexer & Parser Mode (now parser only mode)
    * CamelCase Tokenizer
    * CharactorClass change point detecting tokenizer
* multi line mode (multi paser instance mode. excution in parallel)
* AnnotatedAbstractCollectingparser 
    * eg. parser = parser("(") + ValueParser + parser("("), <- how to reference valueParser ? <-with scopeTree ?
* PEG-> parser generator
* parser -> graphical diagram
* make new grammar notation language.
* representate about matchOnly with TokenPrinter
* thinking about position. consumed and matched
* add ParenthesisParser test case
    * ((a),(b)) <- nested parenthesis
    * (PuncuationParser) <- Punctuation contains '('and')'
        * how to excludes '(' and ')' with PuncuationParser when exclosure is ParenthesisParser
* add ErrorMessage to Parsed
* add stop flag to Parsed
    * when ErrorMessage added and source until all consumed, parser try consume remain source. it's unexpected.
* draw diagram about createMetaToken and commit
* the Source at part of Parser interface , change to TransactionaSource extends Transaction , Source
* thinking about CollectiongParser and SingleChildParser. SingleChildParser has not collect(). to generaize collect child
* thinking about occurs specifier needed at case grammar interleave(optional(parser),optional(parser)) or chain(optional(parser),optional(parser)), no inner parser matched.
* thinking about RELAXNG's grammar combine.
* test case to show difference regular expression and unlaxer. -> https://bitbucket.org/opaopa6969/calcsample/src/802d2ec3ad4e79b53b74d0e801ef7beedd90da6e/src/test/java/com/daredayo/app/Test.java?at=master&fileviewer=file-view-default
* support token printer following mode (https://github.com/nikomatsakis/lalrpop/blob/master/doc/tutorial.md)
(  (  1  2  3  )  )
|  |  |     |  |  |
|  |  +-Num-+  |  |
|  |     |     |  |
|  |   Term    |  |
|  |     |     |  |
|  +---Term----+  |
|        |        |
+------Term-------+

## Bug

* trim unnecessary double '/' when printing parser path

## Implemented
* prune syntax tree to get ast
* ScopeTree
    * create scopeTree gernerate after syntaxTree generated ?
* group + group reference
    * MatchedTokenParser(Reference) -> match with immediate value as token that matched Parser
        * '<'Parser1=alphabet*'>''<'ImmediateMatcher(Parser1)'>'
        * <abc><abc> -> match
        * <abc><az> ->  not match
* javadoc language set to english
* Syntax tree reducer(eg . cut filter MetaFunctionParser)
* SuggestableParser
    * base implementation is same about ErrorMessageParser. SuggestableParser make Suggests from ParentParser implemented Suggestable
    * Suggestable has following method-> List<Suggest> suggestWith(characte)
    * thinking about runtime suggest.(far successor phase) eg. int a = math.<suggest methods>
* thinking about parent parser for TerminatorParser in Occurs.
* ChainTest activate ignored test 
* slice
    * slice(MatchedTokenParser,specifier(start:end)) or slice(MultiChildrenParse,specifier(start:end))
        * ABCD[0:-1] =ABC
        * ABCD[1:] =BCD
        * ABCD[:-2] =AB
        *
* Token stores consumed and matched each or Token stores TokenKind(matchOnly or consumed)
* ReverseParser
* FlattenParser
    * A(B,C,D) : Flatten(A) -> (B,C,D)

* group + group reference
    * Reference(Predicete<Parser>) -> search ParserTree in statically
        * '<'Parser1=alphabet+'>''<'Reference(Parser1)'>'
        * <abc><abc> -> match
        * <abc><de> -> match
        * <abc><12> -> not match
        * <1><1> ->  not match
        * <1><az> ->  not match
    * ReferenceMatched(Predicete<Parser>) -> search TokenTree in dynamically.(macthed parser only) <- 
        * '<'Parser1=alphabet+'>''<'ReferenceMatched(Parser1)'>'
        * <abc><abc> -> match
        * <abc><de> -> match
        * <1><1> ->  not match
        * <1><az> ->  not match
        *
        * namedChoice<Parser1=alphabet+,Parser2=numeric+,Praser3=puncture+>'-'Reference(namedChoice)
        * abc-kgz -> match
        * 123-653 -> match
        * $#"-)%& -> match
        * abc-458 -> not match
        * 456-kgz -> not match
        * $$$-&1z -> not match
            * draw sequence diagram how to retrieve matched choosable
        *
        * namedNonOrder<Parser1=alphabet+,Parser2=numeric+,Praser3=puncture+>'-'Reference(namedNonOrder)
        * 1#a-3%b ->match
        * az12&#a-b%b ->match
        * 12#-35% ->not match

## Rejected
* Indicate rootParser when parser.pase(parseContext).
    * indicate when construct parser manually

## thought about
* Parse successfully is valid ? when ErrorMessageParser Matched
    * if parsed.success == false then might to rollback. -> parsed.sucess = true is valid.

----

License
----

MIT

  [JavaSDK]: http://java.com/ja/
  [gradle]: http://www.gradle.org/
