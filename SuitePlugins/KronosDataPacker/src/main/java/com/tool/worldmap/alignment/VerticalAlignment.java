package com.tool.worldmap.alignment;

import com.util.Enumerated;

/**
 * @author ReverendDread on 7/8/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
public enum VerticalAlignment implements Enumerated {

    TOP(0, 0), CENTER(2, 1), BOTTOM(1, 2);

    private final int value, id;

    VerticalAlignment(int value, int id) {
        this.value = value;
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    public static VerticalAlignment[] alignments() {
        return new VerticalAlignment[] { BOTTOM, TOP, CENTER };
    }

}
