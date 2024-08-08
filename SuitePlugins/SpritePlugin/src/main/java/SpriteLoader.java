/**
 * 
 */


import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import store.CacheLibrary;
import store.cache.index.archive.Archive;
import store.plugin.PluginManager;
import store.plugin.PluginType;
import store.plugin.extension.LoaderExtensionBase;
import suite.controller.Selection;

/**
 * @author ReverendDread Sep 10, 2018
 */
public class SpriteLoader extends LoaderExtensionBase {

	@Override
	public boolean load() {
		try {
			int[] archiveIds = CacheLibrary.get().getIndex(getIndex()).getArchiveIds();
			for (int archiveId : archiveIds) {
				Archive archive = CacheLibrary.get().getIndex(getIndex()).getArchive(archiveId);
				if (Objects.nonNull(archive) && archive.containsData()) {
					definitions.put(archiveId, new SpriteContainer(archiveId));
					Selection.progressListener.pluginNotify("(" + archiveId + "/" + archiveIds.length + ")");
				}
			}
			return true;
		} catch (Exception e) {
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
		return -1;
	}

	@Override
	public int getIndex() {
		// TODO Auto-generated method stub
		return 8;
	}
	
	/**
	 * Checks if the image decoder needs to be cleaned up.
	 * 
	 * @return
	 */
	public static boolean needsCleanup() {
		SpriteLoader loader = (SpriteLoader) PluginManager.get().getLoaderForType(PluginType.SPRITE);
		List<SpriteContainer> containers = loader.getDefinitions().values().stream().filter(Objects::nonNull).map(SpriteContainer.class::cast).collect(Collectors.toList());
		long loaded = containers.stream().filter(config -> config.isLoaded()).count();
		return loaded >= 100;
	}
	
	/**
	 * Checks if cached sprite images needs cleaned up.
	 * 
	 * @return
	 */
	public static void cleanup() {
		if (needsCleanup()) {
			SpriteLoader loader = (SpriteLoader) PluginManager.get().getLoaderForType(PluginType.SPRITE);
			for (int index = 0; index < loader.getDefinitions().size(); index++) {
				SpriteContainer container = (SpriteContainer) loader.getDefinitions().get(index);
				if (Objects.nonNull(container) && container.isLoaded()) {
					container.getImages().clear();
					container.setLoaded(false);
					loader.getDefinitions().put(index, container); //modify it
				}
			}
			System.gc();
		}
	}

}
