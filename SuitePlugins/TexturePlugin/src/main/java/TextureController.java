import com.google.common.collect.Lists;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;
import misc.CustomTab;
import org.apache.commons.lang3.ArrayUtils;
import store.CacheLibrary;
import store.io.impl.OutputStream;
import store.plugin.PluginManager;
import store.plugin.PluginType;
import store.plugin.extension.FXController;
import suite.Main;
import suite.controller.Selection;
import suite.dialogue.Dialogues;
import suite.dialogue.InformationDialogue;
import utility.ConfigEditorInfo;
import utility.StringUtilities;
import utility.TaskUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ReverendDread on 12/11/2019
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
@Slf4j
public class TextureController extends FXController {

    @FXML private Canvas canvas;
    @FXML private ListView<TextureConfig> listView;
    @FXML private TextField search, spriteIds, direction, speed, textureType, aField1585, aField1591;
    @FXML private Button setSprites, setDirection, setSpeed, setTextureTypes, setField1585, setField1591;

    private GraphicsContext gc;
    private ObservableList<TextureConfig> textures = FXCollections.observableArrayList();
    private TextureConfig editing;
    private CustomTab tab;
    private BooleanProperty closed = new SimpleBooleanProperty();

    @Override
    public void initialize(CustomTab tab, boolean refresh, int lastId) {

        this.tab = tab;

        textures.setAll(PluginManager.get().getLoaderForType(getInfo().getType()).getDefinitions().values().stream().map((entry) -> (TextureConfig) entry).collect(Collectors.toList()));
        listView.getItems().setAll(textures);

        gc = canvas.getGraphicsContext2D();
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        listView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            this.editing = newValue;
            refresh();
        }));

        ContextMenu menu = new ContextMenu();
        listView.setContextMenu(menu);

        MenuItem create = new MenuItem("Create");
        create.setOnAction((event) -> {
            try {
                int defaultVal = PluginManager.get().getLoaderForType(getInfo().getType()).getDefinitions().size() + 1;
                int id = Dialogues.integerInput("Enter an id.", "Enter the id you want to create, if no id is entered the next one in line will be made.", defaultVal);
                TextureConfig config = (TextureConfig) PluginManager.get().getConfigForType(getInfo().getType()).getClass().newInstance();
                config.id = id;
                config.setSprites(new int[] { 0 });
                CacheLibrary.get().getIndex(getInfo().getIndex()).getArchive(getInfo().getArchive()).addFile(config.id, config.encode(new OutputStream()).flip());
                CacheLibrary.get().getIndex(getInfo().getIndex()).update(Selection.progressListener);
                PluginManager.get().getLoaderForType(getInfo().getType()).reload();
                Platform.runLater(() -> initialize(tab, true, config.id));
            } catch (Exception ex) {
                InformationDialogue.create("Error", "An error has occured.", Alert.AlertType.ERROR, "Unable to create new texture.", null, null);
                ex.printStackTrace();
            }
        });

        setSprites.setOnAction((event -> {
            if (Objects.nonNull(editing)) {
                List<String> text = Lists.newArrayList();
                text.addAll(Arrays.asList(spriteIds.getText().split(";")));
                editing.setSprites(ArrayUtils.toPrimitive(text.stream().mapToInt(Integer::valueOf).boxed().collect(Collectors.toList()).toArray(new Integer[1])));
            }
        }));
        setDirection.setOnAction((event -> {
            if (Objects.nonNull(editing)) {
                editing.setAnimationDirection(Integer.parseInt(direction.getText()));
            }
        }));
        setSpeed.setOnAction((event -> {
            if (Objects.nonNull(editing)) {
                editing.setAnimationSpeed(Integer.parseInt(speed.getText()));
            }
        }));
        setField1585.setOnAction((event -> {
            if (Objects.nonNull(editing)) {
                List<String> text = Lists.newArrayList();
                text.addAll(Arrays.asList(setField1585.getText().split(";")));
                editing.setField1585(ArrayUtils.toPrimitive(text.stream().mapToInt(Integer::valueOf).boxed().toArray(Integer[]::new)));
            }
        }));
        setTextureTypes.setOnAction((event -> {
            if (Objects.nonNull(editing)) {
                List<String> text = Lists.newArrayList();
                text.addAll(Arrays.asList(setTextureTypes.getText().split(";")));
                editing.setTextureType(ArrayUtils.toPrimitive(text.stream().mapToInt(Integer::valueOf).boxed().toArray(Integer[]::new)));
            }
        }));
        setField1591.setOnAction((event -> {
            if (Objects.nonNull(editing)) {
                List<String> text = Lists.newArrayList();
                text.addAll(Arrays.asList(setField1591.getText().split(";")));
                editing.setField1591(ArrayUtils.toPrimitive(text.stream().mapToInt(Integer::valueOf).boxed().toArray(Integer[]::new)));
            }
        }));

        menu.getItems().addAll(create);

        refresh();

    }

    @Override
    public void save() {
        try {
            if (Objects.nonNull(editing)) {
                Task<Boolean> save = TaskUtil.create(() -> {
                    CacheLibrary.get().getIndex(getInfo().getIndex()).getArchive(getInfo().getArchive()).addFile(editing.id, editing.encode(new OutputStream()).flip());
                    CacheLibrary.get().getIndex(getInfo().getIndex()).update(Selection.progressListener);
                    PluginManager.get().getLoaderForType(getInfo().getType()).reload();
                    Platform.runLater(() -> initialize(tab, true, editing.id));
                    return true;
                });
                Main.getSelection().createTask("Saving changes...", false, save);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public ConfigEditorInfo getInfo() {
        return ConfigEditorInfo.builder().index(9).archive(0).type(PluginType.TEXTURE).build();
    }

    private void refresh() {
        try {
            if (Objects.nonNull(editing)) {
                this.tab.setText("Texture editor - " + editing.toString());
                //clear the canvas
                gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
                //set fields
                spriteIds.setText(StringUtilities.formatIntArrayToString(editing.getSprites()));
                direction.setText("" + editing.getAnimationDirection());
                speed.setText("" + editing.getAnimationSpeed());
                aField1585.setText(StringUtilities.formatIntArrayToString(editing.getField1585()));
                textureType.setText(StringUtilities.formatIntArrayToString(editing.getTextureType()));
                aField1591.setText(StringUtilities.formatIntArrayToString(editing.getField1591()));
                //draw sprite
                if (!editing.getImages().isEmpty()) {
                    Optional<Image> optImage = Optional.of(editing.getImages().get(0));
                    Image image = optImage.orElse(null);
                    gc.setFill(Color.BLACK);
                    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    gc.drawImage(image, (gc.getCanvas().getWidth() / 2) - (image.getWidth() / 2), (gc.getCanvas().getHeight() / 2) - (image.getHeight() / 2));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
