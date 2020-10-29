package store.codec.util;

import java.math.BigInteger;

import store.CacheLibrary;
import store.io.impl.OutputStream;

public final class Utils {

	public static byte[] cryptRSA(byte[] data, BigInteger exponent, BigInteger modulus) {
		return (new BigInteger(data)).modPow(exponent, modulus).toByteArray();
	}

	public static byte[] getArchivePacketData(int indexId, int archiveId, byte[] archive) {
		OutputStream stream = new OutputStream(archive.length + 4);
		stream.writeByte(indexId);
		stream.writeShort(archiveId);
		stream.writeByte(0);
		stream.writeInt(archive.length);
		int offset = 8;

		for (int var6 = 0; var6 < archive.length; ++var6) {
			if (offset == 512) {
				stream.writeByte(-1);
				offset = 1;
			}

			stream.writeByte(archive[var6]);
			++offset;
		}
		return stream.flip();
	}

	public static int getNameHash(String name) {
		return name.toLowerCase().hashCode();
	}

	public static final int getInterfaceDefinitionsSize(CacheLibrary store) {
		return store.getIndex(3).getLastArchive().getId() + 1;
	}

	public static final int getInterfaceDefinitionsComponentsSize(CacheLibrary store, int interfaceId) {
		return store.getIndex(3).getArchive(interfaceId).getLastFile().getId() + 1;
	}

	public static final int getRenderAnimationDefintionsSize(CacheLibrary store) {
		return store.getIndex(2).getArchive(32).getLastFile().getId() + 1;
	}

	public static final int getAnimationDefinitionsSize(CacheLibrary store) {
		int lastArchiveId = store.getIndex(20).getLastArchive().getId();
		return lastArchiveId * 128 + store.getIndex(20).getArchive(lastArchiveId).getLastFile().getId();
	}

	public static final int getItemDefinitionsSize(CacheLibrary store) {
		int lastArchiveId = store.getIndex(19).getLastArchive().getId();
		return lastArchiveId * 256 + store.getIndex(19).getArchive(lastArchiveId).getLastFile().getId();
	}

	public static int getNPCDefinitionsSize(CacheLibrary store) {
		int lastArchiveId = store.getIndex(18).getLastArchive().getId();
		return lastArchiveId * 256 + store.getIndex(18).getArchive(lastArchiveId).getLastFile().getId();
	}

	public static final int getObjectDefinitionsSize(CacheLibrary store) {
		int lastArchiveId = store.getIndex(16).getLastArchive().getId();
		return lastArchiveId * 256 + store.getIndex(16).getArchive(lastArchiveId).getLastFile().getId();
	}

	public static final int getGraphicDefinitionsSize(CacheLibrary store) {
		int lastArchiveId = store.getIndex(21).getLastArchive().getId();
		return lastArchiveId * 256 + store.getIndex(21).getArchive(lastArchiveId).getLastFile().getId();
	}

	public static int getTextureDiffuseSize(CacheLibrary store) {
		return store.getIndex(9).getLastArchive().getId();
	}

	public static int getSpriteDefinitionSize(CacheLibrary store) {
		return store.getIndex(8).getLastArchive().getId();
	}

	public static int getParticleConfigSize(CacheLibrary store) {
		return store.getIndex(27).getArchive(0).getLastFile().getId() + 1;
	}

	public static int getMagnetConfigSize(CacheLibrary store) {
		return store.getIndex(27).getArchive(1).getLastFile().getId() + 1;
	}

	public static int getConfigArchive(int id, int bits) {
		return (id) >> bits;
	}

	public static int getConfigFile(int id, int bits) {
		return (id) & (1 << bits) - 1;
	}

}
