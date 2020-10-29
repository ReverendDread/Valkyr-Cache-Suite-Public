import store.plugin.Plugin;
import suite.annotation.PluginDescriptor;
import store.plugin.PluginType;

/**
 * 
 */

/**
 * @author ReverendDread
 * Oct 9, 2019
 */
@PluginDescriptor(author = "ReverendDread", description = "A simple configuration editor.", type = PluginType.OBJECT, version = "183")
public class ObjectPlugin extends Plugin {

	@Override
	public boolean load() {
		setConfig(new ObjectConfig());
		setLoader(new ObjectLoader());
		setController(new ObjectEditor());
		return true;
	}
	
	@Override
	public String getFXML() {
		return "ConfigEditor.fxml";
	}

}
