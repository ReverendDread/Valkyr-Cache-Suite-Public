package store.codec;

import store.CacheLibrary;
import store.io.impl.InputStream;

/**
 * 
 * @author ReverendDread Jun 25, 2018
 */
@Deprecated
public interface AbstractDefinition {

	/**
	 * Decodes a definition.
	 * 
	 * @param stream data {@link InputStream} being read from.
	 */
	public void decode(final InputStream stream);

	/**
	 * Encodes data into a byte array ready to be saved.
	 * 
	 * @return
	 */
	public byte[] encode();

	/**
	 * Saves this definition to its correct index/archive.
	 * 
	 * @param cache TODO
	 * @return true if saving was successful.
	 */
	public boolean save(CacheLibrary cache);

}
