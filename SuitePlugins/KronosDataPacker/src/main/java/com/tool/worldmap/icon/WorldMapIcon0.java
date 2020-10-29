package com.tool.worldmap.icon;

import com.tool.worldmap.WorldMapElement;
import com.tool.worldmap.label.WorldMapLabel;
import com.util.Position;

/**
 * @author ReverendDread on 7/8/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
public class WorldMapIcon0 extends AbstractWorldMapIcon {

    public int element;
    public int subWidth;
    public int subHeight;
    public WorldMapLabel label;

    public WorldMapIcon0(Position primary, Position secondary, int element, WorldMapLabel label) {
        super(primary, secondary);
        this.element = element;
        this.label = label;
        WorldMapElement wme = new WorldMapElement(this.element);
        //TODO set dimensions based on sprite
    }

}
