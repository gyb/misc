/**
 * 2007-8-29
 */
package test.qa;

import java.io.Serializable;

/**
 * @author dreamsky
 */
public interface Pattern extends Serializable {
	Object execute();
	
	/**
	 * 为提高计算效率，pattern可能会缓存结果，因此当条件改变时，需要清除掉缓存的结果
	 */
	void clear();
}
