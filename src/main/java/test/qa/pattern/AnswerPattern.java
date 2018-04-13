/**
 * 2007-8-31
 */
package test.qa.pattern;

import test.qa.Pattern;
import test.qa.Question;

/**
 * @author dreamsky
 */
public class AnswerPattern implements Pattern {
	private static final long serialVersionUID = 3249873264556295918L;
	
	private Pattern question;
	private Integer result;
	
	public AnswerPattern(Question q) {
		this.question = new ObjectPattern(q);
	}
	
	public AnswerPattern(Pattern question) {
		this.question = question;
	}

	/* (non-Javadoc)
	 * @see qa.Pattern#execute()
	 */
	public Integer execute() {
		if (result != null) return result;
		
		result = ((Question)question.execute()).getAnswer();
		return result;
	}

	public void clear() {
		if (result == null) return;
		
		result = null;
		question.clear();
	}

}
