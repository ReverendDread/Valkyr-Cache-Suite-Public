package dev.dread.map.cfg

import store.CacheLibrary
import store.io.impl.InputStream
import store.io.impl.OutputStream

/**
 *
 * @project ValkyrCacheSuite
 * @author ReverendDread on 3/22/2020
 * https://www.rune-server.ee/members/reverenddread/
 */
class UnderlayConfig  {

    var id = -1
    var color = -1
    var texture = -1
    var anInt5597 = -1
    var aBoolean5599 = true
    var aBoolean5596 = true
    var hue = 0
    var saturation = 0
    var lightness = 0
    var hueMultiplier = 0

    fun decode(opcode: Int, buffer: InputStream) {
        when(opcode){
            1 -> {
                color = buffer.read24BitInt()
                calculateHsl(color)
            }
            2 -> texture = buffer.readUnsignedShort()
            3 -> anInt5597 = (buffer.readUnsignedShort() shl 2)
            4 -> aBoolean5599 = false
            5 -> aBoolean5596 = false
        }
    }

    fun encode(buffer: OutputStream?): OutputStream? {
        buffer?.apply {
            buffer.writeByte(1)
            buffer.write24BitInt(color)
            if (!CacheLibrary.get().isOSRS) {
                buffer.writeByte(2)
                buffer.writeShort(texture)
                buffer.writeByte(3)
                buffer.writeShort(anInt5597 shr 2)
                if (aBoolean5599) buffer.writeByte(4)
                if (aBoolean5596) buffer.writeByte(5)
            }
        }
        return buffer
    }

    fun calculateHsl(rgb: Int) {
        val var2 = (rgb shr 16 and 255).toDouble() / 256.0
        val var4 = (rgb shr 8 and 255).toDouble() / 256.0
        val var6 = (rgb and 255).toDouble() / 256.0
        var var8 = var2
        if (var4 < var2) {
            var8 = var4
        }
        if (var6 < var8) {
            var8 = var6
        }
        var var10 = var2
        if (var4 > var2) {
            var10 = var4
        }
        if (var6 > var10) {
            var10 = var6
        }
        var var12 = 0.0
        var var14 = 0.0
        val var16 = (var10 + var8) / 2.0
        if (var8 != var10) {
            if (var16 < 0.5) {
                var14 = (var10 - var8) / (var8 + var10)
            }
            if (var16 >= 0.5) {
                var14 = (var10 - var8) / (2.0 - var10 - var8)
            }
            if (var2 == var10) {
                var12 = (var4 - var6) / (var10 - var8)
            } else if (var10 == var4) {
                var12 = 2.0 + (var6 - var2) / (var10 - var8)
            } else if (var10 == var6) {
                var12 = 4.0 + (var2 - var4) / (var10 - var8)
            }
        }
        var12 /= 6.0
        saturation = ((var14 * 256.0).toInt())
        lightness = ((var16 * 256.0).toInt())
        if (saturation < 0) {
            saturation = 0
        } else if (saturation > 255) {
            saturation = 255
        }
        if (lightness < 0) {
            lightness = 0
        } else if (lightness > 255) {
            lightness = 255
        }
        hueMultiplier = if (var16 > 0.5) {
            (var14 * (1.0 - var16) * 512.0).toInt()
        } else {
            (var14 * var16 * 512.0).toInt()
        }
        if (hueMultiplier < 1) {
            hueMultiplier = 1
        }
        hue = ((hueMultiplier * var12).toInt())
    }

    override fun toString(): String {
        return "UnderlayConfing[id = $id, color = $color]"
    }

}