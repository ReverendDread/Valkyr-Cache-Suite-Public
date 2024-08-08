package plugin;

import store.plugin.Plugin;
import store.plugin.PluginType;
import suite.annotation.PluginDescriptor;

/**
 * @author ReverendDread on 12/11/2019
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
@PluginDescriptor(author = "ReverendDread", description = "Packs models into the cache.", version = "183", type = PluginType.MESH)
public class MeshPackerPlugin extends Plugin {

    @Override
    public boolean load() {
        setController(new MeshPacker());
        setLoader(new MeshLoader());
        return true;
    }

    @Override
    public String getFXML() {
        return "ModelPacker.fxml";
    }
}
