/**
 * 2007-9-2
 */
package test.qa.builder;

import test.qa.Constants;
import test.qa.Question;
import test.qa.QuestionSet;
import test.qa.pattern.ObjectPattern;
import test.qa.pattern.PatternFactory;

/**
 * @author dreamsky
 */
public class CountBuilder extends AbstractBuilder {
	/* (non-Javadoc)
	 * @see q10.QuestionBuilder#create(q10.AnswerSet, int)
	 */
	public Question create(QuestionSet set, int index) {
		int option = random.nextInt(Constants.QUESTION_OPTIONS_NUM);
		Question question = new Question();
		question.setContent("答案为" + Constants.options[option] + "的题目个数是?");
		int count = set.count(new int[] {option});
		if (count >= Constants.QUESTION_OPTIONS_NUM) return null;
		question.setAnswer(set.getAnswer(index));
		String[] options = new String[Constants.QUESTION_OPTIONS_NUM];
		Object[] optionValues = new Object[Constants.QUESTION_OPTIONS_NUM];
		for (int i=0; i<Constants.QUESTION_OPTIONS_NUM; i++) {
			options[i] = i + "个";
			optionValues[i] = i;
		}
		for (int i=0; i<Constants.QUESTION_OPTIONS_NUM; i++) {
			int next = random.nextInt(Constants.QUESTION_OPTIONS_NUM);
			if (i != next) {
				swap(options, i, next);
				swap(optionValues, i, next);
			}
		}
		for (int i = 0; i < Constants.QUESTION_OPTIONS_NUM; i++) {
			if (optionValues[i].equals(count)) {
				if (i != question.getAnswer()) {
					swap(options, i, question.getAnswer());
					swap(optionValues, i, question.getAnswer());
					break;
				}
			}
		}
		question.setOptions(options);
		question.setOptionValues(optionValues);
		question.setPattern(PatternFactory.createCountPattern(new ObjectPattern(set), 
				new ObjectPattern(index), new ObjectPattern(option)));
		return question;
	}

}
