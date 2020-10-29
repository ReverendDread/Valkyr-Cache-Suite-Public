import store.CacheLibrary;
import store.cache.index.Index;
import store.cache.index.archive.file.File;
import store.io.impl.InputStream;
import store.plugin.extension.LoaderExtensionBase;
import suite.controller.Selection;

/**
 * @author ReverendDread on 5/21/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
public class SpotAnimLoader extends LoaderExtensionBase {

    @Override
    public boolean load() {
        try {
            Index index = CacheLibrary.get().getIndex(getIndex());
            int[] filesIds = index.getArchive(getArchive()).getFileIds();
            for (int fileId : filesIds) {
                File file = index.getArchive(getArchive()).getFile(fileId);
                if (file == null || file.getData() == null)
                    continue;
                SpotAnimConfig definition = new SpotAnimConfig();
                definition.setId(fileId);
                InputStream buffer = new InputStream(file.getData());
                readConfig(buffer, definition);
                definitions.put(fileId, definition);
                Selection.progressListener.pluginNotify("(" + fileId + "/" + filesIds.length + ")");
            }
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    @Override
    public int getFile() {
        return -1;
    }

    @Override
    public int getArchive() {
        return 13;
    }

    @Override
    public int getIndex() {
        return 2;
    }

}
