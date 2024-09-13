package dev.dread.map.cfg

import store.io.impl.InputStream
import store.io.impl.OutputStream
import store.plugin.extension.ConfigExtensionBase
import java.lang.reflect.Field

/**
 *
 * @project ValkyrCacheSuite
 * @author ReverendDread on 3/22/2020
 * https://www.rune-server.ee/members/reverenddread/
 */
class OverlayConfig : ConfigExtensionBase() {

    var color = -1
    var textureId = -1
    var hideUnderlay = false
    var blendColor = -1
    var textureTransparency = -1
    var aBoolean4061 = true
    var aBoolean4063 = false
    var someRGBValue = -1

    override fun decode(opcode: Int, buffer: InputStream) {
        when(opcode) {
            1 -> color = buffer.read24BitInt()
            2 -> textureId = buffer.readUnsignedShort()
            3 -> {
                var id = buffer.readUnsignedShort()
                if (id == 0xffff)
                    id = -1
                textureId = id
            }
            5 -> hideUnderlay = false
            7 -> buffer.read24BitInt()
            9 -> buffer.readUnsignedShort()
            10 -> aBoolean4061 = false
            11 -> buffer.readUnsignedByte()
            12 -> aBoolean4063 = true
            13 -> buffer.read24BitInt()
            14 -> buffer.readUnsignedByte()
            16 -> buffer.readUnsignedByte()
            20 -> buffer.readUnsignedShort()
            21 -> buffer.readUnsignedByte()
            22 -> buffer.readUnsignedShort()
        }
    }

    override fun encode(buffer: OutputStream?): OutputStream {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPriority(): MutableMap<Field, Int>? {
        return null
    }

    override fun toString(): String { return id.toString() }


}