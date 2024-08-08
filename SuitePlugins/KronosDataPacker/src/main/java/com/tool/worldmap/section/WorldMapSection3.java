package com.tool.worldmap.section;

import com.tool.worldmap.WorldMapArea;
import com.util.Position;
import store.io.impl.InputStream;

/**
 * @author ReverendDread on 7/8/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
public class WorldMapSection3 implements WorldMapSection {

    private int field222;
    private int field223;
    private int field224;
    private int field228;
    private int field227;
    private int field229;
    private int field226;
    private int field230;
    private int field236;
    private int field225;

    @Override
    public void decode(InputStream buffer) {
        this.field222 = buffer.readUnsignedByte();
        this.field223 = buffer.readUnsignedByte();
        this.field224 = buffer.readUnsignedShort();
        this.field228 = buffer.readUnsignedByte();
        this.field227 = buffer.readUnsignedShort();
        this.field229 = buffer.readUnsignedByte();
        this.field226 = buffer.readUnsignedShort();
        this.field230 = buffer.readUnsignedByte();
        this.field236 = buffer.readUnsignedShort();
        this.field225 = buffer.readUnsignedByte();
    }

    @Override
    public boolean vmethod5843(int var1, int var2, int var3) {
        return (var1 >= this.field222 && var1 < this.field223 + this.field222) && (var2 >= (this.field224 << 6) + (this.field228 << 3) && var2 <= (this.field224 << 6) + (this.field228 << 3) + 7 && var3 >= (this.field227 << 6) + (this.field229 << 3) && var3 <= (this.field227 << 6) + (this.field229 << 3) + 7);
    }

    @Override
    public Position vmethod5846(int var1, int var2) {
        if(!this.vmethod5844(var1, var2)) {
            return null;
        } else {
            int var3 = this.field224 * 64 - this.field226 * 64 + (this.field228 * 8 - this.field230 * 8) + var1;
            int var4 = this.field227 * 64 - this.field236 * 64 + var2 + (this.field229 * 8 - this.field225 * 8);
            return new Position(var3, var4, this.field222);
        }
    }

    @Override
    public int[] vmethod5845(int var1, int var2, int var3) {
        if(!this.vmethod5843(var1, var2, var3)) {
            return null;
        } else {
            int[] var4 = new int[]{this.field226 * 64 - this.field224 * 64 + var2 + (this.field230 * 8 - this.field228 * 8), var3 + (this.field236 * 64 - this.field227 * 64) + (this.field225 * 8 - this.field229 * 8)};
            return var4;
        }
    }

    @Override
    public boolean vmethod5844(int var1, int var2) {
        return var1 >= (this.field226 << 6) + (this.field230 << 3) && var1 <= (this.field226 << 6) + (this.field230 << 3) + 7 && var2 >= (this.field236 << 6) + (this.field225 << 3) && var2 <= (this.field236 << 6) + (this.field225 << 3) + 7;
    }

    @Override
    public void adjustBounds(WorldMapArea area) {
        if (area.regionLowX > this.field226) {
            area.regionLowX = this.field226;
        }
        if (area.regionHighX < this.field226) {
            area.regionHighX = this.field226;
        }
        if (area.regionLowY > this.field236) {
            area.regionLowY = this.field236;
        }
        if (area.regionHighY < this.field236) {
            area.regionHighY = this.field236;
        }
    }

}
