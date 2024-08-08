import store.plugin.PluginType;
import suite.controller.ConfigEditor;
import utility.ConfigEditorInfo;

public class ObjectEditor extends ConfigEditor {

	@Override
	public ConfigEditorInfo getInfo() {
		return ConfigEditorInfo.builder().index(2).archive(6).type(PluginType.OBJECT).build();
	}

}
