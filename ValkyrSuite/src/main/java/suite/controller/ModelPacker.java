package suite.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import misc.CustomTab;
import store.CacheLibrary;
import store.cache.index.OSRSIndices;
import store.codec.model.Mesh;
import store.loader.MeshLoader;
import store.plugin.PluginManager;
import store.plugin.PluginType;
import store.plugin.extension.ConfigExtensionBase;
import store.plugin.extension.FXController;
import suite.Main;
import suite.dialogue.Dialogues;
import suite.dialogue.InformationDialogue;
import utility.*;

public class ModelPacker extends FXController {

	private File directory;
	private Stage stage;
	private CustomTab tab;

	private ObservableList<ConfigExtensionBase> meshes = FXCollections.observableArrayList();
	
	@FXML public TextField path;
	@FXML public TextArea console;
	@FXML public ListView<ConfigExtensionBase> meshList;
	@FXML public TextField search_bar;

	@FXML
	public void choose_directory() {
		DirectoryChooser dir_chooser = new DirectoryChooser();
		dir_chooser.setTitle("Locate Model Directory..");
		Stage stage = Main.getPrimaryStage();
		File default_dir = new File(System.getProperty("user.home") + File.separator + "Desktop" + File.separator);
		dir_chooser.setInitialDirectory(default_dir);
		File directory = dir_chooser.showDialog(stage);
		if (directory == null) {
			Dialogues.alert(AlertType.ERROR, "Error", null, "Failed to locate model directory.", true);
			return;
		}
		this.directory = new File(directory.toString());
		path.setText(directory.toString());
		this.stage.toFront();
	}
	
	@FXML
	private void inject_blanks() {
		TextInputDialog input = new TextInputDialog("0");
		input.setHeaderText("Enter how many blanks you would like to inject.");
		Optional<String> value = input.showAndWait();
		if (value.isPresent()) {
			Main.getSelection().createTask("Injecting blanks...", true, TaskUtil.create(() -> {
				int amount = Integer.parseInt(value.get());
				int start = CacheLibrary.get().getIndex(7).getLastArchive().getId() + 1;
				for (int index = start; index < (start + amount); index++) {
					CacheLibrary.get().getIndex(OSRSIndices.MODELS).addArchive(index).addFile(new byte[1]);
				}
				CacheLibrary.get().getIndex(OSRSIndices.MODELS).update(Selection.progressListener);
				console.appendText("Injected " + amount + " blanks, last model index: " + CacheLibrary.get().getIndex(OSRSIndices.MODELS).getLastArchive().getId() + ".\n");
				return true;
			}));
		}
	}

	@FXML
	public void pack() {
		try {

			if (directory == null) {
				Dialogues.alert(AlertType.ERROR, "Error", null, "Model directory was not chosen.", true);
				return;
			}

			Main.getSelection().createTask("Packing models...", true, TaskUtil.create(() -> {
				File[] files = directory.listFiles();
				String packed_models = "";
				int[] model_ids = new int[files.length];
				int archive = CacheLibrary.get().getIndex(7).getLastArchive().getId() + 1;
				for (int index = 0; index < files.length; index++) {
					if (!files[index].getName().endsWith(".dat"))
						continue;
					byte[] data = Files.readAllBytes(files[index].toPath());
					CacheLibrary.get().getIndex(7).addArchive(archive).addFile(0, data);
					String original_id = files[index].getName().substring(0,
							files[index].getName().length() - ".dat".length());
					model_ids[index] = archive;
					packed_models = packed_models + "Packed model " + original_id + " as model "
							+ model_ids[index] + ".\n";
					archive++;
				}
				console.appendText(packed_models);	
				CacheLibrary.get().getIndex(7).update(Selection.progressListener);
				return true;
			}));

		} catch (Throwable e) {
			InformationDialogue.create("Error", "An error has occured while packing models.", AlertType.ERROR, "", "",
					StringUtilities.getStackTrace(e));
		}
	}

	@Override
	public void initialize(CustomTab tab, boolean refresh, int lastId) {
		this.tab = tab;
		this.stage = stage;
		console.textProperty().addListener((ChangeListener<Object>) (observable, oldValue, newValue) -> console.setScrollTop(Double.MAX_VALUE));
		meshes.setAll(PluginManager.get().getLoaderForType(PluginType.MESH).getDefinitions().values());
		meshList.getItems().setAll(meshes);

		search_bar.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.equals("")) {
				meshList.getItems().setAll(meshes);
				return;
			}
			List<ConfigExtensionBase> matching = meshes.stream().filter(config -> config.toString().contains(newValue)).collect(Collectors.toList());
			meshList.getItems().setAll(matching);
		});

		ContextMenu menu = new ContextMenu();
		Menu importMenu = new Menu("Import");
		MenuItem importDat = new MenuItem(".dat (Uses file name)");
		importDat.setOnAction((event) -> {
			File file = RetentionFileChooser.showOpenDialog("Choose a file to import", stage, FilterMode.DAT);
			if (Objects.nonNull(file)) {
				try {
					Integer id = Integer.parseInt(FileUtilities.stripExtension(file.getName()));
					byte[] data = Files.readAllBytes(file.toPath());
					Main.getSelection().createTask("Packing model...", true, TaskUtil.create(() -> {
						CacheLibrary.get().getIndex(OSRSIndices.MODELS).addArchive(id).addFile(0, data);
						CacheLibrary.get().getIndex(OSRSIndices.MODELS).update(Selection.progressListener);
						return true;
					}));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		MenuItem importMultiDat = new MenuItem("Multi .dat (Uses file name)");
		importMultiDat.setOnAction((event) -> {
			DirectoryChooser chooser = new DirectoryChooser();
			chooser.setTitle("Choose a directory to import from");
			File directory = chooser.showDialog(stage);
			if (Objects.nonNull(directory) && directory.isDirectory()) {
				try {
					List<File> files = Stream.of(directory.listFiles()).filter(Objects::nonNull).filter(file ->
							file.getName().endsWith(".dat")).collect(Collectors.toList());
					Main.getSelection().createTask("Packing models...", true, TaskUtil.create(() -> {
						files.stream().forEach(file -> {
							Integer id = Integer.parseInt(FileUtilities.stripExtension(file.getName()));
							try {
								byte[] data = Files.readAllBytes(file.toPath());
								CacheLibrary.get().getIndex(OSRSIndices.MODELS).addArchive(id).addFile(0, data);
							} catch (IOException e) {
								e.printStackTrace();
							}
						});
						CacheLibrary.get().getIndex(OSRSIndices.MODELS).update(Selection.progressListener);
						return true;
					}));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		importMenu.getItems().setAll(importDat, importMultiDat);
		menu.getItems().addAll(importMenu);
		meshList.setContextMenu(menu);
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ConfigEditorInfo getInfo() {
		return ConfigEditorInfo.builder().index(7).type(PluginType.MESH).build();
	}

}
