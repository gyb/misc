package test.qa.pattern;

/**
 * @author dreamsky
 */
public class AndPattern implements BooleanPattern {
	private static final long serialVersionUID = 209248328179990484L;
	
	private BooleanPattern a;
	private BooleanPattern b;
	private Boolean result;
	
	public AndPattern(BooleanPattern a, BooleanPattern b) {
		this.a = a;
		this.b = b;
	}

	public Boolean execute() {
		if (result != null) return result;
		
		result = a.execute() && b.execute();
		return result;
	}

	public void clear() {
		if (result == null) return;
		
		result = null;
		a.clear();
		b.clear();
	}

}
