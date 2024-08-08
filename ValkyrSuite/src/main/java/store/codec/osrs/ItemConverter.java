/**
 * 
 */
package store.codec.osrs;

import java.io.IOException;
import java.text.DecimalFormat;

import store.CacheLibrary;
import store.io.impl.InputStream;

/**
 * @author ReverendDread Oct 6, 2018
 */
public class ItemConverter {

	private static final DecimalFormat format = new DecimalFormat("#.##");

	public static void main(String[] args) throws IOException {

		CacheLibrary cache = new CacheLibrary("F:\\Mega-Sausage-Server\\data\\cache\\");
		CacheLibrary osrs = new CacheLibrary("C:\\Users\\Andrew\\Desktop\\177\\");

		final int size = osrs.getIndex(2).getArchive(10).getLastFile().getId();

		System.out.println("Packing " + size + " items...");

		byte[] data;
		final int offset = 30000;
		double progress;

		for (int index = 0; index < size; index++) {
			data = osrs.getIndex(2).getArchive(10).getFile(index).getData();
//			cache.getIndexes()[Indices.ITEMS.getIndex()].putFile(Utils.getConfigArchive((index + 30_000), 8), 
//					Utils.getConfigFile((index + 30_000), 8), Constants.GZIP_COMPRESSION, data, null, false, false, -1, -1);
			ItemDefinition osrs_definition = new ItemDefinition(index + offset, new InputStream(data));
			store.codec.ItemDefinition definition = osrs_definition.to718Item(30_000);
			definition.save(cache);
			progress = ((double) index / (double) size * 100D);
			System.out.println("[Progress -  " + format.format(progress) + "%]");
		}
		cache.getIndex(19).update();
		System.out.println("Done packing data.");

	}

}
