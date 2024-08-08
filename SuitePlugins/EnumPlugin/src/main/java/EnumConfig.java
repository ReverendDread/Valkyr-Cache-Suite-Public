import com.google.common.collect.Maps;
import javafx.util.Pair;
import store.io.impl.InputStream;
import store.io.impl.OutputStream;
import store.plugin.extension.ConfigExtensionBase;
import store.utilities.ReflectionUtils;
import suite.annotation.OrderType;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author ReverendDread on 5/17/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
public class EnumConfig extends ConfigExtensionBase {

    private static Map<Field, Integer> fieldPriorities;

    @OrderType(priority = 1)
    public char keyType;
    @OrderType(priority = 2)
    public char valueType;
    @OrderType(priority = 3)
    public String defaultString;
    @OrderType(priority = 4)
    public int defaultInt;
    @OrderType(priority = 5)
    public int[] keys;
    @OrderType(priority = 6)
    public String[] stringValues;
    @OrderType(priority = 7)
    public int[] intValues;

    @Override
    public void decode(int opcode, InputStream buffer) {
        int length;
        switch (opcode) {
            case 1:
                keyType = (char) buffer.readUnsignedByte();
                break;
            case 2:
                valueType = (char) buffer.readUnsignedByte();
                break;
            case 3:
                defaultString = buffer.readString();
                break;
            case 4:
                defaultInt = buffer.readInt();
                break;
            case 5:
                length = buffer.readUnsignedShort();
                keys = new int[length];
                stringValues = new String[length];
                for (int i = 0; i < length; i++) {
                    keys[i] = buffer.readInt();
                    stringValues[i] = buffer.readString();
                }
                break;
            case 6:
                length = buffer.readUnsignedShort();
                keys = new int[length];
                intValues = new int[length];
                for (int i = 0; i < length; i++) {
                    keys[i] = buffer.readInt();
                    intValues[i] = buffer.readInt();
                }
                break;
        }
    }

    @Override
    public OutputStream encode(OutputStream buffer) {

        if (keys != null)
            System.out.println("k: " + keys.length + ", " + Arrays.toString(keys));
        if (stringValues != null)
            System.out.println("s: " + stringValues.length + ", " + Arrays.toString(stringValues));
        if (intValues != null)
            System.out.println("i: " + intValues.length + ", " + Arrays.toString(intValues));

        if (keyType != '\u0000') {
            buffer.writeByte(1);
            buffer.writeByte(keyType);
        }
        if (valueType != '\u0000') {
            buffer.writeByte(2);
            buffer.writeByte(valueType);
        }
        if (defaultString != null) {
            buffer.writeByte(3);
            buffer.writeString(defaultString);
        }
        if (defaultInt != 0) {
            buffer.writeByte(4);
            buffer.writeInt(defaultInt);
        }
        if (keys != null) {
            if (stringValues != null && stringValues.length > 0) {
                buffer.writeByte(5);
                int length = Math.min(keys.length, stringValues.length);
                buffer.writeShort(length);
                for (int i = 0; i < length; i++) {
                    buffer.writeInt(keys[i]);
                    buffer.writeString(stringValues[i]);
                }
            }
            else if (intValues != null && intValues.length > 0) {
                buffer.writeByte(6);
                int length = Math.min(keys.length, intValues.length);
                buffer.writeShort(length);
                for (int i = 0; i < length; i++) {
                    buffer.writeInt(keys[i]);
                    buffer.writeInt(intValues[i]);
                }
            }
        }
        buffer.writeByte(0);
        return buffer;
    }

    @Override
    public String toString() {
        return "" + id;
    }

    @Override
    public Map<Field, Integer> getPriority() {
        if (fieldPriorities != null)
            return fieldPriorities;
        Map<String, Pair<Field, Object>> values = ReflectionUtils.getValues(this);
        fieldPriorities = Maps.newHashMap();
        values.values().stream().forEach(pair -> {
            Field field = pair.getKey();
            int priority = field.isAnnotationPresent(OrderType.class) ? field.getAnnotation(OrderType.class).priority() : 1000;
            fieldPriorities.put(field, priority);
        });
        return fieldPriorities;
    }

}
