import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import store.CacheLibrary;
import store.cache.index.Index;
import store.cache.index.OSRSIndices;
import store.cache.index.archive.Archive;
import store.cache.index.archive.file.File;
import store.io.impl.InputStream;
import store.plugin.PluginManager;
import store.plugin.PluginType;
import store.plugin.extension.ConfigExtensionBase;
import store.plugin.extension.LoaderExtensionBase;
import suite.annotation.LoaderDescriptor;
import suite.controller.Selection;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author ReverendDread on 12/11/2019
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
@LoaderDescriptor(author = "ReverendDread", description = "Loads textures from the cache", version = "183", type = PluginType.TEXTURE)
public class TextureLoader extends LoaderExtensionBase {

    @Override
    public boolean load() {
        Index index = CacheLibrary.get().getIndex(getIndex());
        int[] fileIds = index.getArchive(getArchive()).getFileIds();
        for (int id : fileIds) {
            File file = index.getArchive(getArchive()).getFile(id);
            if (file == null || file.getData() == null)
                continue;
            TextureConfig def = new TextureConfig();
            InputStream buffer = new InputStream(file.getData());
            def.setId(id);
            def.decode(-1, buffer);
            definitions.put(def.id, def);
            Selection.progressListener.pluginNotify("(" + id + "/" + fileIds.length + ")");
        }
        return true;
    }

    @Override
    public int getFile() {
        return -1;
    }

    @Override
    public int getArchive() {
        return 0;
    }

    @Override
    public int getIndex() {
        return 9;
    }

}
