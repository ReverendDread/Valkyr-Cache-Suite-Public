import lombok.extern.slf4j.Slf4j;
import store.CacheLibrary;
import store.cache.index.Index;
import store.cache.index.archive.file.File;
import store.io.impl.InputStream;
import store.plugin.PluginManager;
import store.plugin.PluginType;
import store.plugin.extension.ConfigExtensionBase;
import store.plugin.extension.LoaderExtensionBase;
import suite.controller.Selection;

/**
 * 
 */

/**
 * @author ReverendDread
 * Oct 6, 2019
 */
@Slf4j
public class NPCLoader extends LoaderExtensionBase {

	@Override
	public boolean load() {
		try {
			Index index = CacheLibrary.get().getIndex(getIndex());
			int[] fileIds = index.getArchive(getArchive()).getFileIds();
			int size = fileIds.length;
			for (int id : fileIds) {
				File file = index.getArchive(getArchive()).getFile(id);
				if (file == null)
					continue;
				NPCConfig definition = new NPCConfig();
				if (file.getData() == null)
					continue;
				definition.id = id;
				InputStream buffer = new InputStream(file.getData());
				readConfig(buffer, definition);
				definitions.put(id, definition);
				Selection.progressListener.pluginNotify("(" + id + "/" + size + ")");
			}
			return true;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int getFile() {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public int getArchive() {
		// TODO Auto-generated method stub
		return 9;
	}

	@Override
	public int getIndex() {
		// TODO Auto-generated method stub
		return 2;
	}

}
