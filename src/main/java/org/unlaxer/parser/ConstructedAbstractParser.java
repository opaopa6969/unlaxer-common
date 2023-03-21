package org.unlaxer.parser;

import java.util.List;

import org.unlaxer.Name;

public abstract class ConstructedAbstractParser extends AbstractParser{

	private static final long serialVersionUID = 37196026907568384L;


	public ConstructedAbstractParser(List<Parser> children) {
		super(children);
	}

	public ConstructedAbstractParser(Name name, List<Parser> children) {
		super(name, children);
	}
	
	@SuppressWarnings("unused")
	private ConstructedAbstractParser() {
		super();
	}

	@SuppressWarnings("unused")
	private ConstructedAbstractParser(Name name) {
		super(name);
	}

	@Override
	public void prepareChildren(List<Parser> childrenContainer) {
	}

	@Override
	public Parser createParser() {
		return this;
	}
}