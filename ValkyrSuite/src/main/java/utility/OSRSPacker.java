package utility;

import store.CacheLibrary;
import store.CacheLibraryMode;
import store.cache.index.Index;
import store.cache.index.OSRSIndices;
import store.cache.index.archive.Archive;
import store.codec.util.Utils;
import store.progress.AbstractProgressListener;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author ReverendDread on 1/17/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
@SuppressWarnings("unused")
public class OSRSPacker {

	private static final DecimalFormat format = new DecimalFormat("#.##");

	public static final String CACHE_DIR = "F:\\Downloads\\mrpvm\\cachenewone\\";
	private static final String OSRS_CACHE = "C:\\Users\\Andrew\\Desktop\\188\\"; //"C:\\Users\\Andrew\\Desktop\\177\\";
	private static final String OSRS_MAPS = "C:\\Users\\Andrew\\Desktop\\177\\";
	private static final String BASES_DIR = "C:\\Users\\Andrew\\Desktop\\dumps\\bases\\";
	private static final String FRAMES_DIR = "C:\\Users\\Andrew\\Desktop\\dumps\\frames\\";
	private static final String SEQS_DIR = "C:\\Users\\Andrew\\Desktop\\seqs\\";
	private static final String NORMAL_CACHE = "F:\\Jankscape Server V1\\data\\cache\\";
	private static final String KRONOS_CACHE = "H:\\Github\\ZarosOSRS\\cache\\";

	private static final int NPC_OFFSET = 20_000;
	private static final int ITEM_OFFSET = 30_000;
	private static final int OVERLAY_OFFSET = 1_000;
	private static final int UNDERLAY_OFFSET = 1_000;
	private static final int MODEL_OFFSET = 300_000;
	private static final int OBJECT_OFFSET = 200_000;
	private static final int FRAMES_OFFSET = 8_000;
	private static final int BASE_OFFSET = 8_000;
	private static final int SPOTS_OFFSET = 5_000;
	private static final int SEQS_OFFSET = 20_000;

	private final static AbstractProgressListener progress = new AbstractProgressListener() {

		@Override
		public void finish(String title, String message) {
			System.out.println("Done!");
		}

		@Override
		public void change(double progress, String message) {
			System.out.println("[" + progress + "%]: " + message);
		}

	};

	public static void main(String[] args) throws IOException {	
//		CacheLibrary cache = new CacheLibrary(KRONOS_CACHE, CacheLibraryMode.UN_CACHED);
//        CacheLibrary osrs_cache = new CacheLibrary(OSRS_CACHE, CacheLibraryMode.UN_CACHED);
//		pack_osrs_dat(cache, osrs_cache);
//		CacheLibrary old = CacheLibrary.create("H:\\Github\\718---Server\\data\\cache\\", CacheLibraryMode.UN_CACHED, null);
//		old.rebuild(new File("H:\\Github\\718---Server\\data\\cleaned-cache\\"));
		CacheLibrary cache = CacheLibrary.create(KRONOS_CACHE, CacheLibraryMode.UN_CACHED, progress);
		Stream.of(cache.getIndex(8).getArchives()).forEach(Archive::flag);
		cache.getIndex(8).update(progress);
	}

