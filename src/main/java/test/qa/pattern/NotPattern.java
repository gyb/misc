/**
 * 2007-9-6
 */
package test.qa.pattern;

/**
 * @author dreamsky
 */
public class NotPattern implements BooleanPattern {
	private static final long serialVersionUID = 1L;
	
	private BooleanPattern pattern;
	private Boolean result;
	
	public NotPattern(BooleanPattern pattern) {
		this.pattern = pattern;
	}
	
	/* (non-Javadoc)
	 * @see q10.pattern.BooleanPattern#execute()
	 */
	public Boolean execute() {
		if (result != null) return result;
		
		result = !pattern.execute();
		return result;
	}

	public void clear() {
		if (result == null) return;
		
		result = null;
		pattern.clear();
	}
	
}
