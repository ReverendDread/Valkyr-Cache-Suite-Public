package com.tool.worldmap.data;

import com.tool.worldmap.decor.WorldMapDecoration;
import lombok.Getter;
import store.io.impl.InputStream;

/**
 * @author ReverendDread on 7/8/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
@Getter
public class WorldMapChunkSector extends AbstractWorldMapSector {

    private int chunkXLow, chunkYLow;
    private int chunkX, chunkY;

    @Override
    void init(InputStream var1) {
        super.planes = Math.min(super.planes, 4);
        super.underlays = new int[1][64][64];
        super.overlays = new int[super.planes][64][64];
        super.overlayShape = new int[super.planes][64][64];
        super.overlayRotation = new int[super.planes][64][64];
        super.decorations = new WorldMapDecoration[super.planes][64][64][];
        int var2 = var1.readUnsignedByte();
        if(var2 != 1) {
            throw new IllegalStateException("");
        } else {
            int var3 = var1.readUnsignedByte();
            int var4 = var1.readUnsignedByte();
            int var5 = var1.readUnsignedByte();
            int var6 = var1.readUnsignedByte();
            if(var3 == super.regionX && var4 == super.regionY && var5 == this.chunkX && var6 == this.chunkY) {
                for(int var7 = 0; var7 < 8; ++var7) {
                    for(int var8 = 0; var8 < 8; ++var8) {
                        this.decodeFloor(var7 + this.chunkX * 8, var8 + this.chunkY * 8, var1);
                    }
                }

            } else {
                throw new IllegalStateException("");
            }
        }
    }

    public void decodeChunk(InputStream var1) {
        int var2 = var1.readUnsignedByte();
        if(var2 != 1) {
            throw new IllegalStateException("");
        } else {
            super.minPlane = var1.readUnsignedByte();
            super.planes = var1.readUnsignedByte();
            super.regionXLow = var1.readUnsignedShort();
            super.regionYLow = var1.readUnsignedShort();
            this.chunkXLow = var1.readUnsignedByte();
            this.chunkYLow = var1.readUnsignedByte();
            super.regionX = var1.readUnsignedShort();
            super.regionY = var1.readUnsignedShort();
            this.chunkX = var1.readUnsignedByte();
            this.chunkY = var1.readUnsignedByte();
            super.archiveId = var1.readBigSmart();
            super.fileId = var1.readBigSmart();
        }
    }

}
