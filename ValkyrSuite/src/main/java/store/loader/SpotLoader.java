package store.loader;

import lombok.Getter;
import store.CacheLibrary;
import store.cache.index.Index;
import store.codec.SpotDefinition;
import store.codec.util.Utils;
import store.io.impl.InputStream;

/**
 * @author ReverendDread Jun 28, 2018
 */
public class SpotLoader implements DefinitionLoader {

	/**
	 * The {@link FileStore} being used.
	 */
	private CacheLibrary cache;

	/**
	 * Array for loaded definitions.
	 */
	@Getter
	private static SpotDefinition[] definitions;

	/**
	 * Creates a new {@code SpotLoader} and loads all spot definitions.
	 * 
	 * @param cache the cache to load from.
	 */
	public SpotLoader(CacheLibrary cache) {
		this.cache = cache;
		long start = System.currentTimeMillis();
		if (!initialize())
			System.err.println("Failed to load Spot Definitions.");
		else
			System.out.println("[SpotLoader] Took " + (System.currentTimeMillis() - start) + " millis to load "
					+ definitions.length + " definitions.");
	}

	@Override
	public boolean initialize() {
		try {
			Index index = cache.getIndex(21);
			int size = Utils.getGraphicDefinitionsSize(cache);
			definitions = new SpotDefinition[size];
			for (int id = 0; id < size; id++) {
				SpotDefinition definition = new SpotDefinition(id);
				try {
					byte[] data = index.getArchive(id >>> 8).getFile(id & 0xff).getData();
					if (data != null) {
						definition.decode(new InputStream(data));
					}
				} catch (Exception e) {
					if (suite.Constants.settings.debug)
						System.err.println("No data exists for spotanim " + id + ".");
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
