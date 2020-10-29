package com.tool.worldmap.section;

import com.util.Enumerated;
import lombok.Getter;

/**
 * @author ReverendDread on 7/8/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
public enum WorldMapSectionType implements Enumerated {

    WORLD_MAP_SECTION_TYPE_0(0, 0),

    WORLD_MAP_SECTION_TYPE_1(3, 1),

    WORLD_MAP_SECTION_TYPE_2(2, 2),

    WORLD_MAP_SECTION_TYPE_3(1, 3);

    private final int type, id;

    private WorldMapSectionType(int type, int id) {
        this.type = type;
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }
}
