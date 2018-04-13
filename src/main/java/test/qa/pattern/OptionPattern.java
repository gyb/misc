/**
 * 2007-8-31
 */
package test.qa.pattern;

import test.qa.Pattern;
import test.qa.Question;

/**
 * @author dreamsky
 */
public class OptionPattern implements Pattern {
	private static final long serialVersionUID = 1L;
	
	private Pattern question;
	private Pattern index;
	private Object result;
	
	public OptionPattern(Question q, int i) {
		this.question = new ObjectPattern(q);
		this.index = new ObjectPattern(i);
	}
	
	public OptionPattern(Pattern question, Pattern index) {
		this.question = question;
		this.index = index;
	}
	
	/* (non-Javadoc)
	 * @see qa.Pattern#execute()
	 */
	public Object execute() {
		if (result != null) return result;
		
		result = ((Question)question.execute()).getOptionValue((Integer)index.execute());
		return result;
	}

	public void clear() {
		if (result == null) return;
		
		result = null;
		question.clear();
		index.clear();
	}

}
