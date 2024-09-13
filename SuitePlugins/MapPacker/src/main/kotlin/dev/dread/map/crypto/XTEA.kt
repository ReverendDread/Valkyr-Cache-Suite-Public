package dev.dread.map.crypto

import com.fasterxml.jackson.annotation.JsonAlias

/**
 * Represents an XTEA key pair.
 * @param mapsquare The region id.
 * @param key The xtea keys.
 */
data class XTEA(
    @JsonAlias("region", "regionId")
    val mapsquare: Int,
    @JsonAlias("keys")
    val key: IntArray
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as XTEA

        if (mapsquare != other.mapsquare) return false
        if (!key.contentEquals(other.key)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = mapsquare
        result = 31 * result + key.contentHashCode()
        return result
    }

}