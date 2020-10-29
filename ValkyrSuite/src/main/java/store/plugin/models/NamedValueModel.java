/**
 * 
 */
package store.plugin.models;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author ReverendDread Sep 14, 2019
 */
@RequiredArgsConstructor
@Getter
public class NamedValueModel {

	final SimpleStringProperty name;
	final SimpleObjectProperty value;

}