	public static void pack_osrs_dat(CacheLibrary cache, CacheLibrary osrs_cache) throws IOException {

		//configs
//    	pack_osrs_flos(osrs_cache, cache);
//   	pack_osrs_flus(osrs_cache, cache);
//		pack_osrs_objects(osrs_cache, cache);
//		pack_osrs_items(osrs_cache, cache);
//		pack_osrs_npcs(osrs_cache, cache);
//		pack_osrs_skins(osrs_cache, cache);
//		pack_osrs_skeletons(osrs_cache, cache);
//		pack_osrs_animations(osrs_cache, cache);
//		pack_osrs_spotanims(cache, osrs_cache);
//		pack_osrs_models(osrs_cache, cache);

		pack_sprite_group(osrs_cache, cache, 423);

		//Interfaces
		//transport_index(CacheLibrary.create("C:\\Users\\Andrew\\Desktop\\cache_742\\", CacheLibraryMode.UN_CACHED, progress), 3, cache, 3);
		//Sprites
		//transport_index(CacheLibrary.create("C:\\Users\\Andrew\\Desktop\\cache_742\\", CacheLibraryMode.UN_CACHED, progress), 8, cache, 8);
		//Clientscripts
		//transport_index(CacheLibrary.create("C:\\Users\\Andrew\\Desktop\\cache_742\\", CacheLibraryMode.UN_CACHED, progress), 12, cache, 12);

		int[] regions = new int[] {
				12961, 9116, 9363, 9619,
				//Theater of blood lobby
				14385, 14386, 14642, 14641,
				//Zulrah
				8751,
				8752,
				9007,
				9008,
				8495,
				8496,
				9773,
				10029,
				10028,
				10284,
				9516, 9517, 9515, 9771, 10027,
				//Zeah
				4668, 4924, 5180, 5436,
				4667, 4923, 5179, 5435,
				4666, 4922, 5178, 5434,
				4665, 4921, 5177, 5433,
				5695, 5951, 6207, 6463, 6719, 6975, 7231, 7487, 7743, 5694, 5950, 6206, 6462, 6718, 6974, 7230, 7486,
				7742, 5693, 5949, 6205, 6461, 6717, 6973, 7229, 7485, 7741, 5692, 5949, 6204, 6460, 6716, 6972, 7228,
				7484, 7740, 5691, 5948, 6203, 6459, 6715, 6971, 7227, 7483, 7739, 5690, 5947, 6202, 6458, 6714, 6970,
				7226, 7482, 7738, 5689, 5946, 6201, 6457, 6713, 6969, 7225, 7481, 7737, 4664, 4920, 5176, 5432, 5688,
				5945, 6200, 6457, 6712, 6968, 7224, 7480, 7736, 4663, 4919, 5176, 5431, 5687, 5944, 6199, 6456, 6711,
				6967, 7223, 7479, 7735, 4662, 6918, 5174, 5430, 5686, 5943, 6198, 6455, 6710, 6966, 7222, 7478, 7734, 5942,
				5685, 5941, 6197, 6453, 6709, 6965, 7221, 7477, 7733, 5684, 5940, 6196, 6452, 6708, 6964, 7220, 7476, 6454, 6709,
				7732, 5175,
				//Fossile island
				14142,
				14393, 14394, 14395, 14396, 14398,
				14649, 14650, 14651, 14652, 14653, 14654,
				14905, 14906, 14907, 14908, 14909, 14910,
				15161, 15162, 15163, 15164, 15165, 15166,
				15417, 15418, 15419, 15420, 15421, 15422,
				//Cerberus
				4883,
				//Skotizo
				6810,
				//Vorkath
				9023,
				//Adamant & rune dragons
				6223,
				//Khorend dungeon
				6556,
				6557,
				6812,
				6813,
				7069,
				6301,
				6299,
				6555,
				6811,
				7067,
				7068,
				//Keymaster
				5139,
				//Inferno
				9043,
				//Myths guild
				9772,
				//Demonic gorillas
				8536, 8280,
				//Raids
				12889, 13145, 13401,
				13141, 13397,
				13140, 13396,
				13139, 13395,
				13138, 13394,
				13137, 13393,
				13136, 13392,
				9043,
				//Gargoyle boss
				6727,
				//Abyssal sire
				11851, 12107, 12363,
				11850, 12106, 12362,
				//Priff
				8501, 8757, 9013, 8500, 8756, 9012, 8499, 8755, 9011,
				12637, 12638, 12639, 12640, 12893, 12894, 12895, 12896,
				13149, 13150, 13151, 13152, 13405, 13406, 13407, 13408,
				//Gauntlet
				7512, 7768,
				//Theatre of blood
				12613, 12612, 12611, 12867, 13123, 13122, 12869, 13125,
				14679, 14680, 14681, 14937, 14936, 14935, 15191, 15192, 15193,
				//Hydra
				5022, 5023, 5024, 5279, 5535, 5280, 5536,
		};
//		pack_regions(cache, osrs_cache, regions);
//		cache.getIndex(5).update(progress);
	}

	private static void pack_osrs_animations(CacheLibrary osrs_cache, CacheLibrary cache) {
		int size = osrs_cache.getIndex(OSRSIndices.CONFIG).getArchive(12).getLastFile().getId();
		Archive archive = osrs_cache.getIndex(OSRSIndices.CONFIG).getArchive(12);
		for (int index = 0; index < size; index++) {
			store.cache.index.archive.file.File file = archive.getFile(index);
			if (Objects.isNull(file) || Objects.isNull(file.getData()))
				continue;
			double percentage = (double) index / (double) size * 100D;
			cache.getIndex(20).addArchive(getConfigArchive((index + SEQS_OFFSET), 7)).addFile(getConfigFile((index + SEQS_OFFSET), 7), file.getData());
			System.out.println("Packing sequences (" + index + "/" + size + ") " + percentage);
		}
		System.out.println("Updating index 20 reference table...");
		cache.getIndex(20).update(progress);
	}

