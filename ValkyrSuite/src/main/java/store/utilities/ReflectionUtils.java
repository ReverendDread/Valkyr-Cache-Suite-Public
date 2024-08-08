/**
 * 
 */
package store.utilities;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.collect.Maps;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Shorts;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import suite.annotation.LinkedField;
import store.plugin.extension.ConfigExtensionBase;
import store.plugin.models.NamedValueObject;

@Slf4j
public class ReflectionUtils {

	public static List<NamedValueObject> getValueAsNamedValueList(ConfigExtensionBase object) {
		List<NamedValueObject> list =  object.getPriority()
				.entrySet()
		.stream()
		.sorted(Comparator.comparingInt(Entry::getValue))
		.map(entry -> new NamedValueObject(entry.getKey().getName(), entry.getKey(), getValueForField(entry.getKey(), object)))
		.collect(Collectors.toList());
		Map<String, List<NamedValueObject>> map = list.stream().filter(nvo -> nvo.getField().isAnnotationPresent(LinkedField.class))
				.collect(Collectors.groupingBy(nvo -> nvo.getField().getAnnotation(LinkedField.class).groupName()));
		map.values().forEach(list::removeAll);
		map.entrySet().forEach(entry -> {
			if(entry.getValue().get(0).getField().getType().isAssignableFrom(int[].class)) {}
		});
		return list;
	}

	public static Object getValueForField(Field field, Object object){
		try {
			return field.get(object);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static Map<String, Pair<Field, Object>> getValues(Object object) {
		Map<String, Pair<Field, Object>> values = new HashMap<>();
		for (Field field : object.getClass().getDeclaredFields()) {
			if (field == null || Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			Pair<Field, Object> pair = new Pair<>(field, null);
			String name = field.getName();
			Object value = null;
			try {
				field.setAccessible(true);
				value = field.get(object);
				if(value != null)
					pair = new Pair<>(field, value);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			values.put(name, pair);
		}
		return values;
	}
	
	public static void setValue(Object originalObject, Field field, Object value) {
		try {
			Class<?> type = field.getType();
			field.setAccessible(true);
			if (value instanceof TextField) {
				TextField textField = ((TextField) value);
				if (type.isAssignableFrom(String.class)) {
					field.set(originalObject, textField.getText());
				} else if (type.isAssignableFrom(long.class)) {
					field.set(originalObject, Long.parseLong(textField.getText()));
				} else if (type.isAssignableFrom(int.class)) {
					field.set(originalObject, Integer.parseInt(textField.getText()));
				} else if (type.isAssignableFrom(short.class)) {
					field.set(originalObject, Integer.parseInt(textField.getText()));
				} else if (type.isAssignableFrom(byte.class)) {
					field.set(originalObject, Short.parseShort(textField.getText()));
				} else if (type.isAssignableFrom(char.class)) {
					field.set(originalObject, textField.getText().charAt(0));
				} else {
					log.info("Invalid textfield input {}", type);
				}
			} else if (value instanceof ComboBox) {
				ComboBox<String> box = ((ComboBox) value);
				if (type.isAssignableFrom(String[].class)) {
					field.set(originalObject, box.getItems().toArray(new String[0]));
				} else if (type.isAssignableFrom(int[].class)) {
					field.set(originalObject, Ints.toArray(box.getItems().stream().map(Integer::valueOf).collect(Collectors.toList())));
				} else if (type.isAssignableFrom(short[].class)) {
					field.set(originalObject, Shorts.toArray(box.getItems().stream().map(Integer::valueOf).collect(Collectors.toList())));
				} else if (type.isAssignableFrom(byte[].class)) {
					field.set(originalObject, Bytes.toArray(box.getItems().stream().map(Byte::valueOf).collect(Collectors.toList())));
				} else {
					log.info("Invalid combobox input {}", type);
				}
			} else if (value instanceof CheckBox) {
				CheckBox box = ((CheckBox) value);
				if (type.isAssignableFrom(boolean.class)) {
					field.set(originalObject, box.isSelected());
				} else {
					log.info("Invalid checkbox input {}", type);
				}
			} else if (value instanceof HBox) {
				HBox hbox = ((HBox) value);
				if (hbox.getProperties().values().contains("params")) {
					ComboBox<String> keys = (ComboBox<String>) hbox.getChildren().get(0);
					ComboBox<String> values = (ComboBox<String>) hbox.getChildren().get(1);
					Map<Integer, Object> params = Maps.newHashMap();
					IntStream
					.range(0, keys.getItems().size())
					.forEach(index -> {
						try {
							Integer key = Integer.parseInt(keys.getItems().get(index));
							Object obj;
							try {
								obj = Integer.parseInt(values.getItems().get(index));
							} catch (Exception ex) {
								obj = String.valueOf(values.getItems().get(index));
							}
							params.put(key, obj);
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
					field.set(originalObject, params);
				} else if (hbox.getProperties().values().contains("ints")) {
					ComboBox<String> box = ((ComboBox) hbox.getChildren().get(0));
					field.set(originalObject, Ints.toArray(box.getItems().stream().map(Integer::valueOf).collect(Collectors.toList())));
				} else if (hbox.getProperties().values().contains("shorts")) {
					ComboBox<String> box = ((ComboBox) hbox.getChildren().get(0));
					field.set(originalObject, Ints.toArray(box.getItems().stream().map(Integer::valueOf).map(Integer::shortValue).collect(Collectors.toList())));
				} else if (hbox.getProperties().values().contains("strings")) {
					ComboBox<String> box = ((ComboBox) hbox.getChildren().get(0));
					field.set(originalObject, box.getItems().toArray(new String[0]));
				} else {
					log.info("Invalid hbox input {}", type);
				}
			} else {
				log.info("Type Value: {}", value);
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
