package suite.dialogue;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import suite.Constants;

import java.util.Optional;

/**
 * 
 * @author ReverendDread Jul 10, 2018
 */
public class Dialogues {

	public static final void alert(AlertType type, String title, String header, String content, boolean canIgnore) {
		if (Constants.settings.notifications && canIgnore)
			return;
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(Constants.FAVICON));
		alert.showAndWait();
	}

	/**
	 * Returns true if the user clicked accept or ok.
	 * @param header
	 * @param content
	 * @return
	 */
	public static boolean confirmation(String header, String content) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(Constants.WINDOW_NAME);
		alert.setHeaderText(header);
		alert.setContentText(content);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(Constants.FAVICON));
		Optional<ButtonType> result = alert.showAndWait();
		return result.isPresent() ? result.get().equals(ButtonType.OK) ? true : false : false;
	}

	/**
	 * Returns the integer value of the user's input, or -1 if the input was invalid.
	 * @param header
	 * @param content
	 * @return
	 */
	public static int integerInput(String header, String content) {
		try {
			TextInputDialog input = new TextInputDialog();
			input.setTitle(Constants.WINDOW_NAME);
			input.setHeaderText(header);
			input.setContentText(content);
			return Integer.parseInt(input.showAndWait().get());
		} catch (NumberFormatException ex) {
			alert(AlertType.ERROR, Constants.WINDOW_NAME, null, "Invalid id entered.", false);
			return -1;
		}
	}

	public static int integerInput(String header, String content, int defaultValue) {
		try {
			TextInputDialog input = new TextInputDialog();
			input.setTitle(Constants.WINDOW_NAME);
			input.setHeaderText(header);
			input.setContentText(content);
			Optional<String> text = input.showAndWait();
			String value = text.orElse(null);
			return value == null || value.isEmpty() ? defaultValue : Integer.parseInt(value);
		} catch (NumberFormatException ex) {
			alert(AlertType.ERROR, Constants.WINDOW_NAME, null, "Invalid id entered.", false);
			return -1;
		}
	}

}
