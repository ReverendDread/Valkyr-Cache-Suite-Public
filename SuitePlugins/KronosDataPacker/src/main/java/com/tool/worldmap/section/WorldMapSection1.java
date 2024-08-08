package com.tool.worldmap.section;

import com.tool.worldmap.WorldMapArea;
import com.util.Position;
import store.io.impl.InputStream;

/**
 * @author ReverendDread on 7/8/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
public class WorldMapSection1 implements WorldMapSection {

    private int minPlane, planes;
    private int regionStartX, regionStartY;
    private int regionEndX, regionEndY;

    @Override
    public void decode(InputStream buffer) {
        this.minPlane = buffer.readUnsignedByte();
        this.planes = buffer.readUnsignedByte();
        this.regionStartX = buffer.readUnsignedShort();
        this.regionStartY = buffer.readUnsignedShort();
        this.regionEndX = buffer.readUnsignedShort();
        this.regionEndY = buffer.readUnsignedShort();
    }

    @Override
    public boolean vmethod5843(int var1, int var2, int var3) {
        return (var1 >= this.minPlane && var1 < this.planes + this.minPlane) && (var2 >> 6 == this.regionStartX && var3 >> 6 == this.regionStartY);
    }

    @Override
    public Position vmethod5846(int var1, int var2) {
        if(!this.vmethod5844(var1, var2)) {
            return null;
        } else {
            int var3 = this.regionStartX * 64 - this.regionEndX * 64 + var1;
            int var4 = this.regionStartY * 64 - this.regionEndY * 64 + var2;
            return new Position(var3, var4, this.minPlane);
        }
    }

    @Override
    public int[] vmethod5845(int var1, int var2, int var3) {
        if(!this.vmethod5843(var1, var2, var3)) {
            return null;
        } else {
            int[] var4 = new int[]{this.regionEndX * 64 - this.regionStartX * 64 + var2, var3 + (this.regionEndY * 64 - this.regionStartY * 64)};
            return var4;
        }
    }

    @Override
    public boolean vmethod5844(int var1, int var2) {
        return var1 >> 6 == this.regionEndX && var2 >> 6 == this.regionEndY;
    }

    @Override
    public void adjustBounds(WorldMapArea area) {
        if (area.regionLowX > this.regionEndX) {
            area.regionLowX = this.regionEndX;
        }
        if (area.regionHighX < this.regionEndX) {
            area.regionHighX = this.regionEndX;
        }
        if (area.regionLowY > this.regionEndY) {
            area.regionLowY = this.regionEndY;
        }
        if (area.regionHighY < this.regionEndY) {
            area.regionHighY = this.regionEndY;
        }
    }

}
