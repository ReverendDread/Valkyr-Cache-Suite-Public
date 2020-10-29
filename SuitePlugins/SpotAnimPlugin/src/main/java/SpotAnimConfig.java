import com.google.common.collect.Maps;
import javafx.util.Pair;
import store.io.impl.InputStream;
import store.io.impl.OutputStream;
import store.plugin.extension.ConfigExtensionBase;
import store.utilities.ReflectionUtils;
import suite.annotation.MeshIdentifier;
import suite.annotation.OrderType;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author ReverendDread on 5/21/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
public class SpotAnimConfig extends ConfigExtensionBase {

    private static Map<Field, Integer> fieldPriorities;

    @OrderType(priority = 1) @MeshIdentifier
    public int modelId = -1;
    @OrderType(priority = 2)
    public int animation = -1;
    @OrderType(priority = 3)
    public int widthScale = 128;
    @OrderType(priority = 4)
    public int heightScale = 128;
    @OrderType(priority = 5)
    public int orientation = 0;
    @OrderType(priority = 6)
    public int ambient;
    @OrderType(priority = 6)
    public int contrast;
    @OrderType(priority = 7)
    public short[] recolorToFind;
    @OrderType(priority = 8)
    public short[] recolorToReplace;
    @OrderType(priority = 9)
    public short[] retextureToFind;
    @OrderType(priority = 10)
    public short[] retextureToReplace;

    @Override
    public void decode(int opcode, InputStream buffer) {
        switch (opcode) {
            case 1:
                modelId = buffer.readUnsignedShort();
                break;
            case 2:
                animation = buffer.readUnsignedShort();
                break;
            case 4:
                widthScale = buffer.readUnsignedShort();
                break;
            case 5:
                heightScale = buffer.readUnsignedShort();
                break;
            case 6:
                orientation = buffer.readUnsignedShort();
                break;
            case 7:
                ambient = buffer.readUnsignedByte();
                break;
            case 8:
                contrast = buffer.readUnsignedByte();
                break;
            case 40: {
                int length = buffer.readUnsignedByte();
                recolorToFind = new short[length];
                recolorToReplace = new short[length];
                for (int i = 0; i < length; i++) {
                    recolorToFind[i] = ((short) buffer.readUnsignedShort());
                    recolorToReplace[i] = (short) buffer.readUnsignedShort();
                }
                break;
            }
            case 41: {
                int length = buffer.readUnsignedByte();
                retextureToFind = new short[length];
                retextureToReplace = new short[length];
                for (int i = 0; i < length; i++) {
                    retextureToFind[i] = ((short) buffer.readUnsignedShort());
                    retextureToReplace[i] = (short) buffer.readUnsignedShort();
                }
                break;
            }
        }
    }

    @Override
    public OutputStream encode(OutputStream buffer) {

        buffer.writeByte(1);
        buffer.writeShort(modelId);

        if (animation != -1) {
            buffer.writeByte(2);
            buffer.writeShort(animation);
        }

        if (widthScale != 128) {
            buffer.writeByte(4);
            buffer.writeShort(widthScale);
        }

        if (heightScale != 128) {
            buffer.writeByte(5);
            buffer.writeShort(heightScale);
        }

        if (orientation != 0) {
            buffer.writeByte(6);
            buffer.writeShort(orientation);
        }

        if (ambient != 0) {
            buffer.writeByte(7);
            buffer.writeShort(ambient);
        }

        if (contrast != 0) {
            buffer.writeByte(8);
            buffer.writeShort(contrast);
        }

        if (recolorToFind != null && recolorToReplace != null) {
            buffer.writeByte(40);
            int colorSize = Math.min(recolorToFind.length, recolorToReplace.length);
            for (int index = 0; index < colorSize; index++) {
                buffer.writeShort(recolorToFind[index]);
                buffer.writeShort(recolorToReplace[index]);
            }
        }

        if (retextureToFind != null && retextureToReplace != null) {
            buffer.writeByte(41);
            int textureSize = Math.min(retextureToFind.length, retextureToReplace.length);
            for (int index = 0; index < textureSize; index++) {
                buffer.writeShort(retextureToFind[index]);
                buffer.writeShort(retextureToReplace[index]);
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
