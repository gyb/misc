/**
 * 2007-9-6
 */
package test.qa.pattern;


/**
 * @author dreamsky
 */
public class OrPattern implements BooleanPattern {
	private static final long serialVersionUID = 1L;
	
	private BooleanPattern a;
	private BooleanPattern b;
	private Boolean result;
	
	public OrPattern(BooleanPattern a, BooleanPattern b) {
		this.a = a;
		this.b = b;
	}
	
	public Boolean execute() {
		if (result != null) return result;

		result = a.execute() || b.execute();
		return result;
	}
	
	public void clear() {
		if (result == null) return;
		
		result = null;
		a.clear();
		b.clear();
	}
	
}
