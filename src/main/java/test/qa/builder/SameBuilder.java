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
public class SameBuilder extends AbstractBuilder {

	/* (non-Javadoc)
	 * @see q10.QuestionBuilder#create(q10.AnswerSet, int)
	 */
	public Question create(QuestionSet set, int index) {
		Question question = new Question();
		question.setContent("本题与哪道题答案相同？");
		question.setAnswer(set.getAnswer(index));
		Integer[] answers = new Integer[Constants.QUESTION_OPTIONS_NUM];
		String[] options = new String[Constants.QUESTION_OPTIONS_NUM];
		
		int same = -1;
		for (int i=0; i<Constants.QUESTION_NUMBER; i++) {
			if (i == index) continue;
			if (set.getAnswer(i) == set.getAnswer(index)) {
				if (same < 0 || random.nextInt(2) > 0) {
					same = i;
				}
			}
		}
		if (same < 0) return null;
		
		int[] temp = new int[Constants.QUESTION_NUMBER];
		for (int i=0; i<Constants.QUESTION_NUMBER; i++) {
			temp[i] = i;
		}
		shuffle(temp);
		for (int i=0, j=0; i<Constants.QUESTION_OPTIONS_NUM; i++, j++) {
			while (temp[j] == index || temp[j] == same) j++;
			answers[i] = temp[j];
		}
		answers[question.getAnswer()] = same;
		for (int i=0; i<Constants.QUESTION_OPTIONS_NUM; i++) {
			options[i] = "第" + (answers[i] + 1) + "题";
		}
		question.setOptions(options);
		question.setOptionValues(answers);
		question.setPattern(PatternFactory.createSamePattern(
				new ObjectPattern(set), new ObjectPattern(index)));
		return question;
	}

}
