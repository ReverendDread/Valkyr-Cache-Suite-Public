package com.tool.worldmap;

import com.tool.worldmap.area.WorldMapAreaData;
import com.tool.worldmap.data.WorldMapRegionSector;
import com.util.Sprite;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.cache.index.Index;
import store.cache.index.archive.Archive;
import store.io.impl.InputStream;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author ReverendDread on 7/8/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
@RequiredArgsConstructor @Getter
public class WorldMapManager {

    private final Sprite[] sprites;
    private final HashMap fonts;
    private final Index geography, ground;
    private WorldMapAreaData mapArea;
    private int tileX, tileY;
    private int tileWidth, tileHeight;
    private WorldMapRegion[][] regions;
    private Sprite compositeTextureSprite;
    private HashMap icons;

    public void load(Index index, String name, boolean members) {
        int archiveId = index.getArchiveId(WorldMapArchiveName.DETAILS.format());
        int fileId = index.getArchive(archiveId).getFileId(name);

        InputStream details = new InputStream(index.getArchive(WorldMapArchiveName.DETAILS.format()).getFile(name).getData());
        InputStream composite = new InputStream(index.getArchive(WorldMapArchiveName.COMPOSITE_MAP.format()).getFile(name).getData());
        this.mapArea = new WorldMapAreaData();
        try {
            this.mapArea.decode(details, composite, fileId, members);
        } catch (IllegalStateException ex) {
            return;
        }
        this.tileX = this.mapArea.getRegionLowX() * 64;
        this.tileY = this.mapArea.getRegionLowY() * 64;
        this.tileWidth = (this.mapArea.getRegionHighX() - this.mapArea.getRegionLowX() + 1) * 64;
        this.tileHeight = (this.mapArea.getRegionHighY() - this.mapArea.getRegionLowY() + 1) * 64;
        int width = this.mapArea.getRegionHighX() - this.mapArea.getRegionLowX() + 1;
        int height = this.mapArea.getRegionHighY() - this.mapArea.getRegionLowY() + 1;
        this.regions = new WorldMapRegion[width][height];
        Iterator regionSectors = this.mapArea.regional.iterator();
        while (regionSectors.hasNext()) {
            WorldMapRegionSector sector = (WorldMapRegionSector) regionSectors.next();
            int regionX = sector.getRegionX();
            int regionY = sector.getRegionY();
            int regionLowX = regionX - this.mapArea.getRegionLowX();
            int regionLowY = regionY - this.mapArea.getRegionLowY();
            this.regions[regionLowX][regionLowY] = new WorldMapRegion(regionX, regionY, this.mapArea.getBackgroundColor(), this.fonts);
            this.regions[regionLowX][regionLowY].setRegionSector(sector, this.mapArea.icons);
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (this.regions[x][y] == null) {
                    this.regions[x][y] = new WorldMapRegion(this.mapArea.getRegionLowX() + x, this.mapArea.getRegionLowY() + y, this.mapArea.getBackgroundColor(), this.fonts);
                    this.regions[x][y].addChunks(this.mapArea.chunks, this.mapArea.icons);
                }
            }
        }
        boolean hasCompositeTextures = index.getArchive(WorldMapArchiveName.COMPOSITE_TEXTURE.format()).getFile(name) != null;
        if (hasCompositeTextures) {
            byte[] data = index.getArchive(WorldMapArchiveName.COMPOSITE_TEXTURE.format()).getFile(name).getData();
            this.compositeTextureSprite = Sprite.createSprite(data);
        }
    }

    public final void nullIcons() {
        this.icons = null;
    }

}
