package suite.controller;

import java.io.File;
import java.io.IOException;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import persistable.Persistable;
import store.CacheLibrary;
import store.plugin.PluginManager;
import suite.Constants;
import suite.Main;
import suite.dialogue.Dialogues;
import utility.TaskUtil;

public class Settings {

	@FXML
	public TextField cache_dir;
	@FXML
	public CheckBox focus_on_search;
	@FXML
	public CheckBox success_notifications;
	@FXML
	public CheckBox debug;
	@FXML
	public TextField xtea_dir;

	public Persistable persistable;

	public Settings() {
		this.persistable = Constants.settings.load();
	}

	public void startup() {
		try {
			cache_dir.setText(persistable.cacheDir);
			focus_on_search.setSelected(persistable.focusOnSearch);
			success_notifications.setSelected(persistable.notifications);
			debug.setSelected(persistable.debug);
			xtea_dir.setText(persistable.xteaFile);
		} catch (NullPointerException e) {
			persistable.delete();
			persistable.save();
			System.out.println("Creating new save file, new settings added.");
		}
	}

	@FXML
	public void resetCacheDirectory() {
		persistable.cacheDir = null;
		Selection.cache = null;
		cache_dir.setText("");
		persistable.save();
	}

	@FXML
	public void resetXTEADirectory() {
		persistable.xteaFile = null;
		xtea_dir.setText("");
		persistable.save();
	}

	@FXML
	public void reloadPlugins() {
		Main.getSelection().createTask("Reloading plugins", true, TaskUtil.create(() -> PluginManager.get().reload()));
	}

	@FXML
	public void edit_cache_dir() throws IOException {
		DirectoryChooser dir_chooser = new DirectoryChooser();
		dir_chooser.setTitle("Locate Cache Directory..");
		Stage stage = Main.getPrimaryStage();
		File default_dir = new File(System.getProperty("user.home") + File.separator + "Desktop" + File.separator);
		dir_chooser.setInitialDirectory(default_dir);
		File directory = dir_chooser.showDialog(stage);
		if (directory == null) {
			Dialogues.alert(AlertType.ERROR, "Error", null, "Failed to load cache.", true);
			return;
		}
		persistable.cacheDir = directory.toString() + "\\";
		cache_dir.setText(persistable.cacheDir);
		persistable.save();
		Dialogues.alert(AlertType.INFORMATION, "Notification", null, "Changes saved successfully, reloading data.", true);
		Main.getSelection().tabs.getTabs().clear();
		Main.getSelection().createTask("Reloading data...", true, new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				Platform.runLater(() -> {
					Main.getSelection().initialize(Main.getPrimaryStage());
				});
				return null;
			}

		});
	}

	@FXML
	public void edit_xtea_dir() {
		FileChooser dir_chooser = new FileChooser();
		dir_chooser.setTitle("Locate Cache Directory..");
		Stage stage = Main.getPrimaryStage();
		File default_dir = new File(System.getProperty("user.home") + File.separator + "Desktop" + File.separator);
		dir_chooser.setInitialDirectory(default_dir);
		File directory = dir_chooser.showOpenDialog(stage);
		if (directory == null) {
			return;
		}
		persistable.xteaFile = directory.getAbsolutePath();
		xtea_dir.setText(persistable.xteaFile);
		persistable.save();
	}

	@FXML
	public void focusOnSearch() {
		persistable.focusOnSearch = focus_on_search.isSelected();
		persistable.save();
	}

	@FXML
	public void toggleNotifications() {
		persistable.notifications = success_notifications.isSelected();
		persistable.save();
	}

	@FXML
	public void toggleDebug() {
		persistable.debug = debug.isSelected();
		persistable.save();
	}

	@FXML
	public void defrag_cache() {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Choose output path for cache.");
		Stage stage = Main.getPrimaryStage();
		File default_dir = new File(System.getProperty("user.home"));
		chooser.setInitialDirectory(default_dir);
		File file = chooser.showDialog(stage);
		if (file == null) {
			return;
		}
		boolean confirmed = Dialogues.confirmation("Confirmation", "Are you sure you want to de-fragment your cache? This will close open editors, and could take a few minutes!");
		if (confirmed) {
			Main.getSelection().createTask("De-fragmenting cache...", true, TaskUtil.create(() -> {
				CacheLibrary.get().rebuild(file);
				return false;
			}));
		}
	}

}
