/**
 * 
 */


import store.plugin.PluginType;
import suite.controller.ConfigEditor;
import utility.ConfigEditorInfo;

/**
 * @author ReverendDread
 * Sep 23, 2019
 */
public class SequenceEditor extends ConfigEditor {

	@Override
	public ConfigEditorInfo getInfo() {
		return ConfigEditorInfo.builder().index(2).archive(12).type(PluginType.SEQUENCE).build();
	}
}

