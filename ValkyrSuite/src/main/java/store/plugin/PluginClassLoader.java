/**
 * 
 */
package store.plugin;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * @author ReverendDread Sep 14, 2019
 */
public class PluginClassLoader<T> extends URLClassLoader {

	public PluginClassLoader(URL url){
		super(new URL[] {url});
	}

	public PluginClassLoader(List<URL> urls) {
		super(urls.toArray(new URL[0]));
	}

}
