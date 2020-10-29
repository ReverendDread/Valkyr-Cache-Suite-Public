package store.loader;

import lombok.Getter;
import store.CacheLibrary;
import store.cache.index.Index;
import store.codec.ParticleDefinition;
import store.codec.util.Utils;
import store.io.impl.InputStream;

/**
 * @author ReverendDread Jun 28, 2018
 */
public class ParticleLoader implements DefinitionLoader {

	/**
	 * The {@link FileStore} being used.
	 */
	private CacheLibrary cache;

	/**
	 * Array for loaded definitions.
	 */
	@Getter
	private static ParticleDefinition[] definitions;

	/**
	 * Creates a new {@code ParticleLoader} and loads all particle definitions.
	 * 
	 * @param cache the cache to load from.
	 */
	public ParticleLoader(CacheLibrary cache) {
		this.cache = cache;
		long start = System.currentTimeMillis();
		if (!initialize())
			System.err.println("Failed to load Particle Definitions.");
		else
			System.out.println("[ParticleLoader] Took " + (System.currentTimeMillis() - start) + " millis to load "
					+ definitions.length + " definitions.");
	}

	@Override
	public boolean initialize() {
		try {
			Index index = cache.getIndex(27);
			int size = Utils.getParticleConfigSize(cache);
			definitions = new ParticleDefinition[size];
			for (int id = 0; id < size; id++) {
				ParticleDefinition definition = new ParticleDefinition(id);
				try {
					byte[] data = index.getArchive(0).getFile(id).getData();
					if (data != null)
						definition.decode(new InputStream(data));
				} catch (Exception e) {
					if (suite.Constants.settings.debug)
						System.err.println("No data exists for particle " + id + ".");
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
