package com.tool.worldmap;

import utility.StringUtilities;

/**
 * @author ReverendDread on 7/8/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
public enum WorldMapArchiveName {

    DETAILS,
    COMPOSITE_MAP,
    COMPOSITE_TEXTURE,
    AREA,
    LABELS;

    public String format() {
        return StringUtilities.getFormattedEnumName(name()).replace(" ", "");
    }

}
