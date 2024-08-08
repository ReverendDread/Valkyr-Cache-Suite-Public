import store.CacheLibrary;
import store.cache.index.Index;
import store.cache.index.archive.file.File;
import store.io.impl.InputStream;
import store.plugin.PluginType;
import store.plugin.extension.LoaderExtensionBase;
import suite.annotation.LoaderDescriptor;
import suite.controller.Selection;

/**
 * @author ReverendDread on 5/17/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
public class EnumLoader extends LoaderExtensionBase {

    @Override
    public boolean load() {
        try {
            Index index = CacheLibrary.get().getIndex(getIndex());
            int[] fileIds = index.getArchive(getArchive()).getFileIds();
            for (int id : fileIds) {
                File file = index.getArchive(getArchive()).getFile(id);
                if (file == null || file.getData() == null)
                    continue;
                EnumConfig definition = new EnumConfig();
                definition.id = id;
                InputStream buffer = new InputStream(file.getData());
                readConfig(buffer, definition);
                definitions.put(id, definition);
                Selection.progressListener.pluginNotify("(" + id + "/" + fileIds.length + ")");
            }
            return true;
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public int getFile() {
        return -1;
    }

    @Override
    public int getArchive() {
        return 8;
    }

    @Override
    public int getIndex() {
        return 2;
    }
}
