package com.util;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ReverendDread on 7/8/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
@Getter @Setter
public class Position {

    private int x, y, z;

    public Position(int hash) {
        if (hash == -1) {
            this.z = -1;
        } else {
            this.x = hash >> 14 & 0x3fff;
            this.y = hash & 0x3fff;
            this.z = hash >> 28 & 0x3;
        }
    }

    public Position(int x, int y) {
        this(x, y, 0);
    }

    public Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
