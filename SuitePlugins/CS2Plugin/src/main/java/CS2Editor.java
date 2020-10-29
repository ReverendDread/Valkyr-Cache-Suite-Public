import misc.CustomTab;
import store.plugin.PluginType;
import store.plugin.extension.FXController;
import suite.controller.ConfigEditor;
import utility.ConfigEditorInfo;

import javax.annotation.Nullable;

public class CS2Editor extends ConfigEditor {

    @Override
    public ConfigEditorInfo getInfo() {
        return ConfigEditorInfo.builder().index(12).type(PluginType.CS2).build();
    }

}
