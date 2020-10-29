import lombok.extern.slf4j.Slf4j;
import store.plugin.PluginType;
import suite.controller.ConfigEditor;
import utility.ConfigEditorInfo;

/**
 * 
 * @author ReverendDread Jul 10, 2018
 */
public class ItemEditor extends ConfigEditor {

	@Override
	public ConfigEditorInfo getInfo() {
		return ConfigEditorInfo.builder().index(2).archive(10).type(PluginType.ITEM).build();
	}

}
