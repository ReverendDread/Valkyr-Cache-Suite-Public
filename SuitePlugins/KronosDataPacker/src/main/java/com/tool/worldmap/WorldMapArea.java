package com.tool.worldmap;

import com.google.common.collect.Lists;
import com.tool.worldmap.section.*;
import com.util.Misc;
import com.util.Position;
import lombok.Getter;
import store.io.impl.InputStream;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author ReverendDread on 7/8/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
@Getter
public class WorldMapArea {

    public int id;
    public int backgroundColor;
    public int zoom;
    public int regionLowX, regionHighX;
    public int regionLowY, regionHighY;
    public boolean isMain;

    public Position origin;
    public String internalName, externalName;

    public LinkedList<WorldMapSection> sections;

    public void decodeDetails(InputStream buffer, int id) {
        this.id = id;
        this.internalName = buffer.readString();
        this.externalName = buffer.readString();
        this.origin = new Position(buffer.readInt());
        this.backgroundColor = buffer.readInt();
        buffer.readUnsignedByte();
        this.isMain = buffer.readBoolean();
        this.zoom = buffer.readUnsignedByte();
        int sectionSize = buffer.readUnsignedByte();
        this.sections = Lists.newLinkedList();
        for (int index = 0; index < sectionSize; index++) {
            this.sections.add(decodeSection(buffer));
        }
        postDecode();
    }

    private void postDecode() {
        Iterator iterator = sections.iterator();
        while (iterator.hasNext()) {
            WorldMapSection section = ((WorldMapSection) iterator.next());
            section.adjustBounds(this);
        }
    }

    WorldMapSection decodeSection(InputStream buffer) {
        int id = buffer.readUnsignedByte();
        WorldMapSectionType[] types = new WorldMapSectionType[] {
                WorldMapSectionType.WORLD_MAP_SECTION_TYPE_0,
                WorldMapSectionType.WORLD_MAP_SECTION_TYPE_2,
                WorldMapSectionType.WORLD_MAP_SECTION_TYPE_3,
                WorldMapSectionType.WORLD_MAP_SECTION_TYPE_1
        };
        WorldMapSectionType type = (WorldMapSectionType) Misc.getEnumeratedTypeIndex(types, id);
        Object section = null;
        switch (type.getType()) {
            case 0:
                section = new WorldMapSection2();
                break;
            case 1:
                section = new WorldMapSection3();
                break;
            case 2:
                section = new WorldMapSection0();
                break;
            case 3:
                section = new WorldMapSection1();
                break;
            default:
                throw new IllegalStateException("Invalid section type");
        }
        ((WorldMapSection) section).decode(buffer);
        return ((WorldMapSection) section);
    }

    public boolean validPosition(int x, int y) {
        int regionX = x / 64;
        int regionY = y / 64;
        if (regionX >= regionLowX && regionX <= regionHighX) {
            if (regionY >= regionLowY && regionY <= regionHighY) {
                Iterator iterator = sections.iterator();
                WorldMapSection section;
                do {
                    if (!iterator.hasNext()) {
                        return false;
                    }
                    section = ((WorldMapSection) iterator.next());
                } while (!section.vmethod5844(x, y));
                return true;
            }
        }
        return false;
    }

    public int[] getAreaCenter(int z, int x, int y) {
        Iterator iterator = this.sections.iterator();
        WorldMapSection section;
        do {
            if(!iterator.hasNext()) {
                return null;
            }
            section = (WorldMapSection)iterator.next();
        } while(!section.vmethod5843(z, x, y));
        return section.vmethod5845(z, x, y);
    }

}
