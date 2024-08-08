import store.plugin.Plugin;
import store.plugin.PluginType;
import suite.annotation.PluginDescriptor;

/**
 * @author ReverendDread on 12/11/2019
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
@PluginDescriptor(author = "ReverendDread", description = "A simple texture editor.", version = "183", type = PluginType.TEXTURE)
public class TexturePlugin extends Plugin {

    @Override
    public boolean load() {
        setController(new TextureController());
        setConfig(new TextureConfig());
        setLoader(new TextureLoader());
        return true;
    }

    @Override
    public String getFXML() {
        return "TextureEditor.fxml";
    }
}
