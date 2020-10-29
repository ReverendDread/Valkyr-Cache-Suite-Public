import store.plugin.Plugin;
import store.plugin.PluginType;
import suite.annotation.PluginDescriptor;

/**
 * @author ReverendDread on 5/17/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
@PluginDescriptor(author = "ReverendDread", type = PluginType.ENUM, version = "184")
public class EnumPlugin extends Plugin {

    @Override
    public boolean load() {
        setConfig(new EnumConfig());
        setLoader(new EnumLoader());
        setController(new EnumEditor());
        return true;
    }

    @Override
    public String getFXML() {
        return "ConfigEditor.fxml";
    }
}
