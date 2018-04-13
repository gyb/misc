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
public class FirstBuilder extends AbstractBuilder {
	
	/* (non-Javadoc)
	 * @see q10.QuestionBuilder#create(q10.AnswerSet, int)
	 */
	public Question create(QuestionSet set, int index) {
		int option = random.nextInt(Constants.QUESTION_OPTIONS_NUM);
		Question question = new Question();
		question.setContent("第一个答案为" + Constants.options[option] + "的题目是？");
		question.setAnswer(set.getAnswer(index));
		int number = set.min(new int[] {option});
		if (number < 0) return null;
		
		String[] options = new String[Constants.QUESTION_OPTIONS_NUM];
		Object[] optionValues = new Object[Constants.QUESTION_OPTIONS_NUM];
		if (number < 5) {
			int n = random.nextInt(number + 1);
			for (int i=0; i<Constants.QUESTION_OPTIONS_NUM; i++) {
				options[i] = "第" + (i + n + 1) + "题";
				optionValues[i] = i + n;
			}
		} else {
			int n = random.nextInt(10 - number);
			for (int i=0; i<Constants.QUESTION_OPTIONS_NUM; i++) {
				options[i] = "第" + (i - n + 6) + "题";
				optionValues[i] = i - n + 5;
			}
		}
		
		for (int i=0; i<Constants.QUESTION_OPTIONS_NUM; i++) {
			int next = random.nextInt(Constants.QUESTION_OPTIONS_NUM);
			if (i != next) {
				swap(options, i, next);
				swap(optionValues, i, next);
			}
		}
		String answer = "第" + (number + 1) + "题";
		for (int i = 0; i < Constants.QUESTION_OPTIONS_NUM; i++) {
			if (options[i].equals(answer)) {
				if (i != question.getAnswer()) {
					swap(options, i, question.getAnswer());
					swap(optionValues, i, question.getAnswer());
					break;
				}
			}
		}
		question.setOptions(options);
		question.setOptionValues(optionValues);
		question.setPattern(PatternFactory.createFirstPattern(new ObjectPattern(set), 
				new ObjectPattern(index), new ObjectPattern(option)));
		return question;
	}

}
