package utility;

import lombok.Getter;
import lombok.Setter;
import store.plugin.PluginType;

/**
 * @author Andrew on 11/25/2019
 * @project ValkyrCacheSuite
 */
@Getter @Setter
public class PluginMenuItem {

    private String text;
    private PluginType type;

    public static PluginMenuItem create(PluginType type) {
        return new PluginMenuItem(type);
    }

    public PluginMenuItem(PluginType type) {
        this.type = type;
        this.text = toString();
    }

    public String toString() {
        return type.toString();
    }

}
