/**
 * 2007-9-6
 */
package test.qa.pattern;

import test.qa.Pattern;

/**
 * @author dreamsky
 */
public class IfPattern implements Pattern {
	private static final long serialVersionUID = 1L;
	
	private BooleanPattern condition;
	private Pattern then;
	private Pattern other;
	private Object result;
	
	public IfPattern(BooleanPattern condition, Pattern then, Pattern other) {
		this.condition = condition;
		this.then = then;
		this.other = other;
	}

	/* (non-Javadoc)
	 * @see q10.Pattern#execute()
	 */
	public Object execute() {
		if (result != null) return result;
		
		if (condition.execute()) {
			result = then.execute();
		} else {
			result = other.execute();
		}
		return result;
	}

	public void clear() {
		if (result == null) return;
		
		result = null;
		condition.clear();
		then.clear();
		other.clear();
	}

}
