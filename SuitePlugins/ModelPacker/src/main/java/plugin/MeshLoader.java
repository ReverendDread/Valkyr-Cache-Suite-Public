package plugin;

import store.plugin.extension.LoaderExtensionBase;

/**
 * @author ReverendDread on 7/15/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
public class MeshLoader extends LoaderExtensionBase {

    @Override
    public boolean load() {
        return true;
    }

    @Override
    public int getFile() {
        return -1;
    }

    @Override
    public int getArchive() {
        return -1;
    }

    @Override
    public int getIndex() {
        return 7;
    }

}
