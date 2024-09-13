package dev.dread.map.loader

import dev.dread.map.cfg.OverlayConfig
import store.CacheLibrary
import store.io.impl.InputStream
import store.plugin.PluginType
import suite.annotation.LoaderDescriptor
import suite.controller.Selection
import java.util.*

/**
 *
 * @project ValkyrCacheSuite
 * @author ReverendDread on 3/22/2020
 * https://www.rune-server.ee/members/reverenddread/
 */
@LoaderDescriptor(author = "ReverendDread", version = "317/OSRS/742", type = PluginType.FLO)
class OverlayLoader {

    val overlays: HashMap<Int, OverlayConfig> = hashMapOf()

    fun load(): OverlayLoader {
        try {
            val index = CacheLibrary.get().getIndex(index)
            val files = index.getArchive(archive).fileIds
            for (id in files) {
                val file = index.getArchive(archive).getFile(id)
                file?.apply {
                    val definition = OverlayConfig()
                    val buffer = InputStream(file.data)
                    buffer.buffer?.apply {
                        while (true) {
                            val opcode = buffer.readUnsignedByte()
                            if (opcode == 0) break
                            definition.decode(opcode, buffer)
                        }
                    }
                    overlays[id] = definition
                    Selection.progressListener.pluginNotify("($id/${files.size})")
                }
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        return this
    }

    companion object {
        val index = 2;
        val archive = 4;
    }

}