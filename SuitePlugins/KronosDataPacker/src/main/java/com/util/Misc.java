package com.util;

/**
 * @author ReverendDread on 7/8/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
public class Misc {

    public static Enumerated getEnumeratedTypeIndex(Enumerated[] enumeratedToSearch, int id) {
        Enumerated[] holder = enumeratedToSearch;
        for (int index = 0; index < holder.length; index++) {
            Enumerated enumerated = holder[index];
            if (id == enumerated.getId()) {
                return enumerated;
            }
        }
        return null;
    }

}
