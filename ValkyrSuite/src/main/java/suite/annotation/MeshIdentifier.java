package suite.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author ReverendDread on 12/9/2019
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface MeshIdentifier {}
