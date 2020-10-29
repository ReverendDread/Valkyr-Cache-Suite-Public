package com.tool.worldmap.decor;

import com.util.Enumerated;
import lombok.RequiredArgsConstructor;

/**
 * @author ReverendDread on 7/8/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
@RequiredArgsConstructor
public enum WorldMapDecorationType implements Enumerated {

    TYPE_0(0),
    TYPE_1(1),
    TYPE_2(2),
    TYPE_3(3),
    TYPE_4(4),
    TYPE_5(5),
    TYPE_6(6),
    TYPE_7(7),
    TYPE_8(8),
    TYPE_9(9),
    TYPE_10(10),
    TYPE_11(11),
    TYPE_12(12),
    TYPE_13(13),
    TYPE_14(14),
    TYPE_15(15),
    TYPE_16(16),
    TYPE_17(17),
    TYPE_18(18),
    TYPE_19(19),
    TYPE_20(20),
    TYPE_21(21),
    TYPE_22(22)

    ;

    private final int id;

    @Override
    public int getId() {
        return id;
    }

}
