/**
 * 2007-9-2
 */
package test.qa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import test.qa.builder.CountBuilder;
import test.qa.builder.FirstBuilder;
import test.qa.builder.NthBuilder;
import test.qa.builder.NthDistanceBuilder;
import test.qa.builder.SameBuilder;
import test.qa.builder.SameCountBuilder;
import test.qa.builder.SimpleBuilder;
import test.qa.pattern.IfPattern;

/**
 * @author dreamsky
 */
public class Designer {
	private static Random random = new Random();
	private List<QuestionBuilder> builders;
	
	public Designer() {
		builders = new ArrayList<QuestionBuilder>();
		builders.add(new SimpleBuilder());
		builders.add(new NthBuilder());
		builders.add(new NthBuilder());
		builders.add(new SameBuilder());
		builders.add(new SameBuilder());
		builders.add(new CountBuilder());
		builders.add(new CountBuilder());
		builders.add(new NthDistanceBuilder());
		builders.add(new SameCountBuilder());
		builders.add(new FirstBuilder());
	}
	
	public QuestionSet build() {
		QuestionSet questionSet = new QuestionSetImpl();
		Collections.shuffle(builders, random);
		for (int i=0; i<Constants.QUESTION_NUMBER; i++) {
			Question q = builders.get(i).create(questionSet, i);
			while (q == null) {
				q = builders.get(random.nextInt(builders.size())).create(questionSet, i);
			}
			
			questionSet.set(i, q);
		}
		return questionSet;
	}
	
	public List<int[]> solve(QuestionSet questionSet) {
		for (int i = 0; i < Constants.QUESTION_NUMBER; i++) {
			questionSet.setAnswer(i, 0);
		}
		int k = 0;
		int n = 0;
		List<int[]> answerList = new ArrayList<int[]>();
		while (true) {
			if (validate(questionSet)) {
				n++;
				int[] answers = new int[Constants.QUESTION_NUMBER];
				System.arraycopy(questionSet.getAnswers(), 0, answers, 0,
						Constants.QUESTION_NUMBER);
				answerList.add(answers);
			}
			k++;
			if (k < Constants.QUESTION_OPTIONS_NUM) {
				questionSet.setAnswer(0, k);
			} else {
				k = 0;
				if (next(questionSet, 1))
					break;
			}
		}
		return answerList;
	}
	
	private boolean next(QuestionSet set, int n) {
		if (n == Constants.QUESTION_NUMBER) {
			return true;
		}
		int k = set.getAnswer(n);
		k++;
		if (k < Constants.QUESTION_OPTIONS_NUM) {
			set.setAnswer(n, k);
			for (int i = 0; i < n; i++) {
				set.setAnswer(i, 0);
			}
			return false;
		} else {
			return next(set, n + 1);
		}
	}
	
	public boolean validate(QuestionSet set) {
		//this IfPattern was large and slow, so there's a trick that moving it to the last.
		IfPattern ifP = null;
		for (int i = 0; i < Constants.QUESTION_NUMBER; i++) {
			Pattern p = set.get(i).getPattern();
			if (ifP == null) {
				if (p instanceof IfPattern) {
					ifP = (IfPattern)p;
					continue;
				}
			}
			if (!trueOrFalse(p.execute())) {
				if (ifP == null) {
					for (int j=0; j<=i; j++) {
						set.get(j).getPattern().clear();
					}
				} else {
					for (int j=0; j<=i; j++) {
						Pattern pt = set.get(j).getPattern();
						if (pt instanceof IfPattern) continue;
						pt.clear();
					}
				}
				return false;
			}
		}
		boolean result = (Boolean)ifP.execute();
		for (int i=0; i<Constants.QUESTION_NUMBER; i++) {
			set.get(i).getPattern().clear();
		}
		return result;
	}
	
	private boolean trueOrFalse(Object obj) {
		if (obj instanceof Boolean)
			return (Boolean)obj;
		return (obj != null);
	}
	
	public void outputQuestion(QuestionSet set) {
		for (int i=0; i<Constants.QUESTION_NUMBER; i++) {
			Question q = set.get(i);
			System.out.println((i + 1) + ". " + q.getContent());
			for (int j=0; j<Constants.QUESTION_OPTIONS_NUM; j++) {
				System.out.print("(" + Constants.options[j] + ")" + q.getOptions()[j] + "  ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		Designer designer = new Designer();
		long totalTime = 0;
		for (int i = 0; i < 10; i++) {
			QuestionSet set = designer.build();
			designer.outputQuestion(set);
			System.out.println("正在计算答案中...");
			long t = System.currentTimeMillis();
			List<int[]> answerList = designer.solve(set);
			System.out.println("耗时" + (System.currentTimeMillis() - t)
					+ "毫秒，共找到" + answerList.size() + "组答案:");
			totalTime += System.currentTimeMillis() - t;
			for (int[] answers : answerList) {
				for (int answer : answers) {
					System.out.print(Constants.options[answer]);
				}
				System.out.println();
			}
		}
		System.out.println("计算10组题目答案，共耗时" + totalTime + "毫秒!");
	}

}
