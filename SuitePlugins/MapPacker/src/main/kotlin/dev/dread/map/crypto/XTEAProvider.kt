package dev.dread.map.crypto

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.nio.file.Path
import kotlin.io.path.absolute

/**
 * A provider for XTEA keys.
 * @param path The path to the XTEA keys file.
 */
class XTEAProvider(private val path: Path) {

    private val keys = mutableMapOf<Int, IntArray>()
    private val mapper = jacksonObjectMapper()

    init {
        val json = path.toFile().readText()
        val xteas = mapper.readValue<Array<XTEA>>(json)
        xteas.forEach { keys[it.mapsquare] = it.key }
    }

    /**
     * Get the XTEA keys for the specified region.
     * @param region The region id.
     * @return The XTEA keys.
     */
    fun get(region: Int): IntArray? = keys[region]

    /**
     * Put the XTEA keys for the specified region.
     * @param region The region id.
     * @param keys The XTEA keys.
     */
    fun put(region: Int, keys: IntArray) {
        this.keys[region] = keys
    }

    /**
     * Save the XTEA keys to the file.
     */
    fun save() {
        val xteas = keys.map { XTEA(it.key, it.value) }
        val json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(xteas)
        println("saved xteas to: ${path.absolute()}")
        path.toFile().writeText(json)
    }

    companion object {

        /**
         * Default set of keys to use if none are found.
         */
        val DEFAULT_KEYS = intArrayOf(0, 0, 0, 0)

    }

}