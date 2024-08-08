package store.cache;

import store.io.impl.InputStream;
import store.io.impl.OutputStream;

/**
 * A class serving as a container where we can read from and write to.
 * 
 * @author Displee
 */
public interface Container {

	/**
	 * Used to read data from an index, an archive or a file.
	 */
	public boolean read(InputStream inputStream);

	/**
	 * Write data to an index, archive or file.
	 */
	public byte[] write(OutputStream outputStream);

}