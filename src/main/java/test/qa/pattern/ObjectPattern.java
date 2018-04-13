package test.qa.pattern;

import test.qa.Pattern;

public class ObjectPattern implements Pattern {
	private static final long serialVersionUID = 1L;
	
	private Object obj;
	
	public ObjectPattern() {}
	
	public ObjectPattern(Object obj) {
		this.obj = obj;
	}
	
	public void set(Object obj) {
		this.obj = obj;
	}
	
	public Object execute() {
		return obj;
	}

	public void clear() {
		//nothing to do
	}

}
