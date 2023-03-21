package org.unlaxer.parser.combinator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.unlaxer.Name;
import org.unlaxer.parser.Parser;

public class Reverse extends Chain {

	private static final long serialVersionUID = -4962065414105156677L;

	public Reverse(Name name, List<Parser> children) {
		super(name, reverse(new ArrayList<>(children)));
	}

	public Reverse(List<Parser> children) {
		super(reverse(new ArrayList<>(children)));
	}

	public Reverse() {
		super();
	}

	public Reverse(Name name, Parser... children) {
		super(name, children);
	}

	public Reverse(Name name) {
		super(name);
	}

	public Reverse(Parser... children) {
		super(children);
	}

	static List<Parser> reverse(List<Parser> list) {
		Collections.reverse(list);
		return list;
	}
}