	private static void pack_sprite_group(CacheLibrary source_cache, CacheLibrary target_cache, int group) throws IOException {
		transport_archive(source_cache, 8, target_cache, 8, group);
	}

	private static void pack_osrs_skins(CacheLibrary osrs_cache, CacheLibrary cache) {
		int archiveSize = osrs_cache.getIndex(OSRSIndices.SKINS).getLastArchive().getId();
		for (int archive = 0; archive < archiveSize; archive++) {
			Archive osrs_archive = osrs_cache.getIndex(OSRSIndices.SKINS).getArchive(archive);
			if (Objects.isNull(osrs_archive) || !osrs_archive.containsData())
				continue;
			cache.getIndex(1).addArchive(osrs_archive, true, true, archive + BASE_OFFSET);
		}
		cache.getIndex(1).update(progress);
	}

	private static void pack_osrs_skeletons(CacheLibrary osrs_cache, CacheLibrary cache) {
		int archiveSize = osrs_cache.getIndex(OSRSIndices.SKELETONS).getLastArchive().getId();
		for (int archive = 0; archive < archiveSize; archive++) {
			Archive osrs_archive = osrs_cache.getIndex(OSRSIndices.SKELETONS).getArchive(archive);
			if (Objects.isNull(osrs_archive) || !osrs_archive.containsData())
				continue;
			cache.getIndex(0).addArchive(osrs_archive, true, true, archive + FRAMES_OFFSET);
		}
		cache.getIndex(0).update(progress);
	}

	private static void pack_osrs_npcs(CacheLibrary osrs_cache, CacheLibrary cache) {
		int size = osrs_cache.getIndex(OSRSIndices.CONFIG).getArchive(9).getLastFile().getId();
		Archive archive = osrs_cache.getIndex(OSRSIndices.CONFIG).getArchive(9);
		for (int index = 0; index < size; index++) {
			store.cache.index.archive.file.File file = archive.getFile(index);
			if (Objects.isNull(file) || Objects.isNull(file.getData()))
				continue;
			double percentage = (double) index / (double) size * 100D;
			cache.getIndex(18).addArchive(getConfigArchive((index + NPC_OFFSET), 7)).addFile(getConfigFile((index + NPC_OFFSET), 7), file.getData());
			System.out.println("Packing npcs (" + index + "/" + size + ") " + percentage);
		}
		cache.getIndex(18).update(progress);
	}

	private static void pack_osrs_items(CacheLibrary osrs_cache, CacheLibrary cache) {
		int size = osrs_cache.getIndex(OSRSIndices.CONFIG).getArchive(10).getLastFile().getId();
		Archive archive = osrs_cache.getIndex(OSRSIndices.CONFIG).getArchive(10);
		for (int index = 0; index < size; index++) {
			store.cache.index.archive.file.File file = archive.getFile(index);
			if (Objects.isNull(file) || Objects.isNull(file.getData()))
				continue;
			double percentage = (double) index / (double) size * 100D;
			cache.getIndex(19).addArchive(getConfigArchive((index + ITEM_OFFSET), 8)).addFile(getConfigFile((index + ITEM_OFFSET), 8), file.getData());
			System.out.println("Packing items (" + index + "/" + size + ") " + percentage);
		}
		cache.getIndex(19).update(progress);
	}

	/**
	 * This actually works on 742 please dont remove.
	 * @param cache
	 * @throws IOException 
	 */
	public static void pack_region(CacheLibrary cache, CacheLibrary osrs_cache, int region) {

		int fromRegionX = (region >> 8);
		int fromRegionY = (region & 0xff);
		int nameHash;
		int archiveId;
		int lastArchive;
		byte[] data;

		//Map data
		String name = "m" + fromRegionX + "_" + fromRegionY;
		archiveId = osrs_cache.getIndex(OSRSIndices.MAPS).getArchiveId(name);
		if (archiveId != -1) {
			data = osrs_cache.getIndex(OSRSIndices.MAPS).getArchive(archiveId).getFile(0).getData();
			nameHash = Utils.getNameHash(name);
			archiveId = cache.getIndex(5).getArchiveId(name);
			lastArchive = cache.getIndex(5).getLastArchive().getId() + 1;
			if (Objects.nonNull(data))
			cache.getIndex(5).addArchive(archiveId == -1 ? lastArchive : archiveId, nameHash, true).addFile(0, data);
		}

		//Location data
		name = "l" + fromRegionX + "_" + fromRegionY;
		archiveId = osrs_cache.getIndex(OSRSIndices.MAPS).getArchiveId(name);
		if (archiveId != -1) {
			data = osrs_cache.getIndex(OSRSIndices.MAPS).getArchive(archiveId, XTEASManager.lookup(region)).getFile(0).getData();
			nameHash = Utils.getNameHash(name);
			archiveId = cache.getIndex(5).getArchiveId(name);
			lastArchive = cache.getIndex(5).getLastArchive().getId() + 1;
			if (Objects.nonNull(data))
			cache.getIndex(5).addArchive(archiveId == -1 ? lastArchive : archiveId, nameHash, true).addFile(0, data);
		}

		System.out.println("Packed region " + region);

	}

