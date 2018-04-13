/**
 * 2007-9-2
 */
package test.qa.builder;

import test.qa.Constants;
import test.qa.Question;
import test.qa.QuestionSet;
import test.qa.pattern.EqualsPattern;

/**
 * @author dreamsky
 */
public abstract class AbstractNthBuilder extends AbstractBuilder {

	/* (non-Javadoc)
	 * @see q10.QuestionBuilder#create(q10.QuestionSet, int)
	 */
	public Question create(QuestionSet set, int index) {
		int n = random.nextInt(Constants.QUESTION_NUMBER);
		while (n == index) {
			n = random.nextInt(Constants.QUESTION_NUMBER);
		}
		Question question = new Question();
		question.setContent(getContent(n));
		question.setAnswer(set.getAnswer(index));
		
		String[] options = getOptions();
		Object[] optionValues = getOptionValues();
		for (int i=0; i<Constants.QUESTION_OPTIONS_NUM; i++) {
			int next = random.nextInt(Constants.QUESTION_OPTIONS_NUM);
			if (i != next) {
				swap(options, i, next);
				swap(optionValues, i, next);
			}
		}
		Object answer = getAnswerOption(set, n, index);
		for (int i = 0; i < Constants.QUESTION_OPTIONS_NUM; i++) {
			if (optionValues[i].equals(answer)) {
				if (i != question.getAnswer()) {
					swap(options, i, question.getAnswer());
					swap(optionValues, i, question.getAnswer());
					break;
				}
			}
		}
		question.setOptions(options);
		question.setOptionValues(optionValues);
		question.setPattern(getPattern(set, n, index));
		
		return question;
	}

	
	abstract protected String getContent(int n);

	abstract protected Object[] getOptionValues();
	
	abstract protected Object getAnswerOption(QuestionSet set, int n, int index);
	
	abstract protected String[] getOptions();
	
	abstract protected EqualsPattern getPattern(QuestionSet set, int n, int index);
}
