package dev.dread.map.packer

import dev.dread.map.crypto.XTEAProvider
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import misc.CustomTab
import store.CacheLibrary
import store.cache.index.OSRSIndices
import store.plugin.extension.FXController
import store.utilities.GZIPCompressor
import suite.Main
import utility.ConfigEditorInfo
import utility.FilterMode
import utility.MultiMapEncoder
import utility.RetentionFileChooser
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.security.SecureRandom
import kotlin.math.floor

class MapPacker : FXController() {

    private val random = SecureRandom()

    private lateinit var tab: CustomTab
    private lateinit var xteaProvider: XTEAProvider

    override fun initialize(tab: CustomTab, refresh: Boolean, lastId: Int) {
        this.tab = tab
        regionType.selectedToggleProperty().addListener { observable, oldValue, newValue ->
            regionIdBox.isDisable = newValue != regionIdRadio;
            regionXYBox.isDisable = newValue != regionXYRadio;
            worldXYBox.isDisable = newValue != worldRadio;
        }
        loadType.selectedToggleProperty().addListener { observable, oldValue, newValue ->
            folderFileBox.isDisable = newValue != folderRadio;
            singleFileBox.isDisable = newValue == folderRadio;
        }

        saveButton()
        setupBrowseFolders()
    }

    private fun saveButton() {
        saveBtn.setOnAction { evt ->
            if (!::xteaProvider.isInitialized) {
                val file = RetentionFileChooser.showOpenDialog(
                    "Choose your xteas file",
                    Main.getSelection().stage.scene.window,
                    FilterMode.JSON
                )
                if (file != null && file.exists()) {
                    xteaProvider = XTEAProvider(file.toPath())
                } else {
                    Alert(AlertType.ERROR, "You must select a valid xteas file.", ButtonType.OK).show()
                    return@setOnAction
                }
            }

            val mapIndex = CacheLibrary.get().getIndex(OSRSIndices.MAPS)

            val regionX: Int
            val regionY: Int
            val regionId = regionID.text.toInt()
            if (regionType.selectedToggle === regionIdRadio) {
                regionX = regionId shr 8
                regionY = regionId and 0xFF
            } else if (regionType.selectedToggle === regionXYRadio) {
                regionX = regionXText.text.toInt()
                regionY = regionYText.text.toInt()
            } else {
                regionX = floor(worldXText.text.toInt() / 64.0).toInt()
                regionY = floor(worldYText.text.toInt() / 64.0).toInt()
            }

            // map of archive id, to xteas, we need these to update the cache
            val packingKeys = mutableMapOf<Int, IntArray>()

            // .pack file
            if (loadType.selectedToggle == folderRadio) {
                val text = mapFolderText.text

                // make sure the user has selected a file
                if (text.isEmpty()) {
                    Alert(AlertType.ERROR, "Please select a .pack file.", ButtonType.OK).show()
                    return@setOnAction
                }

                // make sure the file exists, and is a .pack file
                val file = File(text)
                if (!text.endsWith(".pack")) {
                    Alert(AlertType.ERROR, "You must select a .pack file", ButtonType.OK).show()
                    return@setOnAction
                }

                // make sure the file exists
                if (!file.exists()) {
                    Alert(AlertType.ERROR, "The file you selected does not exist.", ButtonType.OK).show()
                    return@setOnAction
                }

                try {
                    val chunks = MultiMapEncoder.decode(Files.readAllBytes(file.toPath()))
                    for (chunk in chunks) {
                        val newRegionX = (regionX + chunk.offsetX)
                        val newRegionY = (regionY + chunk.offsetY)
                        val regionId = newRegionX shl 8 or newRegionY
                        val newXtea = genXTEAKeys()
                        val archiveId = pack(newRegionX, newRegionY, chunk.tileMapData, chunk.objectMapData)
                        println("Packed region $regionId, archive id: $archiveId, xteas: ${newXtea.contentToString()}")
                        packingKeys[archiveId] = newXtea
                        xteaProvider.put(regionId, newXtea)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Alert(AlertType.ERROR, "An error occurred while packing the map.", ButtonType.OK).show()
                    return@setOnAction
                }
            } else { // .dat/.gz file
                val lsText = lsText.text
                val objText = objText.text
                // make sure both files are selected
                if (lsText.isEmpty() || objText.isEmpty()) {
                    Alert(AlertType.ERROR, "Please select both the .ls and .obj files.", ButtonType.OK).show()
                    return@setOnAction
                }
                // make sure both files are .gz or .dat
                if (!(lsText.endsWith(".dat") || lsText.endsWith(".gz")) || !(objText.endsWith(".dat") || objText.endsWith(".gz"))) {
                    Alert(AlertType.ERROR, "The .ls file must be a .dat or .gz file.", ButtonType.OK).show()
                    return@setOnAction
                }
                try {
                    val tiles = Files.readAllBytes(Paths.get(lsText)).run(GZIPCompressor::inflate317)
                    val locs = Files.readAllBytes(Paths.get(objText)).run(GZIPCompressor::inflate317)
                    val xtea = genXTEAKeys()
                    val archiveId = pack(regionX, regionY, tiles, locs)
                    packingKeys[archiveId] = xtea
                    xteaProvider.put(regionId, xtea)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Alert(AlertType.ERROR, "An error occurred while packing the map.", ButtonType.OK).show()
                    return@setOnAction
                }
            }

            if (mapIndex.update(packingKeys)) {
                // write the new xtea keys to the xteas file
                xteaProvider.save()
                Alert(AlertType.INFORMATION, "Map packed successfully.", ButtonType.OK).show()
            } else {
                Alert(AlertType.ERROR, "An error occurred while updating the cache.", ButtonType.OK).show()
            }
        }
    }

    private fun genXTEAKeys(): IntArray = random.ints(4).toArray()

    /**
     * Packs the map data into the cache.
     * @param regionX The region x coordinate.
     * @param regionY The region y coordinate.
     * @param tiles The tile data.
     * @param locs The location data.
     * @param xteas The xteas.
     * @return The archive id of the loc archive.
     */
    private fun pack(regionX: Int, regionY: Int, tiles: ByteArray, locs: ByteArray): Int {
        val index = CacheLibrary.get().getIndex(OSRSIndices.MAPS)

        val mapName = "m${regionX}_${regionY}"
        val mapArchive = index.addArchive(mapName, true)
        mapArchive.addFile(0, tiles)

        val locName = "l${regionX}_${regionY}"
        val locArchive = index.addArchive(locName, true)
        locArchive.addFile(0, locs)

        return locArchive.id
    }

    private fun setupBrowseFolders() {
        mapFolderBrowse.setOnAction { evt ->
            val file = RetentionFileChooser.showOpenDialog(
                Main.getSelection().stage.scene.window,
                FilterMode.PACK
            )
            if (file != null) {
                require(file.exists()) { "file doesn't exist! "}
                mapFolderText.text = file.absoluteFile.toString()
            }
        }
        lsBrowse.setOnAction { evt ->
            val file = RetentionFileChooser.showOpenDialog(
                Main.getSelection().stage.scene.window,
                FilterMode.DAT,
                FilterMode.GZIP
            )
            if (file != null) {
                lsText.text = file.absoluteFile.toString()
            }
        }
        objBrowse.setOnAction { evt ->
            val file = RetentionFileChooser.showOpenDialog(
                Main.getSelection().stage.scene.window,
                FilterMode.DAT,
                FilterMode.GZIP
            )
            if (file != null) {
                objText.text = file.absoluteFile.toString()
            }
        }
    }

    override fun save() = Unit

    override fun getInfo(): ConfigEditorInfo? = null

    @FXML
    private lateinit var folderRadio: RadioButton

    @FXML
    private lateinit var loadType: ToggleGroup

    @FXML
    private lateinit var folderFileBox: VBox

    @FXML
    private lateinit var mapFolderText: TextField

    @FXML
    private lateinit var mapFolderBrowse: Button

    @FXML
    private lateinit var singleFileRadio: RadioButton

    @FXML
    private lateinit var singleFileBox: VBox

    @FXML
    private lateinit var lsText: TextField

    @FXML
    private lateinit var lsBrowse: Button

    @FXML
    private lateinit var objText: TextField

    @FXML
    private lateinit var objBrowse: Button

    @FXML
    private lateinit var regionIdRadio: RadioButton

    @FXML
    private lateinit var regionType: ToggleGroup

    @FXML
    private lateinit var regionIdBox: HBox

    @FXML
    private lateinit var regionID: TextField

    @FXML
    private lateinit var regionXYRadio: RadioButton

    @FXML
    private lateinit var regionXYBox: HBox

    @FXML
    private lateinit var regionXText: TextField

    @FXML
    private lateinit var regionYText: TextField

    @FXML
    private lateinit var worldRadio: RadioButton

    @FXML
    private lateinit var worldXYBox: HBox

    @FXML
    private lateinit var worldXText: TextField

    @FXML
    private lateinit var worldYText: TextField

    @FXML
    private lateinit var xteaText: TextField

    @FXML
    private lateinit var genXteas: Button

    @FXML
    private lateinit var saveBtn: Button

    @FXML
    private lateinit var canvas: Canvas

    @FXML
    private lateinit var worldAnchor: AnchorPane

    @FXML
    private lateinit var regionIdField: TextField

    @FXML
    private lateinit var regionIdSubmit: Button

}