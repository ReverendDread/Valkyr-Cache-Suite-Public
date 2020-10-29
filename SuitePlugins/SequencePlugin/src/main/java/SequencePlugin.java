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
@PluginDescriptor(author = "ReverendDread", description = "A simple configuration editor.", type = PluginType.SEQUENCE, version = "183")
public class SequencePlugin extends Plugin {

	@Override
	public boolean load() {
		setConfig(new SequenceConfig());
		setLoader(new SequenceLoader());
		setController(new SequenceEditor());
		return true;
	}
	
	@Override
	public String getFXML() {
		return "ConfigEditor.fxml";
	}

}
