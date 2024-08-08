package store.loader;

import java.io.IOException;
import java.util.Map;

import com.google.common.collect.Maps;

import lombok.Getter;
import store.CacheLibrary;
import store.CacheLibraryMode;

@Deprecated
public class LoaderManager {

	@Getter private static Map<String, DefinitionLoader> loaders = Maps.newHashMap();
	
	public static final String 
		ITEM = "Item",
		NPC = "NPC", 
		OBJECT = "Object", 
		SPOTS = "Spot", 
		PARTICLE = "Particle",
		BAS = "BAS",
		SPRITE = "Sprite", 
		TEXTURE = "Texture",
		HITMARK = "Hitmark",
		SEQUENCE = "Sequence",
		MESH = "Mesh";
	

	/**
	 * Creates a new loader manager.
	 * 
	 * @param cache
	 */
	public LoaderManager(String cache) {
		try {
			new CacheLibrary(cache, CacheLibraryMode.UN_CACHED);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loadDefinitions();
	}

	/**
	 * Initializes the loaders.
	 */
	private void loadDefinitions() {
		try {
//			loaders.put(ITEM, new ItemLoader());
//			loaders.put(NPC, new NPCLoader());
//			loaders.put(OBJECT, new ObjectLoader());
//			loaders.put(SPRITE, new SpriteLoader());
//			loaders.put(SEQUENCE, new SequenceLoader());
//			loaders.put(MESH, new MeshLoader());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reloads the desired {@code DefinitionLoader}.
	 * 
	 * @param loader the loader value.
	 */
	public static void reload(String loader) {
		loaders.get(loader).reload();
	}

	/**
	 * Gets the desired loader.
	 * 
	 * @param loader the loader value.
	 * @return the {@code DefinitionLoader}.
	 */
	public static DefinitionLoader getLoader(String loader) {
		return loaders.get(loader);
	}

}
