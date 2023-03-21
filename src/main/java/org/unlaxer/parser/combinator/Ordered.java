package org.unlaxer.parser.combinator;

import java.util.List;

import org.unlaxer.Name;
import org.unlaxer.parser.Parser;

/**
 * this class determined {@link NonOrdered}
 *
 */
public class Ordered extends Chain{

	private static final long serialVersionUID = -4021651140760345185L;

	public Ordered(List<Parser> children) {
		super(children);
	}

	public Ordered(Name name, List<Parser> children) {
		super(name, children);
	}

	public Ordered(Name name, Parser... children) {
		super(name, children);
	}

	public Ordered(Parser... children) {
		super(children);
	}

	public Ordered() {
		super();
	}

	public Ordered(Name name) {
		super(name);
	}
}