import store.plugin.Plugin;
import store.plugin.PluginType;
import suite.annotation.PluginDescriptor;

/**
 * @author ReverendDread on 5/21/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
@PluginDescriptor(author = "ReverendDread", type = PluginType.SPOT_ANIM, version = "184")
public class SpotAnimPlugin extends Plugin {

    @Override
    public boolean load() {
        setConfig(new SpotAnimConfig());
        setController(new SpotAnimEditor());
        setLoader(new SpotAnimLoader());
        return true;
    }

    @Override
    public String getFXML() {
        return "ConfigEditor.fxml";
    }

}
