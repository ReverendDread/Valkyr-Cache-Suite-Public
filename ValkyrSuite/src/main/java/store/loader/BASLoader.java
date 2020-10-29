package store.loader;

import lombok.Getter;
import store.CacheLibrary;
import store.cache.index.Index;
import store.codec.BASDefinition;
import store.codec.util.Utils;
import store.io.impl.InputStream;

/**
 * @author ReverendDread Jun 28, 2018
 */
public class BASLoader implements DefinitionLoader {

	/**
	 * The {@link FileStore} being used.
	 */
	private CacheLibrary cache;

	/**
	 * Array for loaded definitions.
	 */
	@Getter
	private static BASDefinition[] definitions;

	/**
	 * Creates a new {@code BASLoader} and loads all BAS definitions.
	 * 
	 * @param cache the cache to load from.
	 */
	public BASLoader(CacheLibrary cache) {
		this.cache = cache;
		long start = System.currentTimeMillis();
		if (!initialize())
			System.err.println("Failed to load BAS Definitions.");
		else
			System.out.println("[BASLoader] Took " + (System.currentTimeMillis() - start) + " millis to load "
					+ definitions.length + " definitions.");
	}

	@Override
	public boolean initialize() {
		try {
			Index index = cache.getIndex(2);
			int size = Utils.getRenderAnimationDefintionsSize(cache);
			definitions = new BASDefinition[size];
			for (int id = 0; id < size; id++) {
				BASDefinition definition = new BASDefinition(id);
				try {
					byte[] data = index.getArchive(32).getFile(id).getData();
					if (data != null)
						definition.decode(new InputStream(data));
				} catch (Exception e) {
					if (suite.Constants.settings.debug)
						System.err.println("No data exists for BAS " + id + ".");
				}
				definitions[id] = definition;
			}
			return true;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void reload() {
		definitions = null;
		initialize();
	}

}
