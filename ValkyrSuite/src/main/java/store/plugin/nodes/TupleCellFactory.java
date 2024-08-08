package store.plugin.nodes;

import javafx.scene.Node;
import javafx.scene.control.TableCell;
import store.plugin.models.NamedValueObject;

public class TupleCellFactory extends TableCell<NamedValueObject, Node> {

	@Override
	protected void updateItem(Node item, boolean empty) {
		super.updateItem(item, empty);	
		if(empty || item == null) {
			setGraphic(null);
			setText("");
			return;
		}
		setGraphic(item);
	}
}
