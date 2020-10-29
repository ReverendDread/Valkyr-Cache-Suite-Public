/**
 * 
 */
package suite.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author ReverendDread
 * Sep 27, 2019
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface LinkedField {

	String groupName();
	
}
