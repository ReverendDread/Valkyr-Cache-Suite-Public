package com.tool.worldmap;

import com.util.Sprite;
import store.cache.index.Index;
import store.io.impl.InputStream;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author ReverendDread on 7/8/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
public class WorldMap {

    private Index blob;
    private Index geography;
    private Index ground;
    private HashMap<String, WorldMapArea> details;
    private WorldMapArea mainMapArea;
    public WorldMapArea currentMapArea;
    private int centerTileX;
    private int centerTileY;
    private int field3855;
    private int field3830;
    private int field3864;
    private int field3852;
    private int worldMapTargetX;
    private int worldMapTargetY;
    private float zoom;
    private float zoomTarget;
    private List field3868;
    private Iterator iconIterator;
    private WorldMapManager worldMapManager;
    private HashMap fonts;
    private Sprite[] sprites;

    //Entry point for world map
    public void decode(Index blob, Index geography, Index ground) {
        this.blob = blob;
        this.geography = geography;
        this.ground = ground;
        this.fonts = null;
        int id = blob.getArchiveId(WorldMapArchiveName.DETAILS.format());
        int[] detailIds = blob.getArchive(id).getFileIds();
        this.details = new HashMap<>(detailIds.length);
        for (int index = 0; index < detailIds.length; index++) {
            InputStream buffer = new InputStream(blob.getArchive(id).getFile(detailIds[index]).getData());
            WorldMapArea area = new WorldMapArea();
            area.decodeDetails(buffer, detailIds[index]);
            this.details.put(area.internalName, area);
            if (area.isMain) {
                this.mainMapArea = area;
            }
        }
        setMapAreaInit(this.mainMapArea);
    }

    void setCurrentMapArea(WorldMapArea area) {
        this.currentMapArea = area;
        this.worldMapManager = new WorldMapManager(this.sprites, this.fonts, this.geography, this.ground);
    }

    private void setMapAreaInit(WorldMapArea mainMapArea) {
        if(this.currentMapArea == null || mainMapArea != this.currentMapArea) {
            this.setCurrentMapArea(mainMapArea);
            this.init(-1, -1, -1);
        }
    }

    void init(int z, int x, int y) {
        if(this.currentMapArea != null) {
            int[] coordinates = this.currentMapArea.getAreaCenter(z, x, y);
            if (coordinates == null) {
                coordinates = this.currentMapArea.getAreaCenter(this.currentMapArea.getOrigin().getZ(), this.currentMapArea.getOrigin().getX(), this.currentMapArea.getOrigin().getY());
            }
            this.setCenter(coordinates[0] - this.currentMapArea.getRegionLowX() * 64, coordinates[1] - this.currentMapArea.getRegionLowY() * 64, true);
            this.worldMapTargetX = -1;
            this.worldMapTargetY = -1;
            this.zoom = this.convertZoom(this.currentMapArea.getZoom());
            this.zoomTarget = this.zoom;
            this.field3868 = null;
            this.iconIterator = null;
            this.worldMapManager.nullIcons();
        }
    }

    final void setCenter(int centerX, int centerY, boolean reset) {
        this.centerTileX = centerX;
        this.centerTileY = centerY;
        if (reset) {
            this.reset();
        }
    }

    final void reset() {
        this.field3855 = -1;
        this.field3830 = -1;
        this.field3864 = -1;
        this.field3852 = -1;
    }

    float convertZoom(int zoom) {
        return zoom == 25 ? 1.0F : (zoom == 37 ? 1.5F : (zoom == 50 ? 2.0F : (zoom == 75 ? 3.0F : (zoom == 100 ? 4.0F : 8.0F))));
    }

}
