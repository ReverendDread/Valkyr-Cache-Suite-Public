///**
// * 
// */
//package misc;
//
//import lombok.extern.slf4j.Slf4j;
//import store.CacheLibrary;
//import store.CacheLibraryMode;
//import store.cache.index.Index;
//import store.cache.index.archive.Archive;
//import store.utilities.Miscellaneous;
//
///**
// * @author ReverendDread
// * Oct 9, 2019
// */
//@Slf4j
//public class AnimationConverter {
//
//	private static final String VALIUS_CACHE = System.getProperty("user.home") + "//ValiusCache1//";
//	
//	public static void main(String[] args) {
//		CacheLibrary valius = CacheLibrary.create(VALIUS_CACHE, CacheLibraryMode.UN_CACHED, null);
//		
//		Index index = valius.getIndex(2);
//		
//		for (int id = 0; id < index.getArchives().length; id++) {
//			Archive archive = index.getArchive(id);
//			log.info(archive.getInfo());
//		}
//		
//		log.info("Index {}", index.getId());
//		log.info("Archives {}", index.getArchives().length);
//		
//	}
//	
//}
