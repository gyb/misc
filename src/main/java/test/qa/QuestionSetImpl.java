/**
 * 2007-8-23
 */
package test.qa;

import java.io.Serializable;
import java.util.Random;

/**
 * @author dreamsky
 */
public class QuestionSetImpl implements QuestionSet, Serializable {
	private static final long serialVersionUID = -1320704985199853583L;
	
	private Question[] questions;
	private int[] answers;
	private static Random random = new Random();
	
	public QuestionSetImpl() {
		questions = new Question[Constants.QUESTION_NUMBER];
		answers = new int[Constants.QUESTION_NUMBER];
		for (int i=0; i<answers.length; i++) {
			answers[i] = random.nextInt(Constants.QUESTION_OPTIONS_NUM);
		}
	}
	
	/* (non-Javadoc)
	 * @see qa.QuestionSet#get(int)
	 */
	public Question get(int index) {
		return questions[index];
	}
	
	/* (non-Javadoc)
	 * @see qa.QuestionSet#set(int, qa.Question)
	 */
	public void set(int index, Question question) {
		this.questions[index] = question;
		this.answers[index] = question.getAnswer();
	}

	/* (non-Javadoc)
	 * @see q10.QuestionSet#getAnswer(int)
	 */
	public int getAnswer(int index) {
		return answers[index];
	}
	
	/* (non-Javadoc)
	 * @see q10.QuestionSet#setAnswer(int, int)
	 */
	public void setAnswer(int index, int answer) {
		answers[index] = answer;
		questions[index].setAnswer(answer);
	}

	/* (non-Javadoc)
	 * @see q10.QuestionSet#getAnswers()
	 */
	public int[] getAnswers() {
		return answers;
	}
	
	/* (non-Javadoc)
	 * @see qa.QuestionSet#count(int[])
	 */
	public int count(int[] options) {
		int counter = 0;
		for (int answer : answers) {
			if (in(answer, options)) counter++;
		}
		return counter;
	}

	/* (non-Javadoc)
	 * @see qa.QuestionSet#distance(int, int)
	 */
	public int distance(int index1, int index2) {
		return Math.abs(answers[index1] - answers[index2]);
	}

	/* (non-Javadoc)
	 * @see qa.QuestionSet#max(int[])
	 */
	public int max(int[] options) {
		for (int i = Constants.QUESTION_NUMBER - 1; i >= 0; i--) {
			if (in(answers[i], options)) return i;
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see qa.QuestionSet#maxBeforeEquals(int)
	 */
	public int maxBeforeEquals(int index) {
		for (int i = index - 1; i >= 0; i--) {
			if (answers[index] == answers[i]) return i;
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see qa.QuestionSet#maxSeqEquals()
	 */
	public int maxSeqEquals() {
		for (int i = Constants.QUESTION_NUMBER - 1; i > 0; i--) {
			if (answers[i] == answers[i - 1]) return i - 1;
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see qa.QuestionSet#min(int[])
	 */
	public int min(int[] options) {
		for (int i = 0; i < answers.length; i++) {
			if (in(answers[i], options)) return i;
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see qa.QuestionSet#minAfterEquals(int)
	 */
	public int minAfterEquals(int index) {
		for (int i = index + 1; i < Constants.QUESTION_NUMBER; i++) {
			if (answers[i] == answers[index]) return i;
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see qa.QuestionSet#minSeqEquals()
	 */
	public int minSeqEquals() {
		for (int i = 0; i < Constants.QUESTION_NUMBER - 1; i++) {
			if (answers[i] == answers[i + 1]) return i;
		}
		return -1;
	}

	private boolean in(int n, int[] array) {
		for (int i : array) {
			if (n == i) return true;
		}
		return false;
	}
}
