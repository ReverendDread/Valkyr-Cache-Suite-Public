/**
 * 
 */
package store.plugin;

import com.google.common.collect.Lists;
import lombok.Data;
import store.plugin.extension.ConfigExtensionBase;
import store.plugin.extension.FXController;
import store.plugin.extension.LoaderExtensionBase;
import suite.annotation.PluginDescriptor;

import java.net.URL;
import java.util.List;

/**
 * @author ReverendDread
 * Oct 9, 2019
 */
@Data
public abstract class Plugin {

	private List<URL> fxmlFiles = Lists.newArrayList();

	protected ConfigExtensionBase config;
	protected LoaderExtensionBase loader;
	protected FXController controller;

	private PluginClassLoader classLoader;

	public abstract boolean load();

	public abstract String getFXML();

	public final PluginDescriptor getDescriptor() {
		boolean hasDescriptor = this.getClass().isAnnotationPresent(PluginDescriptor.class);
		return hasDescriptor ? this.getClass().getAnnotation(PluginDescriptor.class) : null;
	}

}
