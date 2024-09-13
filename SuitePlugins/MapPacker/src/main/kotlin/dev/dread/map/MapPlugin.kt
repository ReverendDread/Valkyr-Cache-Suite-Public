package dev.dread.map

import dev.dread.map.loader.OverlayLoader
import dev.dread.map.loader.UnderlayLoader
import dev.dread.map.packer.MapPacker
import store.plugin.Plugin
import store.plugin.PluginType
import suite.annotation.PluginDescriptor

@PluginDescriptor(author = "ReverendDread", description = "Pack's maps into the cache.", version = "183", type = PluginType.MAP)
class MapPlugin : Plugin() {

    companion object {
        internal lateinit var underlays: UnderlayLoader
        internal lateinit var overlays: OverlayLoader
    }

    override fun load(): Boolean {
        controller = MapPacker()
        underlays = UnderlayLoader().load()
        overlays = OverlayLoader().load()
        return true
    }

    override fun getFXML(): String = "main.fxml"

}