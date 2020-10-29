package com.tool.worldmap;

import com.tool.worldmap.data.AbstractWorldMapSector;
import com.tool.worldmap.data.WorldMapChunkSector;
import com.tool.worldmap.data.WorldMapRegionSector;
import com.tool.worldmap.icon.WorldMapIcon0;
import com.tool.worldmap.label.WorldMapLabel;
import com.tool.worldmap.label.WorldMapLabelSize;
import com.util.Sprite;
import lombok.Getter;
import store.cache.index.Index;

import java.util.*;

/**
 * @author ReverendDread on 7/8/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
@Getter
public class WorldMapRegion {

    private final int regionX, regionY;
    private LinkedList<WorldMapChunkSector> chunks;
    private LinkedList<WorldMapIcon0> icon0List;
    private WorldMapRegionSector region;
    private LinkedList iconMap;
    private final int backgroundColor;
    private int pixelsPerTile;
    private final HashMap fonts;

    public WorldMapRegion(int regionX, int regionY, int backgroundColor, HashMap fonts) {
        this.regionX = regionX;
        this.regionY = regionY;
        this.chunks = new LinkedList<>();
        this.icon0List = new LinkedList<>();
        this.iconMap = new LinkedList<>();
        this.backgroundColor = backgroundColor | -16777216;
        this.fonts = fonts;
    }

    public void setRegionSector(WorldMapRegionSector var1, List var2) {
        this.iconMap.clear();
        this.region = var1;
        createIcons(var2);
    }

    void createIcons(List var1) {
        Iterator var2 = var1.iterator();
        while (var2.hasNext()) {
            WorldMapIcon0 var3 = (WorldMapIcon0) var2.next();
            if (var3.mapPosition.getX() >> 6 == this.regionX && var3.mapPosition.getY() >> 6 == this.regionY) {
                WorldMapIcon0 var4 = new WorldMapIcon0(var3.mapPosition, var3.mapPosition, var3.element, createElement(var3.element));
                this.icon0List.add(var4);
            }
        }
    }

    WorldMapLabel createElement(int objectId) {
        WorldMapElement var2 = new WorldMapElement(objectId);
        return this.createLabel(var2);
    }

    WorldMapLabel createLabel(WorldMapElement element) {
        if (element.name != null && this.fonts != null && this.fonts.get(WorldMapLabelSize.SMALL) != null) {

            int var3 = element.textSize;
            WorldMapLabelSize[] size = WorldMapLabelSize.values();
            int index = 0;

            WorldMapLabelSize var2;
            while (true) {
                if (index >= size.length) {
                    var2 = null;
                    break;
                }
                WorldMapLabelSize var6 = size[index];
                if (var3 == var6.getField2046()) {
                    var2 = var6;
                    break;
                }
                ++index;
            }

            if(var2 == null) {
                return null;
            } else {
//                Font var14 = (Font) this.fonts.get(var2);
//                if (var14 == null) {
//                    return null;
//                } else {
//                    int var15 = var14.method5327(var1.name, 1000000);
//                    String[] var7 = new String[var15];
//                    var14.method5325(var1.name, (int[])null, var7);
//                    int var8 = var7.length * var14.ascent / 2;
//                    int var9 = 0;
//                    String[] var10 = var7;
//
//                    for(int var11 = 0; var11 < var10.length; ++var11) {
//                        String var12 = var10[var11];
//                        int var13 = var14.method5324(var12);
//                        if(var13 > var9) {
//                            var9 = var13;
//                        }
//                    }
//
//                    return new WorldMapLabel(var1.name, var9, var8, var2);
//                }
            }
        }
        return null;
    }

    boolean method226(Index var1) {
        this.iconMap.clear();
        if(this.region != null) {
            this.region.method3299(var1);
            if(this.region.method3298()) {
                //this.method221(0, 0, 64, 64, this.region);
                return true;
            } else {
                return false;
            }
        } else {
            boolean var2 = true;
            Iterator var3;
            WorldMapChunkSector var4;
            for (var3 = this.chunks.iterator(); var3.hasNext(); var2 &= var4.method3298()) {
                var4 = (WorldMapChunkSector) var3.next();
                var4.method3299(var1);
            }
            if (var2) {
                var3 = this.chunks.iterator();
                while(var3.hasNext()) {
                    var4 = (WorldMapChunkSector) var3.next();
                    //this.method221(var4.method512() * 8, var4.method518() * 8, 8, 8, var4);
                }
            }
            return var2;
        }
    }

//    void method221(int var1, int var2, int var3, int var4, AbstractWorldMapData var5) {
//        for(int var6 = var1; var6 < var3 + var1; ++var6) {
//            label73:
//            for(int var7 = var2; var7 < var2 + var4; ++var7) {
//                for(int var8 = 0; var8 < var5.planes; ++var8) {
//                    WorldMapDecoration[] var9 = var5.decorations[var8][var6][var7];
//                    if(var9 != null && var9.length != 0) {
//                        WorldMapDecoration[] var10 = var9;
//
//                        for(int var11 = 0; var11 < var10.length; ++var11) {
//                            ObjectDefinition var13;
//                            boolean var14;
//                            label64: {
//                                WorldMapDecoration var12 = var10[var11];
//                                var13 = GrandExchangeOfferOwnWorldComparator.getObjectDefinition(var12.objectDefinitionId);
//                                if(var13.transforms != null) {
//                                    int[] var15 = var13.transforms;
//
//                                    for(int var16 = 0; var16 < var15.length; ++var16) {
//                                        int var17 = var15[var16];
//                                        ObjectDefinition var18 = GrandExchangeOfferOwnWorldComparator.getObjectDefinition(var17);
//                                        if(var18.mapIconId != -1) {
//                                            var14 = true;
//                                            break label64;
//                                        }
//                                    }
//                                } else if(var13.mapIconId != -1) {
//                                    var14 = true;
//                                    break label64;
//                                }
//
//                                var14 = false;
//                            }
//
//                            if(var14) {
//                                this.method222(var13, var8, var6, var7, var5);
//                                continue label73;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//    }

    public void decode(int resolution, Index var4, Index var5) {
        this.pixelsPerTile = resolution;
        if(this.region != null || !this.chunks.isEmpty()) {
            int x = this.regionX;
            int y = this.regionY;
            Sprite var6 = null;
            if(var6 == null) {
                boolean var9 = true;
                var9 &= this.method226(var4);
                int archive;
                if (this.region != null) {
                    archive = this.region.getArchiveId();
                } else {
                    archive = ((AbstractWorldMapSector) this.chunks.getFirst()).getArchiveId();
                }
                var9 &= var5.archiveExists(archive);
                if (var9) { //TODO map sprites
//                    byte[] var10 = var5.method4027(var11);
//                    WorldMapSprite var12;
//                    if(var10 == null) {
//                        var12 = new WorldMapSprite();
//                    } else {
//                        var12 = new WorldMapSprite(class28.method577(var10).pixels);
//                    }
//
//                    Sprite var14 = new Sprite(this.pixelsPerTile * 64, this.pixelsPerTile * 64);
//                    var14.method6185();
//                    if(this.worldMapData_0 != null) {
//                        this.method320(var2, var3, var12);
//                    } else {
//                        this.method231(var2, var3, var12);
//                    }
//
//                    int var15 = this.regionX;
//                    int var16 = this.regionY;
//                    int var17 = this.pixelsPerTile;
//                    WorldMapRegion_cachedSprites.method3104(var14, SecureRandomCallable.method1135(var15, var16, var17), var14.pixels.length * 4);
//                    this.method225();
                }
            }
        }
    }

    public void addChunks(HashSet<WorldMapChunkSector> chunks, LinkedList<WorldMapIcon0> icons) {
        this.iconMap.clear();
        Iterator iterator = chunks.iterator();
        while (iterator.hasNext()) {
            WorldMapChunkSector chunk = (WorldMapChunkSector) iterator.next();
            if (chunk.getRegionX() == this.regionX && chunk.getRegionY() == this.regionY) {
                this.chunks.add(chunk);
            }
        }
        createIcons(icons);
    }
}
