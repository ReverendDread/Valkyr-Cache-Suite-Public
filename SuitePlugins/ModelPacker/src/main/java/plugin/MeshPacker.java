package plugin;

import com.misc.MeshHolder;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import misc.CustomTab;
import misc.MQOConverter;
import misc.RsMesh;
import org.apache.commons.io.FilenameUtils;
import store.CacheLibrary;
import store.cache.index.OSRSIndices;
import store.codec.model.Mesh;
import store.plugin.PluginType;
import store.plugin.extension.FXController;
import suite.Main;
import suite.controller.Selection;
import suite.dialogue.Dialogues;
import suite.dialogue.InformationDialogue;
import suite.opengl.wrapper.impl.MeshWrapper;
import utility.ConfigEditorInfo;
import utility.FileUtilities;
import utility.StringUtilities;
import utility.TaskUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MeshPacker extends FXController {

	private File directory;
	private CustomTab tab;
	private MeshWrapper wrapper;

	private ObservableList<MeshHolder> meshRepo = FXCollections.observableArrayList();
	
	@FXML public TextField path;
	@FXML public TextArea console;
	@FXML public ListView<MeshHolder> meshList;
	@FXML public TextField search_bar;
	@FXML public ImageView image;
	@FXML public StackPane viewParent;
	@FXML public Label fps;
	@FXML public Label verts;
	@FXML public Label faces;
	@FXML public CheckBox showGrid;
	@FXML public CheckBox showPriorities;
	@FXML public CheckBox showWeights;

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
		this.meshRepo.clear();
		this.directory = new File(directory.toString());
		path.setText(directory.toString());
		if (this.directory.list().length > 0) {
			List<MeshHolder> holders = Stream.of(this.directory.listFiles()).filter(file -> file.getName().contains(".mqo") || file.getName().contains(".dat")).map(MeshHolder::new).collect(Collectors.toList());
			this.meshRepo.setAll(holders);
			this.meshList.getItems().setAll(holders);
		}
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

		console.textProperty().addListener((ChangeListener<Object>) (observable, oldValue, newValue) -> console.setScrollTop(Double.MAX_VALUE));

		search_bar.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.equals("")) {
				meshList.getItems().setAll(meshRepo);
				return;
			}
			List<MeshHolder> matching = meshRepo.stream().filter(mesh -> mesh.getFile().getName().contains(newValue)).collect(Collectors.toList());
			meshList.getItems().setAll(matching);
		});

		new Thread(() -> {
			wrapper = new MeshWrapper(tab, image);
			wrapper.run(fps, verts, faces);
		}, "GLThread").start();

		image.fitWidthProperty().bind(viewParent.widthProperty());
		image.fitHeightProperty().bind(viewParent.heightProperty());

		showGrid.setOnAction((event) -> wrapper.setShowGrid(showGrid.isSelected()));
		showGrid.setSelected(true);
		showWeights.setOnAction((event) -> wrapper.setShowWeights(showWeights.isSelected()));
		showPriorities.setOnAction((event) -> wrapper.setShowPriorities(showPriorities.isSelected()));
		meshList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> wrapper.setContext(newValue.getMesh()));

		ContextMenu menu = new ContextMenu();
		MenuItem importSingle = new MenuItem("Pack to cache");
		importSingle.setOnAction((event) -> {
			try {
				MeshHolder holder = meshList.getSelectionModel().getSelectedItem();
				if (holder != null) {
					byte[] data = null;
					int id = CacheLibrary.get().getIndex(OSRSIndices.MODELS).getArchiveIds().length + 1;
					switch (holder.getFormat()) {
						case DAT: {
							data = Files.readAllBytes(holder.getFile().toPath());
						}
						break;
						case MQO: {
							MQOConverter converter = new MQOConverter();
							Mesh mesh = converter.assembleMesh(holder.getFile());
							data = mesh.encodeRS2();
						}
						break;
						default:
							console.appendText("Unhandled file format.\n");
					}
					byte[] finalData = data;
					Main.getSelection().createTask("Packing model...", true, TaskUtil.create(() -> {
						CacheLibrary.get().getIndex(OSRSIndices.MODELS).addArchive(id).addFile(0, finalData);
						CacheLibrary.get().getIndex(OSRSIndices.MODELS).update(Selection.progressListener);
						console.appendText("Packed model " + FilenameUtils.getName(holder.toString()) + " as model " + id + ".\n");
						return true;
					}));
				} else {
					console.appendText("Failed to pack model, file is null.\n");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		MenuItem importMultiDat = new MenuItem("Multi .dat (Uses file name)");
		importMultiDat.setOnAction((event) -> {
			DirectoryChooser chooser = new DirectoryChooser();
			chooser.setTitle("Choose a directory to import from");
			File directory = chooser.showDialog(Main.getPrimaryStage());
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
		menu.getItems().addAll(importSingle, importMultiDat);
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
