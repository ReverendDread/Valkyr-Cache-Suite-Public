/**
 * 
 */
package suite.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author ReverendDread
 * Sep 27, 2019
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface OrderType {

	int priority();
	
}
