package com.tool.worldmap.area;

import com.tool.worldmap.WorldMapArea;
import com.tool.worldmap.data.WorldMapChunkSector;
import com.tool.worldmap.data.WorldMapRegionSector;
import com.tool.worldmap.icon.WorldMapIcon0;
import com.util.Position;
import store.io.impl.InputStream;

import java.util.HashSet;
import java.util.LinkedList;

/**
 * @author ReverendDread on 7/8/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
public class WorldMapAreaData extends WorldMapArea {

    public LinkedList<WorldMapIcon0> icons;
    public HashSet<WorldMapRegionSector> regional;
    public HashSet<WorldMapChunkSector> chunks;

    public void decode(InputStream details, InputStream composite, int id, boolean members) {
        this.decodeDetails(details, id);
        int length = composite.readUnsignedShort();
        this.regional = new HashSet<>(length);
        for (int index = 0; index < length; index++) {
            WorldMapRegionSector region = new WorldMapRegionSector();
            try {
                region.decodeRegional(composite);
            } catch (IllegalStateException ex) {
                continue;
            }
            this.regional.add(region);
        }
        length = composite.readUnsignedShort();
        this.chunks = new HashSet<>(length);
        for (int index = 0; index < length; index++) {
            WorldMapChunkSector chunk = new WorldMapChunkSector();
            try {
                chunk.decodeChunk(composite);
            } catch (IllegalStateException ex) {
                continue;
            }
            this.chunks.add(chunk);
        }
        decodeIcons(composite, members);
    }

    public void decodeIcons(InputStream buffer, boolean members) {
        this.icons = new LinkedList();
        int iconSize = buffer.readUnsignedShort();
        for (int index = 0; index < iconSize; index++) {
            int element = buffer.readBigSmart();
            Position position = new Position(buffer.readInt());
            boolean var7 = buffer.readBoolean();
            if (members || !var7) {
                this.icons.add(new WorldMapIcon0(null, position, element, null));
            }
        }
    }

}
