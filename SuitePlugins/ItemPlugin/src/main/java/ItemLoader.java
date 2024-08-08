import javafx.scene.control.Alert;
import store.CacheLibrary;
import store.cache.index.Index;
import store.cache.index.archive.file.File;
import store.io.impl.InputStream;
import store.plugin.extension.LoaderExtensionBase;
import suite.controller.Selection;
import suite.dialogue.Dialogues;

/**
 * 
 */

/**
 * @author ReverendDread
 * Oct 6, 2019
 */
public class ItemLoader extends LoaderExtensionBase {

	@Override
	public boolean load() {
		try {
			Index index = CacheLibrary.get().getIndex(getIndex());
			int[] fileIds = index.getArchive(getArchive()).getFileIds();
			for (int id : fileIds) {
				File file = index.getArchive(getArchive()).getFile(id);
				if (file == null || file.getData() == null)
					continue;
				ItemConfig definition = new ItemConfig();
				definition.id = id;
				InputStream buffer = new InputStream(file.getData());
				readConfig(buffer, definition);
				definitions.put(id, toNote(definition));
				Selection.progressListener.pluginNotify("(" + id + "/" + fileIds.length + ")");
			}
			return true;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public int getFile() {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public int getArchive() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public int getIndex() {
		// TODO Auto-generated method stub
		return 2;
	}
	
	private ItemConfig toNote(ItemConfig config) {
		if (config.notedID != -1 && config.notedTemplate != -1) {
			ItemConfig original = (ItemConfig) getDefinitions().get(config.notedID);
			if (original != null) {
				config.name = original.name;
				config.cost = original.cost;
				config.members = original.members;
				config.stackable = original.stackable;
			} else {
				Dialogues.alert(Alert.AlertType.ERROR, "Error", "You can't import an item with a non-existing note item.", null, false);
				return null;
			}
		}
		return config;
	}

}
