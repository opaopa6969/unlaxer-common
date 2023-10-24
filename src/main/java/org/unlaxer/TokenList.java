package org.unlaxer;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class TokenList implements List<Token>{
  
  List<Token> tokens;

  public TokenList(List<Token> tokens) {
    super();
    this.tokens = tokens;
  }

  public void forEach(Consumer<? super Token> action) {
    tokens.forEach(action);
  }

  public int size() {
    return tokens.size();
  }

  public boolean isEmpty() {
    return tokens.isEmpty();
  }

  public boolean contains(Object o) {
    return tokens.contains(o);
  }

  public Iterator<Token> iterator() {
    return tokens.iterator();
  }

  public Object[] toArray() {
    return tokens.toArray();
  }

  public <T> T[] toArray(T[] a) {
    return tokens.toArray(a);
  }

  public boolean add(Token e) {
    return tokens.add(e);
  }

  public boolean remove(Object o) {
    return tokens.remove(o);
  }

  public boolean containsAll(Collection<?> c) {
    return tokens.containsAll(c);
  }

  public boolean addAll(Collection<? extends Token> c) {
    return tokens.addAll(c);
  }

  public boolean addAll(int index, Collection<? extends Token> c) {
    return tokens.addAll(index, c);
  }

  public boolean removeAll(Collection<?> c) {
    return tokens.removeAll(c);
  }

  public <T> T[] toArray(IntFunction<T[]> generator) {
    return tokens.toArray(generator);
  }

  public boolean retainAll(Collection<?> c) {
    return tokens.retainAll(c);
  }

  public void replaceAll(UnaryOperator<Token> operator) {
    tokens.replaceAll(operator);
  }

  public void sort(Comparator<? super Token> c) {
    tokens.sort(c);
  }

  public void clear() {
    tokens.clear();
  }

  public boolean equals(Object o) {
    return tokens.equals(o);
  }

  public int hashCode() {
    return tokens.hashCode();
  }

  public Token get(int index) {
    return tokens.get(index);
  }

  public boolean removeIf(Predicate<? super Token> filter) {
    return tokens.removeIf(filter);
  }

  public Token set(int index, Token element) {
    return tokens.set(index, element);
  }

  public void add(int index, Token element) {
    tokens.add(index, element);
  }

  public Token remove(int index) {
    return tokens.remove(index);
  }

  public int indexOf(Object o) {
    return tokens.indexOf(o);
  }

  public int lastIndexOf(Object o) {
    return tokens.lastIndexOf(o);
  }

  public ListIterator<Token> listIterator() {
    return tokens.listIterator();
  }

  public ListIterator<Token> listIterator(int index) {
    return tokens.listIterator(index);
  }

  public List<Token> subList(int fromIndex, int toIndex) {
    return tokens.subList(fromIndex, toIndex);
  }

  public Spliterator<Token> spliterator() {
    return tokens.spliterator();
  }

  public Stream<Token> stream() {
    return tokens.stream();
  }

  public Stream<Token> parallelStream() {
    return tokens.parallelStream();
  }
  
  public CursorRange combinedCursorRange() {
    
    if(tokens.isEmpty()) {
      return new CursorRange(new CursorImpl(), new CursorImpl().incrementPosition());
    }
    
    CursorRange first = tokens.get(0).tokenRange;
    CursorRange last = tokens.get(tokens.size()-1).tokenRange;
    
    
    return new CursorRange(
          new CursorImpl()
            .setLineNumber(first.startIndexInclusive.getLineNumber())
            .setPositionInLine(first.startIndexInclusive.getPositionInLine())
            .setPosition(first.startIndexInclusive.getPosition()),
          new CursorImpl()
            .setLineNumber(last.endIndexExclusive.getLineNumber())
            .setPositionInLine(last.endIndexExclusive.getPositionInLine())
            .setPosition(last.endIndexExclusive.getPosition())
     );
  }
  
  public static CursorRange combinedCursorRange(TokenList tokens , Optional<Integer> startPosition) {
    
    if(tokens.isEmpty()) {
      return new CursorRange(new CursorImpl(), new CursorImpl().incrementPosition());
    }
    
    CursorRange first = tokens.get(0).tokenRange;
    CursorRange last = tokens.get(tokens.size()-1).tokenRange;
    
    
    return new CursorRange(
          new CursorImpl()
            .addPosition(new Index(startPosition.orElse(0)))
            .setLineNumber(first.startIndexInclusive.getLineNumber())
            .setPositionInLine(first.startIndexInclusive.getPositionInLine())
            .setPosition(first.startIndexInclusive.getPosition()),
          new CursorImpl()
            .addPosition(new Index(startPosition.orElse(0)))
            .setLineNumber(last.endIndexExclusive.getLineNumber())
            .setPositionInLine(last.endIndexExclusive.getPositionInLine())
            .setPosition(last.endIndexExclusive.getPosition())
     );
  }
  
  public static CursorRange combinedCursorRange(TokenList tokens) {
    
    return combinedCursorRange(tokens, Optional.empty());
  }
}