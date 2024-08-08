import store.plugin.Plugin;
import suite.annotation.PluginDescriptor;
import store.plugin.PluginType;

/**
 * @author Andrew on 11/25/2019
 * @project ValkyrCacheSuite
 */
@PluginDescriptor(author = "ReverendDread", description = "Pack's maps into the cache.", version = "183", type = PluginType.MAP)
public class MapPlugin extends Plugin {

    public static UnderlayLoader underlays;
    public static OverlayLoader overlays;

    @Override
    public boolean load() {
        setController(new MapPacker());
        underlays = new UnderlayLoader().load();
        overlays = new OverlayLoader().load();
        return true;
    }

    @Override
    public String getFXML() {
        return "main.fxml";
    }
}
