import store.plugin.Plugin;
import suite.annotation.PluginDescriptor;
import store.plugin.PluginType;

/**
 * 
 */

/**
 * @author ReverendDread
 * Oct 25, 2019
 */
@PluginDescriptor(author = "ReverendDread", description = "A Sprite packer/dumper.", type = PluginType.SPRITE, version = "183")
public class SpritePlugin extends Plugin {

	@Override
	public boolean load() {
		setConfig(new SpriteContainer());
		setLoader(new SpriteLoader());
		setController(new SpriteEditor());
		return true;
	}

	@Override
	public String getFXML() {
		return "SpriteEditor.fxml";
	}

}
