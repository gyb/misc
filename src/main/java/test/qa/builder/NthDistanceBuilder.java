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
public class NthDistanceBuilder extends AbstractNthBuilder {

	@Override
	protected String[] getOptions() {
		String[] options = new String[Constants.QUESTION_OPTIONS_NUM];
		for (int i=0; i<Constants.QUESTION_OPTIONS_NUM; i++) {
			options[i] = i + "个";
		}
		return options;
	}

	@Override
	protected String getContent(int n) {
		return "按字母顺序，第" + (n + 1) + "题与本题的答案相差几个字母？（例如，A和E相差4个字母, C和B相差1个字母）";
	}

	@Override
	protected Object getAnswerOption(QuestionSet set, int n, int index) {
		return set.distance(n, index);
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
		return PatternFactory.createNthDistancePattern(new ObjectPattern(set),
				new ObjectPattern(index), new ObjectPattern(n));
	}

}
