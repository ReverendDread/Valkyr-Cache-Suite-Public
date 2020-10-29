package com.tool.worldmap.alignment;

import com.util.Enumerated;

/**
 * @author ReverendDread on 7/8/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
public enum HorizontalAlignment implements Enumerated {

    LEFT(1, 0), CENTER(2, 1), RIGHT(0, 2);

    private final int value, id;

    HorizontalAlignment(int value, int id) {
        this.value = value;
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    public static HorizontalAlignment[] alignments() {
        return new HorizontalAlignment[] { RIGHT, LEFT, CENTER };
    }

}
