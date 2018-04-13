/**
 * 2007-8-23
 */
package test.qa;

/**
 * @author dreamsky
 */
public interface QuestionSet {
	/**
	 * @param index
	 * @return 第index个问题
	 */
	Question get(int index);
	
	/**
	 * @param index
	 * @param question
	 */
	void set(int index, Question question);
	
	/**
	 * @return 得到全部答案
	 */
	int[] getAnswers();
	
	/**
	 * @param index
	 * @return 得到第index题答案
	 */
	int getAnswer(int index);
	
	void setAnswer(int index, int answer);
	
	/**
	 * @param options
	 * @return 满足条件的题目个数
	 */
	int count(int[] options);
	
	/**
	 * @param options
	 * @return 第一个符合条件的题号。如没有则返回-1
	 */
	int min(int[] options);
	
	/**
	 * @param options
	 * @return 最后一个符合条件的题号。如没有则返回-1
	 */
	int max(int[] options);
	
	/**
	 * @param index
	 * @return 第index题后第一个与此题答案相同的题号。如没有则返回-1
	 */
	int minAfterEquals(int index);
	
	/**
	 * @param index
	 * @return 第index题前第一个与此题答案相同的题号。如没有则返回-1
	 */
	int maxBeforeEquals(int index);
	
	/**
	 * @return 第一次出现连续答案相同的题目前面那道题的题号。如没有则返回-1
	 */
	int minSeqEquals();
	
	/**
	 * @return 最后一次出现连续答案相同的题目前面那道题的题号。如没有则返回-1
	 */
	int maxSeqEquals();
	
	/**
	 * @param index1
	 * @param index2
	 * @return 两道题目答案相差的字母数（A和E相差4个字母）
	 */
	int distance(int index1, int index2);
}
