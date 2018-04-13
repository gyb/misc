/**
 * 2007-8-23
 */
package test.qa;

import java.io.Serializable;


/**
 * @author dreamsky
 */
public class Question implements Serializable {
	private static final long serialVersionUID = 5195663681064014701L;
	private int answer;
	private String content;
	private String[] options;
	private Object[] optionValues;
	private Pattern pattern;
	
	public int getAnswer() {
		return answer;
	}
	public void setAnswer(int answer) {
		this.answer = answer;
	}
	public Object getOptionValue(int index) {
		return optionValues[index];
	}
	public void setOptionValues(Object[] optionValues) {
		this.optionValues = optionValues;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String[] getOptions() {
		return options;
	}
	public void setOptions(String[] options) {
		this.options = options;
	}
	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}
	public Pattern getPattern() {
		return pattern;
	}

}
