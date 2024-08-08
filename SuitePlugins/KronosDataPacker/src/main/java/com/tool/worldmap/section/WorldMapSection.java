package com.tool.worldmap.section;

import com.tool.worldmap.WorldMapArea;
import com.util.Position;
import store.io.impl.InputStream;

/**
 * @author ReverendDread on 7/8/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
public interface WorldMapSection {

    void decode(InputStream buffer);

    boolean vmethod5843(int var1, int var2, int var3);

    Position vmethod5846(int var1, int var2);

    int[] vmethod5845(int var1, int var2, int var3);

    boolean vmethod5844(int var1, int var2);

    void adjustBounds(WorldMapArea var1);

}
