/**
 * 2007-8-31
 */
package test.qa.pattern;

import test.qa.Pattern;
import test.qa.Question;
import test.qa.QuestionSet;

/**
 * @author dreamsky
 */
public class QuestionPattern implements Pattern {
	private static final long serialVersionUID = 1L;
	
	private Pattern questionSet;
	private Pattern index;
	private Question result;
	
	public QuestionPattern(QuestionSet set, int i) {
		this.questionSet = new ObjectPattern(set);
		this.index = new ObjectPattern(i);
	}
	
	public QuestionPattern(Pattern questionSet, Pattern index) {
		this.questionSet = questionSet;
		this.index = index;
	}
	
	/* (non-Javadoc)
	 * @see qa.Pattern#execute()
	 */
	public Question execute() {
		if (result != null) return result;
		
		result = ((QuestionSet)questionSet.execute()).get((Integer)index.execute());
		return result;
	}

	public void clear() {
		if (result == null) return;
		
		result = null;
		questionSet.clear();
		index.clear();
	}

}
