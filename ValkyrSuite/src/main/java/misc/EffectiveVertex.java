/**
 * 
 */
package misc;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ReverendDread
 * Apr 10, 2019
 */
@Getter @Setter
public class EffectiveVertex {

	public int effector, vertex;
	
	/**
	 * @param effector
	 * @param vertex
	 */
	public EffectiveVertex(int effector, int vertex) {
		this.effector = effector;
		this.vertex = vertex;
	}

}
