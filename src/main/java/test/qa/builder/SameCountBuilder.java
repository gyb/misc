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
public class SameCountBuilder extends AbstractBuilder {
	
	/* (non-Javadoc)
	 * @see q10.QuestionBuilder#create(q10.AnswerSet, int)
	 */
	public Question create(QuestionSet set, int index) {
		int option = random.nextInt(Constants.QUESTION_OPTIONS_NUM);
		int[] count = new int[Constants.QUESTION_OPTIONS_NUM];
		for (int i=0; i<Constants.QUESTION_OPTIONS_NUM; i++) {
			count[i] = set.count(new int[] {i});
		}
		Question question = new Question();
		question.setContent("答案为" + Constants.options[option] + "的题目个数与答案是什么的题目个数相同？");
		question.setAnswer(set.getAnswer(index));

		int same = -1;
		for (int i=0; i<Constants.QUESTION_OPTIONS_NUM; i++) {
			if (i == option) continue;
			if (count[i] == count[option]) {
				if (same < 0 || random.nextInt(2) > 0) {
					same = i;
				}
			}
		}
		
		String[] options = new String[Constants.QUESTION_OPTIONS_NUM];
		Object[] optionValues = new Object[Constants.QUESTION_OPTIONS_NUM];
		for (int i=0; i<Constants.QUESTION_OPTIONS_NUM; i++) {
			if (i == option) {
				options[i] = "其它选项均不正确";
				optionValues[i] = -1;
			} else {
				options[i] = Constants.options[i];
				optionValues[i] = i;
			}
		}
		for (int i=0; i<Constants.QUESTION_OPTIONS_NUM; i++) {
			int next = random.nextInt(Constants.QUESTION_OPTIONS_NUM);
			if (i != next) {
				swap(options, i, next);
				swap(optionValues, i, next);
			}
		}
		for (int i=0; i<Constants.QUESTION_OPTIONS_NUM; i++) {
			if (optionValues[i].equals(same)) {
				swap(options, i, set.getAnswer(index));
				swap(optionValues, i, set.getAnswer(index));
				break;
			}
		}
		question.setOptions(options);
		question.setOptionValues(optionValues);
		question.setPattern(PatternFactory.createSameCountPattern(new ObjectPattern(set), 
				new ObjectPattern(index), new ObjectPattern(option)));
		return question;
	}

}