/**
 * 2007-8-31
 */
package test.qa.pattern;

import java.util.Collection;

import test.qa.Pattern;

/**
 * @author dreamsky
 */
public class ContainsPattern implements BooleanPattern {
	private static final long serialVersionUID = 5882215414229778860L;
	
	Pattern object;
	Pattern collection;
	private Boolean result;
	
	public ContainsPattern(Pattern obj, Pattern collection) {
		this.object = obj;
		this.collection = collection;
	}
	
	public ContainsPattern(Object obj, Collection collection) {
		this.object = new ObjectPattern(obj);
		this.collection = new ObjectPattern(collection);
	}

	/* (non-Javadoc)
	 * @see qa.Pattern#execute()
	 */
	public Boolean execute() {
		if (result != null) return result;
		
		Object obj = object.execute();
		Collection col = (Collection)collection.execute();
		result = col.contains(obj);
		return result;
	}

	public void clear() {
		if (result == null) return;
		
		result = null;
		object.clear();
		collection.clear();
	}

}
