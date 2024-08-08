package com.tool.worldmap.label;

import com.util.Enumerated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author ReverendDread on 7/8/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
@RequiredArgsConstructor @Getter
public enum WorldMapLabelSize implements Enumerated {

    SMALL(1, 0, 4),
    MEDIUM(0, 1, 2),
    LARGE(2, 2, 0);

    private final int field2045, field2046, field2047;

    @Override
    public int getId() {
        return -1;
    }

}
