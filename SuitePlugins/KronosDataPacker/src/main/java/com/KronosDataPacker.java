package com;

import com.google.common.collect.Maps;
import store.CacheLibrary;
import store.cache.index.Index;
import store.cache.index.OSRSIndices;
import store.cache.index.archive.Archive;
import store.cache.index.archive.file.File;
import store.codec.util.Utils;
import store.plugin.Plugin;
import store.plugin.PluginType;
import store.progress.AbstractProgressListener;
import store.progress.ProgressListener;
import suite.annotation.PluginDescriptor;
import suite.controller.Selection;
import utility.XTEASManager;

import java.util.Map;

/**
 * @author ReverendDread on 7/8/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
@PluginDescriptor(author = "ReverendDread", description = "A world map editor.", type = PluginType.WORLD_MAP, version = "184")
public class KronosDataPacker extends Plugin {

    private static final int[] BLACKLIST_ITEMS = {
            22610, 22613, 22616, 22619, 23615, //Vesta's
            22638, 22641, 22644, //Morrigan's
            22647, 22650, 22653, 22656, 23617, // Zuriel's
            22622, 22625, 22628, 22631, 23620, //Statius's
            6758, //Bonus exp scroll
            4067, //Vote ticket
    };
    private static final int[] BLACKLIST_NPCS = {
            2153, 5051, 5081
    };
    private static final int[] BLACKLIST_OBJECTS = {
            23311, 7389, 34752
    };
    private static final int[] BLACKLIST_SPOTS = {

    };
    private static final int[] BLACKLIST_SEQS = {

    };
    private static final int[] BLACKLIST_REGIONS = {
            5535, 7991, 7992, 8248, 8247, 9046, 12342,
    };

    private static final boolean skeletons = true;
    private static final boolean skins = true;
    private static final boolean items = true;
    private static final boolean npcs = true;
    private static final boolean objects = true;
    private static final boolean spots = true;
    private static final boolean models = true;
    private static final boolean sequences = true;
    private static final boolean varbit = true;
    private static final boolean idk = true;
    private static final boolean maps = true;
    private static boolean cs2; //maybe
    private static boolean enums; //maybe
    private static boolean sprites; //
    private static boolean interfaces; //maybe
    
    private static final ProgressListener progressListener = new AbstractProgressListener() {
        
        @Override
        public void finish(String title, String message) {

        }

        @Override
        public void change(double progress, String message) {
            
        }
        
    };

    public static void main(String[] args) {

        progressListener.notify(0, "Initializing Kronos cache");
        CacheLibrary kronos_cache = CacheLibrary.createUncached("H:\\Github\\ZarosOSRS\\cache\\");
        progressListener.notify(0, "Initializing RuneScape cache");
        CacheLibrary runescape_cache = CacheLibrary.createUncached("C:\\Users\\Andrew\\Desktop\\rev190\\cache\\");
        progressListener.notify(0, "Initializing XTEA manager");
        XTEASManager.get().init();

        Index kronos_config = kronos_cache.getIndex(OSRSIndices.CONFIG);
        Index runescape_config = runescape_cache.getIndex(OSRSIndices.CONFIG);

        if (skeletons) {

            Index runescape_skeletons = runescape_cache.getIndex(OSRSIndices.SKELETONS);
            Index kronos_skeletons = kronos_cache.getIndex(OSRSIndices.SKELETONS);

            int skeleton_count = runescape_skeletons.getLastArchive().getId();
            for (int i = 0; i < skeleton_count; i++) {
                kronos_skeletons.addArchive(runescape_skeletons.getArchive(i), true, true, i);
            }

            kronos_skeletons.update(progressListener);

        }

        if (skins) {

            Index runescape_skins = runescape_cache.getIndex(OSRSIndices.SKINS);
            Index kronos_skins = kronos_cache.getIndex(OSRSIndices.SKINS);

            int skin_count = runescape_skins.getLastArchive().getId();
            for (int i = 0; i < skin_count; i++) {
                kronos_skins.addArchive(runescape_skins.getArchive(i), true, true, i);
            }

            kronos_skins.update(progressListener);

        }

        if (items) {

            Archive kronos_items = kronos_config.getArchive(10);
            Archive runescape_items = runescape_config.getArchive(10);

            int item_count = runescape_config.getArchive(10).getLastFile().getId();

            for (int i = 0; i < item_count; i++) {
                if (blacklist(i, BLACKLIST_ITEMS))
                    continue;
                File rs_file = runescape_items.getFile(i);
                if (rs_file == null)
                    continue;
                byte[] rs_data = rs_file.getData();
                if (rs_data == null)
                    continue;
                kronos_items.addFile(i, rs_data);
                progressListener.notify(i / (double) item_count, "Updating item {" + i + "/" + item_count + "}");
            }

        }

        if (npcs) {

            Archive kronos_npcs = kronos_config.getArchive(9);
            Archive runescape_npcs = runescape_config.getArchive(9);

            int npc_count = runescape_config.getArchive(9).getLastFile().getId();

            for (int i = 0; i < npc_count; i++) {
                if (blacklist(i, BLACKLIST_NPCS))
                    continue;
                File rs_file = runescape_npcs.getFile(i);
                if (rs_file == null)
                    continue;
                byte[] rs_data = rs_file.getData();
                if (rs_data == null)
                    continue;
                kronos_npcs.addFile(i, rs_data);
                progressListener.notify(i / (double) npc_count, "Updating npc {" + i + "/" + npc_count + "}");
            }

        }

        if (objects) {

            Archive kronos_objects = kronos_config.getArchive(6);
            Archive runescape_objects = runescape_config.getArchive(6);

            int object_count = runescape_config.getArchive(6).getLastFile().getId();

            for (int i = 0; i < object_count; i++) {
                if (blacklist(i, BLACKLIST_OBJECTS))
                    continue;
                File rs_file = runescape_objects.getFile(i);
                if (rs_file == null)
                    continue;
                byte[] rs_data = rs_file.getData();
                if (rs_data == null)
                    continue;
                kronos_objects.addFile(i, rs_data);
                progressListener.notify(i / (double) object_count, "Updating object {" + i + "/" + object_count + "}");
            }

        }

        if (spots) {

            Archive kronos_spots = kronos_config.getArchive(13);
            Archive runescape_spots = runescape_config.getArchive(13);

            int spots_count = runescape_config.getArchive(13).getLastFile().getId();

            for (int i = 0; i < spots_count; i++) {
                if (blacklist(i, BLACKLIST_SPOTS))
                    continue;
                File rs_file = runescape_spots.getFile(i);
                if (rs_file == null)
                    continue;
                byte[] rs_data = rs_file.getData();
                if (rs_data == null)
                    continue;
                kronos_spots.addFile(i, rs_data);
                progressListener.notify(i / (double) spots_count, "Updating spot {" + i + "/" + spots_count + "}");
            }

        }

        if (sequences) {

            Archive kronos_seqs = kronos_config.getArchive(12);
            Archive runescape_seqs = runescape_config.getArchive(12);

            int sequence_count = runescape_config.getArchive(12).getLastFile().getId();

            for (int i = 0; i < sequence_count; i++) {
                if (blacklist(i, BLACKLIST_SEQS))
                    continue;
                File rs_file = runescape_seqs.getFile(i);
                if (rs_file == null)
                    continue;
                byte[] rs_data = rs_file.getData();
                if (rs_data == null)
                    continue;
                kronos_seqs.addFile(i, rs_data);
                progressListener.notify(i / (double) sequence_count, "Updating sequence {" + i + "/" + sequence_count + "}");
            }

        }

        if (models) {

            Index kronos_models = kronos_cache.getIndex(OSRSIndices.MODELS);
            Index runescape_models = runescape_cache.getIndex(OSRSIndices.MODELS);

            int model_count = runescape_models.getLastArchive().getId();

            for (int i = 0; i < model_count; i++) {
                Archive model = runescape_models.getArchive(i);
                if (model == null)
                    continue;
                File rs_file = model.getFile(0);
                if (rs_file == null)
                    continue;
                byte[] rs_data = rs_file.getData();
                if (rs_data == null)
                    continue;
                kronos_models.addArchive(i).addFile(0, model.getData());
                progressListener.notify(i / (double) model_count, "Updating model {" + i + "/" + model_count + "}");
            }
            kronos_models.update(progressListener);

        }

        if (varbit) {

            Archive kronos_varbit = kronos_config.getArchive(14);
            Archive runescape_varbit = runescape_config.getArchive(14);

            int varbit_count = runescape_varbit.getLastFile().getId();

            for (int i = 0; i < varbit_count; i++) {
                File rs_file = runescape_varbit.getFile(i);
                if (rs_file == null)
                    continue;
                byte[] rs_data = rs_file.getData();
                if (rs_data == null)
                    continue;
                kronos_varbit.addFile(i, rs_data);
                progressListener.notify(i / (double) varbit_count, "Updating sequence {" + i + "/" + varbit_count + "}");
            }

        }

        if (idk) {

            Archive kronos_idk = kronos_config.getArchive(3);
            Archive runescape_idk = runescape_config.getArchive(3);

            int idk_count = runescape_idk.getLastFile().getId();

            for (int i = 0; i < idk_count; i++) {
                File rs_file = runescape_idk.getFile(i);
                if (rs_file == null)
                    continue;
                byte[] rs_data = rs_file.getData();
                if (rs_data == null)
                    continue;
                kronos_idk.addFile(i, rs_data);
                progressListener.notify(i / (double) idk_count, "Updating identity kit {" + i + "/" + idk_count + "}");
            }

        }

        if (maps) {
            Index runescape_maps = runescape_cache.getIndex(OSRSIndices.MAPS);
            Index kronos_maps = kronos_cache.getIndex(OSRSIndices.MAPS);
            Map<Integer, int[]> update_keys = Maps.newHashMap();
            int max_region = Short.MAX_VALUE;
            for (int i = 0; i < max_region; i++) {
                int x = i >> 8;
                int y = i & 0xff;
                if (blacklist(i, BLACKLIST_REGIONS))
                    continue;
                String map_name = "m" + x + "_" + y;
                String land_name = "l" + x + "_" + y;
                int map_id = runescape_maps.getArchiveId(map_name);
                if (map_id != -1) {
                    File map_file = runescape_maps.getFile(map_id, 0);
                    byte[] map_data = map_file.getData();
                    if (map_data != null) {
                        kronos_maps.addArchive(map_id, Utils.getNameHash(map_name), true).addFile(0, map_data);
                    }
                }
                int land_id = runescape_maps.getArchiveId(land_name);
                if (land_id != -1) {
                    int[] xteas = XTEASManager.lookup(i);
                    Archive land_archive = runescape_maps.getArchive(land_id, xteas);
                    File land_file = land_archive.getFile(0);
                    byte[] land_data = land_file.getData();
                    if (land_data != null) {
                        kronos_maps.addArchive(land_id, Utils.getNameHash(land_name), true).addFile(0, land_data);
                        update_keys.put(land_id, xteas);
                    }
                }
                progressListener.notify(i / (double) max_region, "Updating region {" + i + "/" + max_region + "}");
            }
            kronos_maps.update(update_keys);
        }

        //Update config index
        kronos_config.update(progressListener);
    }

    @Override
    public boolean load() {
        try {

//            Index blob = CacheLibrary.get().getIndex(19);
//            Index geom = CacheLibrary.get().getIndex(18);
//            Index ground = CacheLibrary.get().getIndex(20);
//            WorldMap worldMap = new WorldMap();
//            worldMap.decode(blob, geom, ground);
//            WorldMapManager manager = new WorldMapManager(null, null, geom, ground);
//            manager.load(blob, worldMap.currentMapArea.internalName, true);
//
//            int width = manager.getMapArea().getRegionHighX() - manager.getMapArea().getRegionLowX() + 1;
//            int height = manager.getMapArea().getRegionHighY() - manager.getMapArea().getRegionLowY() + 1;
//
//            for (int x = 0; x < width; x++) {
//                for (int y = 0; y < height; y++) {
//                    if (manager.getRegions()[x][y] != null) {
//                        manager.getRegions()[x][y].decode(4, geom, ground);
//                    }
//                }
//            }

            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    @Override
    public String getFXML() {
        return null;
    }

    public static boolean blacklist(int i, int[] array) {
        for (int element : array) {
            if (element == i)
            return true;
        }
        return false;
    }

}
