import store.plugin.PluginType;
import suite.controller.ConfigEditor;
import utility.ConfigEditorInfo;

/**
 * @author ReverendDread on 5/21/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
public class SpotAnimEditor extends ConfigEditor {

    @Override
    public ConfigEditorInfo getInfo() {
        return ConfigEditorInfo.builder().index(2).archive(13).type(PluginType.SPOT_ANIM).build();
    }

}
