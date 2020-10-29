package com.tool.worldmap.section;

import com.tool.worldmap.WorldMapArea;
import com.util.Position;
import store.io.impl.InputStream;

/**
 * @author ReverendDread on 7/8/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
public class WorldMapSection0 implements WorldMapSection {

    private int newX, newY, newZ;
    private int oldX, oldY, oldZ;
    private int oldChunkXLow, oldChunkXHigh;
    private int oldChunkYLow, oldChunkYHigh;
    private int newChunkXLow, newChunkXHigh;
    private int newChunkYLow, newChunkYHigh;

    @Override
    public void decode(InputStream buffer) {
        this.oldZ = buffer.readUnsignedByte();
        this.newZ = buffer.readUnsignedByte();
        this.oldX = buffer.readUnsignedShort();
        this.oldChunkXLow = buffer.readUnsignedByte();
        this.oldChunkXHigh = buffer.readUnsignedByte();
        this.oldY = buffer.readUnsignedShort();
        this.oldChunkYLow = buffer.readUnsignedByte();
        this.oldChunkYHigh = buffer.readUnsignedByte();
        this.newX = buffer.readUnsignedShort();
        this.newChunkXLow = buffer.readUnsignedByte();
        this.newChunkXHigh = buffer.readUnsignedByte();
        this.newY = buffer.readUnsignedShort();
        this.newChunkYLow = buffer.readUnsignedByte();
        this.newChunkYHigh = buffer.readUnsignedByte();
    }

    @Override
    public boolean vmethod5843(int var1, int var2, int var3) {
        return (var1 >= this.oldZ && var1 < this.oldZ + this.newZ) && (var2 >= (this.oldX << 6) + (this.oldChunkXLow << 3) && var2 <= (this.oldX << 6) + (this.oldChunkXHigh << 3) + 7 && var3 >= (this.oldY << 6) + (this.oldChunkYLow << 3) && var3 <= (this.oldY << 6) + (this.oldChunkYHigh << 3) + 7);
    }

    @Override
    public Position vmethod5846(int var1, int var2) {
        if(!this.vmethod5844(var1, var2)) {
            return null;
        } else {
            int var3 = this.oldX * 64 - this.newX * 64 + (this.oldChunkXLow * 8 - this.newChunkXLow * 8) + var1;
            int var4 = this.oldY * 64 - this.newY * 64 + var2 + (this.oldChunkYLow * 8 - this.newChunkYLow * 8);
            return new Position(var3, var4, this.oldZ);
        }
    }

    @Override
    public int[] vmethod5845(int var1, int var2, int var3) {
        if(!this.vmethod5843(var1, var2, var3)) {
            return null;
        } else {
            int[] var4 = new int[]{this.newX * 64 - this.oldX * 64 + var2 + (this.newChunkXLow * 8 - this.oldChunkXLow * 8), var3 + (this.newY * 64 - this.oldY * 64) + (this.newChunkYLow * 8 - this.oldChunkYLow * 8)};
            return var4;
        }
    }

    @Override
    public boolean vmethod5844(int x, int y) {
        return x >= (this.newX << 6) + (this.newChunkXLow << 3) && x <= (this.newX << 6) + (this.newChunkXHigh << 3) + 7 && y >= (this.newY << 6) + (this.newChunkYLow << 3) && y <= (this.newY << 6) + (this.newChunkYHigh << 3) + 7;
    }

    @Override
    public void adjustBounds(WorldMapArea area) {
        if (area.regionLowX > this.newX) {
            area.regionLowX = this.newX;
        }
        if (area.regionHighX < this.newX) {
            area.regionHighX = this.newX;
        }
        if (area.regionLowY > this.newY) {
            area.regionLowY = this.newY;
        }
        if (area.regionHighY < this.newY) {
            area.regionHighY = this.newY;
        }
    }

}
