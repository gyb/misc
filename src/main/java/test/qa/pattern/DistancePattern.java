/**
 * 2007-9-4
 */
package test.qa.pattern;

import test.qa.Pattern;

/**
 * @author dreamsky
 */
public class DistancePattern implements Pattern {
	private static final long serialVersionUID = 1L;
	
	private Pattern a;
	private Pattern b;
	private Integer result;
	
	public DistancePattern(Pattern a, Pattern b) {
		this.a = a;
		this.b = b;
	}
	
	/* (non-Javadoc)
	 * @see qa.Pattern#execute()
	 */
	public Integer execute() {
		if (result != null) return result;
		
		result = Math.abs((Integer)a.execute() - (Integer)b.execute());
		return result;
	}

	public void clear() {
		if (result == null) return;
		
		result = null;
		a.clear();
		b.clear();
	}
	
}
