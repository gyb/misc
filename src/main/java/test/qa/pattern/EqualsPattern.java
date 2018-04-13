/**
 * 2007-8-30
 */
package test.qa.pattern;

import test.qa.Pattern;

/**
 * @author dreamsky
 */
public class EqualsPattern implements BooleanPattern {
	private static final long serialVersionUID = 1L;
	
	private Pattern left;
	private Pattern right;
	private Boolean result;
	
	public EqualsPattern(Pattern left, Pattern right) {
		this.left = left;
		this.right = right;
	}

	/* (non-Javadoc)
	 * @see qa.Pattern#execute()
	 */
	public Boolean execute() {
		if (result != null) return result;
		
		result = left.execute().equals(right.execute());
		return result;
	}

	public void clear() {
		if (result == null) return;
		
		result = null;
		left.clear();
		right.clear();
	}

}
