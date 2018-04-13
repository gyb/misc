/**
 * 2007-9-4
 */
package test.qa.pattern;

import test.qa.Constants;
import test.qa.Pattern;


/**
 * @author dreamsky
 */
public class PatternFactory {
	public static Pattern createSimplePattern() {
		return new TruePattern();
	}
	
	public static EqualsPattern createSamePattern(ObjectPattern questionSet, ObjectPattern index) {
		QuestionPattern q = new QuestionPattern(questionSet, index);
		return new EqualsPattern(
				new AnswerPattern(q), 
				new AnswerPattern(
						new QuestionPattern(
								questionSet, 
								new OptionPattern(
										q, 
										new AnswerPattern(q)))));
	}
	
	public static EqualsPattern createNthPattern(ObjectPattern questionSet, ObjectPattern index, 
			ObjectPattern n) {
		QuestionPattern q = new QuestionPattern(questionSet, index);
		return new EqualsPattern(
				new OptionPattern(
						q,
						new AnswerPattern(q)),
				new AnswerPattern(
						new QuestionPattern(questionSet, n)));
	}
	
	public static EqualsPattern createNthDistancePattern(ObjectPattern questionSet, ObjectPattern index, 
			ObjectPattern n) {
		QuestionPattern q = new QuestionPattern(questionSet, index);
		AnswerPattern a = new AnswerPattern(q);
		return new EqualsPattern(
				new OptionPattern(q, a),
				new DistancePattern(
						a,
						new AnswerPattern(
								new QuestionPattern(questionSet, n))));
	}
	
	public static EqualsPattern createFirstPattern(ObjectPattern questionSet, ObjectPattern index, 
			ObjectPattern x) {
		QuestionPattern q = new QuestionPattern(questionSet, index);
		return new EqualsPattern(
				new OptionPattern(
						q,
						new AnswerPattern(q)),
				new MinPattern(questionSet, x));
	}
	
	public static EqualsPattern createCountPattern(ObjectPattern questionSet, ObjectPattern index, 
			ObjectPattern x) {
		QuestionPattern q = new QuestionPattern(questionSet, index);
		return new EqualsPattern(
				new OptionPattern(
						q,
						new AnswerPattern(q)),
				new CountPattern(questionSet, x));
	}
	
	public static Pattern createSameCountPattern(ObjectPattern questionSet, ObjectPattern index,
			ObjectPattern x) {
		QuestionPattern q = new QuestionPattern(questionSet, index);
		AnswerPattern a = new AnswerPattern(q);
		Pattern questionAnswer = new OptionPattern(q, a);
		Pattern countX = new CountPattern(questionSet, x);

		BooleanPattern[] options = new BooleanPattern[Constants.QUESTION_OPTIONS_NUM];
		for (int i=0; i<Constants.QUESTION_OPTIONS_NUM; i++) {
			options[i] = new OrPattern(
					new EqualsPattern(
							a, 
							new ObjectPattern(i)),
					new UnEqualsPattern(
							countX,
							new CountPattern(
									questionSet,
									new OptionPattern(
											q,
											new ObjectPattern(i)))));
		}
		return new IfPattern(
				new EqualsPattern(
						questionAnswer,
						new ObjectPattern(-1)),
				new AndPattern(
						options[0],
						new AndPattern(
								options[1],
								new AndPattern(
										options[2],
										new AndPattern(
												options[3],
												options[4])))),
				new EqualsPattern(
						countX,
						new CountPattern(
								questionSet,
								questionAnswer)));
	}
}
