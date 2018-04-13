/**
 * 2007-9-2
 */
package test.qa.builder;

import test.qa.Constants;
import test.qa.Question;
import test.qa.QuestionBuilder;
import test.qa.QuestionSet;
import test.qa.pattern.PatternFactory;

/**
 * @author dreamsky
 */
public class SimpleBuilder implements QuestionBuilder {

	/* (non-Javadoc)
	 * @see q10.QuestionBuilder#create(q10.QuestionSet, int)
	 */
	public Question create(QuestionSet set, int index) {
		Question question = new Question();
		question.setContent("本题的答案为？");
		question.setAnswer(set.getAnswer(index));
		String[] options = new String[Constants.QUESTION_OPTIONS_NUM];
		Object[] optionValues = new Object[Constants.QUESTION_OPTIONS_NUM];
		for (int i=0; i<options.length; i++) {
			options[i] = Constants.options[i];
			optionValues[i] = i;
		}
		question.setOptions(options);
		question.setOptionValues(optionValues);
		question.setPattern(PatternFactory.createSimplePattern());
		return question;
	}

}
