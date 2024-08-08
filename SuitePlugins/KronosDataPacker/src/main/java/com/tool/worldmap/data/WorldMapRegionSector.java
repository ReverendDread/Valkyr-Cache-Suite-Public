package com.tool.worldmap.data;

import com.tool.worldmap.decor.WorldMapDecoration;
import com.util.Sprite;
import store.cache.index.Index;
import store.io.impl.InputStream;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author ReverendDread on 7/8/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
public class WorldMapRegionSector extends AbstractWorldMapSector {

    @Override
    void init(InputStream buffer) {
        super.planes = Math.min(super.planes, 4);
        super.underlays = new int[1][64][64];
        super.overlays = new int[super.planes][64][64];
        super.overlayShape = new int[super.planes][64][64];
        super.overlayRotation = new int[super.planes][64][64];
        super.decorations = new WorldMapDecoration[super.planes][64][64][];
        int i = buffer.readUnsignedByte();
        if (i != 0) {
            throw new IllegalStateException("");
        } else {
            int regionX = buffer.readUnsignedByte();
            int regionY = buffer.readUnsignedByte();
            if(regionX == super.regionX && regionY == super.regionY) {
                for(int x = 0; x < 64; ++x) {
                    for(int y = 0; y < 64; ++y) {
                        this.decodeFloor(x, y, buffer);
                    }
                }

            } else {
                throw new IllegalStateException("");
            }
        }
    }

    public void decodeRegional(InputStream var1) {
        int i = var1.readUnsignedByte();
        if(i != 0) {
            throw new IllegalStateException("");
        } else {
            super.minPlane = var1.readUnsignedByte();
            super.planes = var1.readUnsignedByte();
            super.regionXLow = var1.readUnsignedShort();
            super.regionYLow = var1.readUnsignedShort();
            super.regionX = var1.readUnsignedShort();
            super.regionY = var1.readUnsignedShort();
            super.archiveId = var1.readBigSmart();
            super.fileId = var1.readBigSmart();
        }
    }

    @Override
    public String toString() {
        return "WorldMapRegionSector{" +
                "regionXLow=" + regionXLow +
                ", regionYLow=" + regionYLow +
                ", regionX=" + regionX +
                ", regionY=" + regionY +
                ", archiveId=" + archiveId +
                ", fileId=" + fileId +
                ", minPlane=" + minPlane +
                ", planes=" + planes +
                ", field1908=" + field1908 +
                ", field1909=" + field1909 +
                ", underlays=" + Arrays.deepToString(underlays) +
                ", overlays=" + Arrays.deepToString(overlays) +
                ", overlayShape=" + Arrays.deepToString(overlayShape) +
                ", overlayRotation=" + Arrays.deepToString(overlayRotation) +
                ", decorations=" + Arrays.deepToString(decorations) +
                '}';
    }
}
