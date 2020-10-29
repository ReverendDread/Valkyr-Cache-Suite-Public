package misc;

import javafx.scene.control.Tab;
import lombok.Getter;
import store.plugin.PluginType;
import store.plugin.extension.FXController;

import java.util.UUID;

/**
 * 
 * @author ReverendDread Jul 10, 2018
 */
@Getter
public class CustomTab extends Tab {

	/**
	 * Controller object for this tab (Will be casted later on depending on the
	 * {@code EditorType}.
	 */
	public final FXController controller;
	
	/**
	 * EditorType of this tab.
	 */
	public final PluginType type;

	/**
	 * A key unique to this tab.
	 */
	public UUID uuid;

	public CustomTab(FXController controller, PluginType type) {
		this.controller = controller;
		this.type = type;
		this.uuid = UUID.randomUUID();
	}
}
