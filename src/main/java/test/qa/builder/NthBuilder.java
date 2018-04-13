/**
 * 2007-9-2
 */
package test.qa.builder;

import test.qa.Constants;
import test.qa.QuestionSet;
import test.qa.pattern.EqualsPattern;
import test.qa.pattern.ObjectPattern;
import test.qa.pattern.PatternFactory;

/**
 * @author dreamsky
 */
public class NthBuilder extends AbstractNthBuilder {

	@Override
	protected String getContent(int n) {
		return "第" + (n + 1) + "题的答案为？";
	}

	@Override
	protected String[] getOptions() {
		String[] options = new String[Constants.QUESTION_OPTIONS_NUM];
		for (int i=0; i<Constants.QUESTION_OPTIONS_NUM; i++) {
			options[i] = Constants.options[i];
		}
		return options;
	}

	@Override
	protected Object getAnswerOption(QuestionSet set, int n, int index) {
		return set.getAnswer(n);
	}

	@Override
	protected Object[] getOptionValues() {
		Object[] optionValues = new Object[Constants.QUESTION_OPTIONS_NUM];
		for (int i=0; i<Constants.QUESTION_OPTIONS_NUM; i++) {
			optionValues[i] = i;
		}
		return optionValues;
	}

	@Override
	protected EqualsPattern getPattern(QuestionSet set, int n, int index) {
		return PatternFactory.createNthPattern(new ObjectPattern(set),
				new ObjectPattern(index), new ObjectPattern(n));
	}
}
