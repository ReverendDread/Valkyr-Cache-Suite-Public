/**
 * 
 */
package utility;

import java.util.concurrent.Callable;

import javafx.concurrent.Task;

/**
 * @author ReverendDread
 * Sep 23, 2019
 */
public class TaskUtil {
	
	public static <T> Task<T> create(Callable<T> callable) {
        return new Task<T>() {
            @Override
            public T call() throws Exception {
                return callable.call();
            }
        };
    }

}
