package suite;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.ForkJoinPool;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.BasicConfigurator;
import suite.controller.Selection;
import suite.opengl.util.NativeLoader;

@Slf4j
public class Main extends Application {

	private static Stage primary;
	private static BorderPane root;
	private static Selection selection;

	@Override
	public void start(Stage primary) {
		this.primary = primary;
		this.primary.setTitle(Constants.WINDOW_NAME);
		this.primary.getIcons().add(new Image(Constants.FAVICON));
		Platform.setImplicitExit(true);
		initRootLayout();
		initController();
		primary.setOnHiding((event) -> {
			Selection.terminate = true;
			Platform.exit();
		});
		try {
			NativeLoader.extractNatives();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initController() {
		try {
			URL location = Main.class.getClassLoader().getResource("Selection.fxml");
			FXMLLoader loader = new FXMLLoader(location);
			VBox vbox = loader.load();
			Selection selection = loader.getController();
			selection.initialize(primary);
			this.selection = selection;
			root.setCenter(vbox);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initRootLayout() {
		try {
			URL location = Main.class.getClassLoader().getResource("RootLayout.fxml");
			FXMLLoader loader = new FXMLLoader(location);
			root = loader.load();
			Scene scene = new Scene(root);
			primary.setScene(scene);
			primary.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Selection getSelection() {
		return selection;
	}

	/**
	 * Returns the main stage.
	 * 
	 * @return
	 */
	public static Stage getPrimaryStage() {
		return primary;
	}

	public static void main(String[] args) {
		BasicConfigurator.configure();
		if(args != null && args.length > 0) {
			log.info("Args {}", Arrays.toString(args));
			if (args[0].equals("--local")) {
				Constants.localPlugins = true;
				log.info("Loaded application in local mode!");
			}
		}
		launch(args);
	}
}
