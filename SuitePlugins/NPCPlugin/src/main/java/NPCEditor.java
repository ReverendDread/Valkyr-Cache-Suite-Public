

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import lombok.extern.slf4j.Slf4j;
import misc.CustomTab;
import store.CacheLibrary;
import store.cache.index.OSRSIndices;
import store.io.impl.InputStream;
import store.io.impl.OutputStream;
import store.plugin.PluginManager;
import store.plugin.PluginType;
import store.plugin.extension.ConfigExtensionBase;
import store.plugin.extension.FXController;
import store.plugin.models.NamedValueObject;
import store.utilities.ReflectionUtils;
import suite.Constants;
import suite.Main;
import suite.controller.ConfigEditor;
import suite.controller.Selection;
import suite.dialogue.Dialogues;
import suite.dialogue.InformationDialogue;
import utility.*;

@Slf4j
public class NPCEditor extends ConfigEditor {

	@Override
	public ConfigEditorInfo getInfo() {
		return ConfigEditorInfo.builder().index(2).archive(9).type(PluginType.NPC).build();
	}

}
