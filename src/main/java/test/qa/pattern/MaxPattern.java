/**
 * 2007-8-31
 */
package test.qa.pattern;

import java.util.Collection;

import test.qa.Constants;
import test.qa.Pattern;
import test.qa.QuestionSet;

/**
 * @author dreamsky
 */
public class MaxPattern implements Pattern {
	private static final long serialVersionUID = 1L;
	
	private Pattern questionSet;
	private Pattern condition;
	private Integer result;
	
	public MaxPattern(Pattern questionSet, Pattern condition) {
		this.questionSet = questionSet;
		this.condition = condition;
	}

	public MaxPattern(QuestionSet questionSet, int x) {
		this.questionSet = new ObjectPattern(questionSet);
		this.condition = new ObjectPattern(x);
	}
	
	public MaxPattern(QuestionSet questionSet, Collection<Integer> c) {
		this.questionSet = new ObjectPattern(questionSet);
		this.condition = new ObjectPattern(c);
	}
	
	/* (non-Javadoc)
	 * @see qa.Pattern#execute()
	 */
	@SuppressWarnings("unchecked")
	public Integer execute() {
		if (result != null) return result;
		
		QuestionSet set = (QuestionSet)questionSet.execute();
		Object obj = condition.execute();
		if (obj instanceof Collection) {
			Collection<Integer> c = (Collection<Integer>)obj;
			for (int i=Constants.QUESTION_NUMBER-1; i>=0; i++) {
				if (c.contains(set.get(i).getAnswer())) {
					result = i;
					return result;
				}
			}
		} else {
			for (int i=Constants.QUESTION_NUMBER-1; i>=0; i++) {
				if (obj.equals(set.get(i).getAnswer())) {
					result = i;
					return result;
				}
			}
		}
		result = -1;
		return result;
	}

	public void clear() {
		if (result == null) return;
		
		result = null;
		questionSet.clear();
		condition.clear();
	}

}