	public static void pack_regions(CacheLibrary cache, CacheLibrary osrs_cache, int[] regions) throws IOException {
		double percentage;
		for (int index = 0; index < regions.length; index++) {
			pack_region(cache, osrs_cache, regions[index]);
			percentage = (double) index / (double) (regions.length) * 100D;
			System.out.println("Packing regions: " + format.format(percentage) + "%");
		}
		System.out.println("Done packing regions.");
	}

	private static void dump_maps(CacheLibrary cache) throws IOException {

		File dir = new File("./maps/");
		dir.mkdir();

		for (int region = 0; region < 32768; region++) {

			int x = (region >> 8);
			int y = (region & 0xff);

			byte[] data;
			File file;
			DataOutputStream dos;

			//Map
			int map = cache.getIndex(5).getArchiveId("m" + x + "_" + y);
			if (map != -1) {
				data = cache.getIndex(5).getArchive(map).getFile(0).getData();
				file = new File("./maps/", "m" + x + "_" + y + ".dat");
				dos = new DataOutputStream(new FileOutputStream(file));
				dos.write(data);
				dos.close();
				System.out.println("Dumped map " + file.getName());
			}

			//Locations
			int location =  cache.getIndex(5).getArchiveId("l" + x + "_" + y);
			if (location != -1) {
				int[] xteas = XTEASManager.lookup(region);
				data = cache.getIndex(5).getArchive(location, xteas).getFile(0).getData();
				if (data == null)
					continue;
				file = new File("./maps/", "l" + x + "_" + y + ".dat");
				dos = new DataOutputStream(new FileOutputStream(file));
				dos.write(data);
				dos.close();
				System.out.println("Dumped landscape " + file.getName());
			}

		}
	}

