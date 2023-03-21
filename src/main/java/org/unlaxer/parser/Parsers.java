package org.unlaxer.parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Parsers implements List<Parser>  , Serializable{
	
	private static final long serialVersionUID = -5938245876985128589L;
	
	List<Parser> parsers;

	public Parsers(List<Parser> parsers) {
		super();
		this.parsers = parsers;
	}
	
	public Parsers(Parser... parsers) {
		super();
		this.parsers = new ArrayList<>();
		for (Parser parser : parsers) {
			this.parsers.add(parser);
		}
	}



	@Override
	public int size() {
		return parsers.size();
	}

	@Override
	public boolean isEmpty() {
		return parsers.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return parsers.contains(o);
	}

	@Override
	public Iterator<Parser> iterator() {
		return parsers.iterator();
	}

	@Override
	public Object[] toArray() {
		return parsers.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return parsers.toArray(a);
	}

	@Override
	public boolean add(Parser e) {
		return parsers.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return parsers.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return parsers.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends Parser> c) {
		return parsers.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Parser> c) {
		return parsers.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return parsers.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return parsers.retainAll(c);
	}

	@Override
	public void clear() {
		parsers.clear();
	}

	@Override
	public boolean equals(Object o) {
		return parsers.equals(o);
	}

	@Override
	public int hashCode() {
		return parsers.hashCode();
	}

	@Override
	public Parser get(int index) {
		return parsers.get(index);
	}

	@Override
	public Parser set(int index, Parser element) {
		return parsers.set(index, element);
	}

	@Override
	public void add(int index, Parser element) {
		parsers.add(index, element);
	}


	@Override
	public Parser remove(int index) {
		return parsers.remove(index);
	}


	@Override
	public int indexOf(Object o) {
		return parsers.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return parsers.lastIndexOf(o);
	}

	@Override
	public ListIterator<Parser> listIterator() {
		return parsers.listIterator();
	}

	@Override
	public ListIterator<Parser> listIterator(int index) {
		return parsers.listIterator(index);
	}

	@Override
	public List<Parser> subList(int fromIndex, int toIndex) {
		return parsers.subList(fromIndex, toIndex);
	}
}