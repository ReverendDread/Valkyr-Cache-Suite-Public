package com.tool.worldmap.data;

import com.tool.worldmap.decor.WorldMapDecoration;
import lombok.Getter;
import store.cache.index.Index;
import store.io.impl.InputStream;

/**
 * @author ReverendDread on 7/8/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
@Getter
public abstract class AbstractWorldMapSector {

    public int regionXLow, regionYLow;
    public int regionX, regionY;
    public int archiveId, fileId;
    public int minPlane, planes;
    public boolean field1908, field1909;
    public int[][][] underlays, overlays, overlayShape, overlayRotation;
    public WorldMapDecoration[][][][] decorations;

    public AbstractWorldMapSector() {
        this.archiveId = -1;
        this.fileId = -1;
        this.field1908 = false;
        this.field1909 = false;
    }

    abstract void init(InputStream var1);

    public void method3299(Index index) {
        if(!this.method3298()) {
            byte[] data = index.getFileData(this.archiveId, this.fileId);
            if(data != null) {
                this.init(new InputStream(data));
                this.field1908 = true;
                this.field1909 = true;
            }

        }
    }

    void decodeFloor(int x, int y, InputStream buffer) {
        int var4 = buffer.readUnsignedByte();
        if (var4 != 0) {
            if ((var4 & 1) != 0) {
                this.decodeFloorBasic(buffer, x, y, var4);
            } else {
                this.decodeFloorDecor(buffer, x, y, var4);
            }
        }
    }

    void decodeFloorDecor(InputStream var3, int x, int y, int var4) {
        int maxZ = ((var4 & 24) >> 3) + 1;
        boolean hasOverlay = (var4 & 2) != 0;
        boolean hasDecor = (var4 & 4) != 0;
        this.underlays[0][x][y] = (short)var3.readUnsignedByte();
        if (hasOverlay) {
            int length = var3.readUnsignedByte();
            for (int z = 0; z < length; ++z) {
                int var10 = var3.readUnsignedByte();
                if (var10 != 0) {
                    this.overlays[z][x][y] = (short)var10;
                    int mask = var3.readUnsignedByte();
                    this.overlayShape[z][x][y] = (byte)(mask >> 2);
                    this.overlayRotation[z][x][y] = (byte)(mask & 3);
                }
            }
        }
        if (hasDecor) {
            for (int z = 0; z < maxZ; ++z) {
                int decorLength = var3.readUnsignedByte();
                if (decorLength != 0) {
                    WorldMapDecoration[] decor = this.decorations[z][x][y] = new WorldMapDecoration[decorLength];
                    for(int index = 0; index < decorLength; ++index) {
                        int objectId = var3.readBigSmart();
                        int mask = var3.readUnsignedByte();
                        decor[index] = new WorldMapDecoration(objectId, mask >> 2, mask & 3);
                    }
                }
            }
        }

    }

    public void decodeFloorBasic(InputStream buffer, int x, int y, int mask) {
        boolean hasOverlay = (mask & 0x2) != 0;
        if (hasOverlay) {
            this.overlays[0][x][y] = buffer.readUnsignedByte();
        }
        this.underlays[0][x][y] = buffer.readUnsignedByte();
    }

    //method3300
    void reset() {
        this.underlays = null;
        this.overlays = null;
        this.overlayShape = null;
        this.overlayRotation = null;
        this.decorations = null;
        this.field1908 = false;
        this.field1909 = false;
    }

    public boolean method3298() {
        return this.field1908 && this.field1909;
    }
}
