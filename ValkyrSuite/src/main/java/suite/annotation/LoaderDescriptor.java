/**
 * 
 */
package suite.annotation;

import store.plugin.PluginType;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
/**
 * @author ReverendDread
 * Oct 6, 2019
 */
public @interface LoaderDescriptor {

	String author() default "";
	String description() default "";
	String version() default "";
	PluginType type();
	
}
