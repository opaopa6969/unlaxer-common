package org.unlaxer;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public interface Source extends CodePointAccessor{
	
	public RangedString peek(CodePointIndex startIndexInclusive, CodePointLength length);
	
  public default RangedString peek(CodePointIndex startIndexInclusive, CodePointIndex endIndexExclusive) {
    return peek(startIndexInclusive , new CodePointLength(endIndexExclusive.minus(startIndexInclusive)));
  }
	
	public default RangedString peekLast(CodePointIndex endIndexInclusive, CodePointLength length) {
	  
	  CodePointIndex start = endIndexInclusive.minus(length)
	      .createIfMatch(CodePointIndex::isNegative, ()->new CodePointIndex(0));
	  
	  CodePointIndex end = endIndexInclusive.minus(start);
		return peek(start , end);
	}
	
	public CodePointLength getLength();
	
	public LineNumber getLineNUmber(CodePointIndex Position);
	
  public Source subSource(CodePointIndex startIndexInclusive, CodePointIndex endIndexExclusive);
  
  public Source subSource(CodePointIndex startIndexInclusive, CodePointLength codePointLength);
  
  public int[] subCodePoints(CodePointIndex startIndexInclusive, CodePointIndex endIndexExclusive);

	
//	/**
//   * Returns a {@code Collector} that concatenates the input elements into a
//   * {@code String}, in encounter order.
//   *
//   * @return a {@code Collector} that concatenates the input elements into a
//   * {@code String}, in encounter order
//   */
//  public static Collector<CharSequence, ?, Source> joining() {
//      return new CollectorImpl<CharSequence, SimpleBuilder, Source>(
//              SimpleBuilder::new, SimpleBuilder::append,
//              (r1, r2) -> { 
//                SimpleBuilder append = r1.append(r2); 
//                return append; 
//              },
//              SimpleBuilder::toSource, CH_NOID);
//  }
	
  /**
  * Returns a {@code Collector} that concatenates the input elements into a
  * {@code String}, in encounter order.
  *
  * @return a {@code Collector} that concatenates the input elements into a
  * {@code String}, in encounter order
  */
	public static Collector<CharSequence, ?, Source> joining() {
	  return joining("");
	}
  
  /**
   * Returns a {@code Collector} that concatenates the input elements,
   * separated by the specified delimiter, in encounter order.
   *
   * @param delimiter the delimiter to be used between each element
   * @return A {@code Collector} which concatenates CharSequence elements,
   * separated by the specified delimiter, in encounter order
   */
  public static Collector<CharSequence, ?, Source> joining(CharSequence delimiter) {
      return joining(delimiter, "", "");
  }

  /**
   * Returns a {@code Collector} that concatenates the input elements,
   * separated by the specified delimiter, with the specified prefix and
   * suffix, in encounter order.
   *
   * @param delimiter the delimiter to be used between each element
   * @param  prefix the sequence of characters to be used at the beginning
   *                of the joined result
   * @param  suffix the sequence of characters to be used at the end
   *                of the joined result
   * @return A {@code Collector} which concatenates CharSequence elements,
   * separated by the specified delimiter, in encounter order
   */
  public static Collector<CharSequence, ?, Source> joining(CharSequence delimiter,
                                                           CharSequence prefix,
                                                           CharSequence suffix) {
      return new CollectorImpl<>(
              () -> new SourceJoiner(delimiter, prefix, suffix),
              SourceJoiner::add, SourceJoiner::merge,
              SourceJoiner::toSource, CH_NOID);
  }

  
  
  /**
   * Simple implementation class for {@code Collector}.
   *
   * @param <T> the type of elements to be collected
   * @param <R> the type of the result
   */
  static class CollectorImpl<T, A, R> implements Collector<T, A, R> {
      private final Supplier<A> supplier;
      private final BiConsumer<A, T> accumulator;
      private final BinaryOperator<A> combiner;
      private final Function<A, R> finisher;
      private final Set<Characteristics> characteristics;

      CollectorImpl(Supplier<A> supplier,
                    BiConsumer<A, T> accumulator,
                    BinaryOperator<A> combiner,
                    Function<A,R> finisher,
                    Set<Characteristics> characteristics) {
          this.supplier = supplier;
          this.accumulator = accumulator;
          this.combiner = combiner;
          this.finisher = finisher;
          this.characteristics = characteristics;
      }

      CollectorImpl(Supplier<A> supplier,
                    BiConsumer<A, T> accumulator,
                    BinaryOperator<A> combiner,
                    Set<Characteristics> characteristics) {
          this(supplier, accumulator, combiner, castingIdentity(), characteristics);
      }

      @Override
      public BiConsumer<A, T> accumulator() {
          return accumulator;
      }

      @Override
      public Supplier<A> supplier() {
          return supplier;
      }

      @Override
      public BinaryOperator<A> combiner() {
          return combiner;
      }

      @Override
      public Function<A, R> finisher() {
          return finisher;
      }

      @Override
      public Set<Characteristics> characteristics() {
          return characteristics;
      }
  }
  
  @SuppressWarnings("unchecked")
  private static <I, R> Function<I, R> castingIdentity() {
      return i -> (R) i;
  }
  
  static final Set<Collector.Characteristics> CH_NOID = Collections.emptySet();
  
  
  public static final Source EMPTY = new StringSource("");
  
  
	
}
