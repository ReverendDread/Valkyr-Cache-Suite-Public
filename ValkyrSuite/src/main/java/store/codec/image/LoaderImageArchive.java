package store.codec.image;

import java.awt.Image;
import java.awt.Toolkit;

import store.CacheLibrary;

public class LoaderImageArchive {

	private byte[] data;

	public LoaderImageArchive(byte[] data) {
		this.data = data;
	}

	public LoaderImageArchive(CacheLibrary cache, int archiveId) {
		this(cache, 32, archiveId, 0);
	}

	private LoaderImageArchive(CacheLibrary cache, int idx, int archiveId, int fileId) {
		this.decodeArchive(cache, idx, archiveId, fileId);
	}

	private void decodeArchive(CacheLibrary cache, int idx, int archiveId, int fileId) {
		byte[] data = cache.getIndex(idx).getArchive(archiveId).getFile(fileId).getData();
		if (data != null) {
			this.data = data;
		}
	}

	public Image getImage() {
		return Toolkit.getDefaultToolkit().createImage(this.data);
	}

	public byte[] getImageData() {
		return this.data;
	}
}
