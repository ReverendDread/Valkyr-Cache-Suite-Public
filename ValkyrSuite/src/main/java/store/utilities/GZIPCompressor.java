package store.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

import store.io.impl.InputStream;

/**
 * A class that represents the gzip (de)compressor.
 * 
 * @author Displee
 */
public class GZIPCompressor {

	/**
	 * The inflater.
	 */
	private static Inflater inflater;

	/**
	 * Inflate a deflated GZIP file.
	 * 
	 * @param inputStream {@link InputStream}
	 * @param data        The delfated GZIP file.
	 */
	public static boolean inflate(InputStream inputStream, byte[] data) {
		try {
			if ((inputStream.getBytes()[inputStream.getPosition()] ^ 0xffffffff) != -32
					|| inputStream.getBytes()[inputStream.getPosition() + 1] != -117) {
				return false;
			}
			if (inflater == null) {
				inflater = new Inflater(true);
			}
			try {
				inflater.setInput(inputStream.getBytes(), 10 + inputStream.getPosition(),
						(-10 - inputStream.getPosition() - (8 - inputStream.getBytes().length)));
				inflater.inflate(data);
			} catch (Exception exception) {
				inflater.reset();
				return false;
			}
			inflater.reset();
		} catch (RuntimeException runtimeException) {
			runtimeException.printStackTrace();
		}
		return true;
	}

	public static byte[] inflate317(byte[] uncompressed) {
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			try (DeflaterOutputStream os = new GZIPOutputStream(bout)) {
				os.write(uncompressed);
				os.finish();
				return bout.toByteArray();
			}
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Deflate an inflated GZIP file.
	 * 
	 * @param data The infalted GZIP file.
	 * @return The defalted GZIP file.
	 */
	public static byte[] deflate(byte[] data) {
		ByteArrayOutputStream compressed = new ByteArrayOutputStream();
		try {
			GZIPOutputStream gzipOutputStream = new GZIPOutputStream(compressed);
			gzipOutputStream.write(data);
			gzipOutputStream.finish();
			gzipOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return compressed.toByteArray();
	}

	private static byte[] gzipBuffer = new byte[0x71868];

	public static byte[] deflate317(byte[] data) {
		int read = 0;
		try {
			if (data.length == 0) {
				return null;
			}
			GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(data));
			do {
				if (read == gzipBuffer.length) {
					throw new RuntimeException("buffer overflow!");
				}

				int in = gis.read(gzipBuffer, read, gzipBuffer.length - read);
				if (in == -1) {
					break;
				}

				read += in;
			} while (true);
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("error unzipping - ");
		}
		byte[] deflated = new byte[read];
		System.arraycopy(gzipBuffer, 0, deflated, 0, deflated.length);
		return deflated;
	}

}