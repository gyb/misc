/**
 * 2007-9-2
 */
package test.qa.builder;

import java.util.Random;

import test.qa.QuestionBuilder;

/**
 * @author dreamsky
 */
public abstract class AbstractBuilder implements QuestionBuilder {
	protected static Random random = new Random();
	
	protected void swap(Object[] s, int i, int j) {
		Object temp = s[i];
		s[i] = s[j];
		s[j] = temp;
	}

	protected void shuffle(int[] array) {
		int len = array.length;
		for (int i=0; i<len; i++) {
			int t = random.nextInt(len - i) + i;
			if (t != i) {
				int temp = array[i];
				array[i] = array[t];
				array[t] = temp;
			}
		}
	}
}
