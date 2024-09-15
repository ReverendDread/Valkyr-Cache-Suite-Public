package suite.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marker annotation for array fields that cannot be resized.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FixedSize {
}
