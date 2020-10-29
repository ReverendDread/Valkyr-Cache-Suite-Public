package utility;

import javafx.stage.FileChooser.ExtensionFilter;

public enum FilterMode {
	
	// Setup supported filters
	DAT("dat files (*.dat)", "*.dat"), 
	GZIP("gzip files (*.gz)", "*.gz"),
	PNG("Image files (*.png)", "*.png"),
	JPG("Image files (*.jpg)", "*.jpg"),
	PACK(".pack files (*.pack)", "*.pack"),
	JSON("JSON files (*.json)", "*.json"),
	NONE(" (*.)", "*.");

	private ExtensionFilter extensionFilter;

	private FilterMode(String extensionDisplayName, String... extensions) {
		extensionFilter = new ExtensionFilter(extensionDisplayName, extensions);
	}

	public ExtensionFilter getExtensionFilter() {
		return extensionFilter;
	}
}