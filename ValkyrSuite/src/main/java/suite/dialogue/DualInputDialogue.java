/**
 * 
 */
package suite.dialogue;

import java.util.Optional;

import org.apache.tools.ant.Main;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Pair;
import suite.Constants;

/**
 * @author ReverendDread
 * Sep 30, 2019
 */
public class DualInputDialogue extends Dialog<Pair<Integer, Integer>> {
	
	@FXML private TextField min;
	@FXML private TextField max;

	public Optional<Pair<Integer, Integer>> display(String title) {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("/DualInputDialogue.fxml"));	
			root = loader.load();
			Stage stage = new Stage();
			stage.getIcons().add(new Image(Constants.FAVICON));
			stage.setTitle(title);
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			getDialogPane().getChildren().add(root);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return showAndWait();
	}
	
	@FXML private void finish() {
		try {
			Integer minimum = Integer.parseInt(min.getText());
			Integer maximum = Integer.parseInt(max.getText());
			resultProperty().set(new Pair<Integer, Integer>(minimum, maximum));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		close();
	}
	
}
