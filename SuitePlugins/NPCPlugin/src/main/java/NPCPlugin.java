import lombok.var;
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
@PluginDescriptor(author = "ReverendDread", type = PluginType.NPC, version = "223")
public class NPCPlugin extends Plugin {

	@Override
	public boolean load() {
		setConfig(new NPCConfig());
		setLoader(new NPCLoader());
		setController(new NPCEditor());
		return true;
	}
	
	@Override
	public String getFXML() {
		return "ConfigEditor.fxml";
	}

}
