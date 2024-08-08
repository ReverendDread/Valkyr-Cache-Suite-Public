/**
 * 
 */
package store.plugin.models;

import java.util.Map;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author ReverendDread Sep 14, 2019
 */
@RequiredArgsConstructor
@Getter
public class KeyModel {

	private final SimpleIntegerProperty id;
	private final SimpleStringProperty name;

	private Map<String, Object> map;

	@Override
	public String toString() {
		return id.toString();
	}

}
