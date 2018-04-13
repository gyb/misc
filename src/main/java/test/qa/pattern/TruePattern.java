/**
 * 2007-9-6
 */
package test.qa.pattern;

/**
 * @author dreamsky
 */
public class TruePattern implements BooleanPattern {
	private static final long serialVersionUID = 1L;
	
	/* (non-Javadoc)
	 * @see q10.Pattern#execute()
	 */
	public Boolean execute() {
		return Boolean.TRUE;
	}

	public void clear() {
		//nothing to do
	}

}
