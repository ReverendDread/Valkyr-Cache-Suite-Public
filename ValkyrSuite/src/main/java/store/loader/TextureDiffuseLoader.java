/**
 * 
 */
package store.loader;

import lombok.Getter;
import store.CacheLibrary;
import store.cache.index.Index;
import store.codec.image.texture.TextureDefinition;
import store.codec.util.Utils;
import store.io.impl.InputStream;

/**
 * @author ReverendDread Dec 9, 2018
 */
public class TextureDiffuseLoader implements DefinitionLoader {

	/**
	 * The {@link FileStore} being used.
	 */
	private CacheLibrary cache;

	/** Loaded definitions */
	@Getter
	private static TextureDefinition[] definitions;

	public TextureDiffuseLoader(CacheLibrary cache) {
		this.cache = cache;
		long start = System.currentTimeMillis();
		if (!initialize())
			System.err.println("Failed to load Texture definition.");
		else
			System.out.println("[TextureLoader] Took " + (System.currentTimeMillis() - start) + " millis to load "
					+ definitions.length + " definitions.");
	}

	/* (non-Javadoc)
	 * @see com.alex.loaders.DefinitionLoader#initialize()
	 */
	@Override
	public boolean initialize() {
		try {
			Index index = cache.getIndex(9);
			int size = Utils.getTextureDiffuseSize(cache);
			definitions = new TextureDefinition[size];
			for (int id = 0; id < size; id++) {
				TextureDefinition definition = new TextureDefinition(id);
				try {
					byte[] data = index.getArchive(0).getFile(id).getData();
					if (data != null) {
						definition.decode(new InputStream(data));
					}
				} catch (Exception e) {
					if (suite.Constants.settings.debug)
						System.err.println("No data exists for texture " + id + ".");
				}
				definitions[id] = definition;
			}
			return true;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.alex.loaders.DefinitionLoader#reload()
	 */
	@Override
	public void reload() {
		definitions = null;
		initialize();
	}

}
