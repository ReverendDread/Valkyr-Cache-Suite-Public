import store.plugin.Plugin;
import suite.annotation.PluginDescriptor;
import store.plugin.PluginType;

/**
 * 
 */

@PluginDescriptor(author = "ReverendDread", description = "A simple configuration editor.", type = PluginType.ITEM, version = "183")
public class ItemPlugin extends Plugin {

	@Override
	public boolean load() {
		setConfig(new ItemConfig());
		setLoader(new ItemLoader());
		setController(new ItemEditor());
		return true;
	}

	@Override
	public String getFXML() {
		return "ConfigEditor.fxml";
	}

}
