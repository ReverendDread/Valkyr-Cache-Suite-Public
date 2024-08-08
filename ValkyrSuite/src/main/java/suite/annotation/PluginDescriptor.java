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
 * @author ReverendDread Sep 14, 2019
 */
public @interface PluginDescriptor {

	String author() default "";

	String description() default "A simple configuration editor.";

	String version() default "";

	PluginType type();

}
