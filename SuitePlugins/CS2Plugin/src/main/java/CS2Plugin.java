import script.CS2Script;
import store.plugin.Plugin;
import store.plugin.PluginType;
import suite.annotation.PluginDescriptor;

/**
 * @author ReverendDread on 7/7/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
@PluginDescriptor(author = "ReverendDread", description = "Basic CS2 editor", type = PluginType.CS2, version = "184")
public class CS2Plugin extends Plugin {

    @Override
    public boolean load() {
        setLoader(new CS2Loader());
        setConfig(new CS2Script());
        setController(new CS2Editor());
        return true;
    }

    @Override
    public String getFXML() {
        return "ConfigEditor.fxml";
    }

}