	public static void pack_osrs_spotanims(CacheLibrary cache, CacheLibrary osrs) {
		int length = osrs.getIndex(2).getArchive(13).getLastFile().getId();
		Archive archive = osrs.getIndex(OSRSIndices.CONFIG).getArchive(13);
		for (int index = 0; index < length; index++) {
			store.cache.index.archive.file.File file = archive.getFile(index);
			if (Objects.isNull(file) || Objects.isNull(file.getData()))
				continue;
			try {
				byte[] data = file.getData();
				int id = (index + SPOTS_OFFSET);
				System.out.println("Packing spot " + id + "... Data: " + Arrays.toString(data));
				cache.getIndex(21)
				.addArchive(Utils.getConfigArchive(id, 8))
				.addFile(Utils.getConfigFile(id, 8), data);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		cache.getIndex(21).update(progress);
	}

	private static final void pack_osrs_flos(CacheLibrary osrs_cache, CacheLibrary cache) throws IOException {
	    int size = osrs_cache.getIndex(2).getArchive(4).getLastFile().getId();
	    Archive archive = osrs_cache.getIndex(2).getArchive(4);
		for (int fileId = 0; fileId < size; fileId++) {
			store.cache.index.archive.file.File file = archive.getFile(fileId);
			if (Objects.isNull(file) || Objects.isNull(file.getData()))
				continue;
			byte[] data = file.getData();
			cache.getIndex(2).addArchive(4).addFile((fileId + OVERLAY_OFFSET), data);
		}
	}

	private static final void pack_osrs_flus(CacheLibrary osrs_cache, CacheLibrary cache) throws IOException {
		int size = osrs_cache.getIndex(2).getArchive(1).getLastFile().getId();
		Archive archive = osrs_cache.getIndex(2).getArchive(1);
		for (int fileId = 0; fileId < size; fileId++) {
			store.cache.index.archive.file.File file = archive.getFile(fileId);
			if (Objects.isNull(file) || Objects.isNull(file.getData()))
				continue;
			byte[] data = file.getData();
			cache.getIndex(2).addArchive(1).addFile((fileId + UNDERLAY_OFFSET), data);
		}
	}

	public static void pack_osrs_objects(CacheLibrary osrs_cache, CacheLibrary cache) throws IOException {
		double percentage;
		Index index = osrs_cache.getIndex(2);
		Archive archive = index.getArchive(6);
		if (Objects.isNull(archive)) {
		    return;
        }
		int valid_objects = archive.getLastFile().getId();
		for (int id = 0; id < valid_objects; id++) {
			byte[] data = index.getArchive(6).getFile(id).getData();
			if (data == null)
				continue;
			cache.getIndex(16).addArchive(getConfigArchive(id + OBJECT_OFFSET, 8)).addFile(getConfigFile(id + OBJECT_OFFSET, 8), data);
			percentage = (double) id / (double) valid_objects * 100D;
			System.out.println("Packing objects: " + format.format(percentage) + "%");
		}
		System.out.println("Rewriting table..");
		cache.getIndex(16).update(progress);
	}

	public static void pack_osrs_models(CacheLibrary osrs_cache, CacheLibrary cache) throws IOException {
		double percentage;
		int valid_models = osrs_cache.getIndex(7).getLastArchive().getId();
		for (int index = 0; index < valid_models; index++) {
			store.cache.index.archive.Archive archive = osrs_cache.getIndex(7).getArchive(index);
			if (!archive.containsData())
				continue;
			store.cache.index.archive.file.File file = archive.getFile(0);
			if (Objects.isNull(file) || Objects.isNull(file.getData()))
				continue;
			byte[] data = file.getData();
			cache.getIndex(7).addArchive((index + MODEL_OFFSET)).addFile(0, data);
			percentage = (double) index / (double) valid_models * 100D;
			System.out.println("Packing models: (" + index + "/" + valid_models + ") " + format.format(percentage) + "%");
		}
		System.out.println("Rewriting table..");
		cache.getIndex(7).update(progress);
	}

	private static void transport_archive(CacheLibrary source_cache, int source_id, CacheLibrary target_cache, int target_id, int group_id) throws IOException {
		transport_archive(source_cache, source_id, target_cache, target_id, group_id, true);
	}

	private static void transport_archive(CacheLibrary source_cache, int source_id, CacheLibrary target_cache, int target_id, int group_id, boolean rewrite) throws IOException {
		Index target_index = target_cache.getIndex(target_id);
		System.out.println("Attempting to transport group of id " + group_id + "..");
		if (source_cache.getIndex(source_id).getArchive(group_id) == null) {
		    target_index.addArchive(source_cache.getIndex(source_id + 1000).getArchive(group_id), false, true);
		}
		if (rewrite) {
			target_index.update(progress);
		}
	}

	private static void transport_index(CacheLibrary source_cache, int source_id, CacheLibrary target_cache, int target_id) throws IOException {
		System.out.println("Attempting to transport index from source id of " + source_id + " and target id of " + target_id);
		Index source_index = source_cache.getIndex(source_id);
		if (target_cache.getIndices().length <= target_id) {
			if (target_cache.getIndices().length != target_id) {
				throw new IllegalStateException("The cache has more than one gap between the source_index and the target_index!");
			}
			target_cache.addIndex(source_index.isNamed(), source_index.usingWhirlpool());
			System.out.println("\t ^ Index was created!");
		}

		Index target_index = target_cache.getIndex(target_id);
		int num_groups = source_index.getLastArchive().getId() + 1;
		System.out.println("\t ^ Attempting to pack " + num_groups + " group(s)..");

		double last = 0.0D;
		for (int group_id = 0; group_id < num_groups; group_id++) {
			if (source_index.getArchive(group_id) != null) {
				target_index.addArchive(source_index.getArchive(group_id), true, true);
				double percentage = (double) group_id / (double) num_groups * 100D;
				if (group_id == num_groups - 1 || percentage - last >= 1.0D) {
					System.out.println("\t ^ Percentage Completed: " + format.format(percentage) + "%");
					last = percentage;
				}
			}
		}
		System.out.println("\t ^ Rewriting table..");
		target_index.update();
		System.out.println("\t ^ Finished!");
		System.out.println();
	}

//	public static void reset_index(CacheLibrary store, int index) throws FileNotFoundException, IOException {
//		store.resetIndex(index, store.getIndex()[index].getTable().isNamed(), store.getIndex()[index].getTable().usesWhirpool(), store.getIndex()[index].getTable().getCompression());
//	}

	private static int getConfigArchive(int id, int bits) {
		return (id) >> bits;
	}

	private static int getConfigFile(int id, int bits) {
		return (id) & (1 << bits) - 1;
	}

	private static String stripDat(String name) {
		return name.substring(0, name.length() - ".dat".length());
	}

	private static int stripId(String name) {
		return Integer.parseInt(name.substring(0, name.length() - ".dat".length()));
	}

}