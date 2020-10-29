import com.google.common.collect.Lists;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;
import store.io.impl.InputStream;
import store.io.impl.OutputStream;
import store.plugin.PluginManager;
import store.plugin.PluginType;
import store.plugin.extension.ConfigExtensionBase;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @author ReverendDread on 12/11/2019
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
@Setter @Getter
public class TextureConfig extends ConfigExtensionBase {

    private int[] sprites;
    private int[] textureType;
    private int[] field1585;
    private int averageRGB;
    private boolean field1587;
    private int[] field1591;
    private int animationDirection;
    private int animationSpeed;
    private int[] pixels;

    @Override
    public void decode(int opcode, InputStream buffer) {
        this.averageRGB = buffer.readUnsignedShort();
        this.field1587 = buffer.readUnsignedByte() == 1;
        int size = buffer.readUnsignedByte();
        if (size >= 1 && size <= 4) {
            this.sprites = new int[size];
            for (int file = 0; file < size; file++) {
                this.sprites[file] = buffer.readUnsignedShort();
            }
            if (size > 1) {
                this.textureType = new int[size - 1];
                for (int file = 0; file < size - 1; file++) {
                    this.textureType[file] = buffer.readUnsignedByte();
                }
            }
            if (size > 1) {
                this.field1585 = new int[size - 1];
                for (int file = 0; file < size - 1; file++) {
                    this.field1585[file] = buffer.readUnsignedByte();
                }
            }
            this.field1591 = new int[size];
            for (int file = 0; file < size; file++) {
                this.field1591[file] = buffer.readInt();
            }
            this.animationDirection = buffer.readUnsignedByte();
            this.animationSpeed = buffer.readUnsignedByte();
        }
    }

    @Override
    public OutputStream encode(OutputStream buffer) {
        buffer.writeShort(this.averageRGB);
        buffer.writeByte(this.field1587 ? 1 : 0);
        int size = sprites == null ? 0 : sprites.length;
        buffer.writeByte(size);
        if (size >= 1 && size <= 4) {
            IntStream.of(this.sprites).forEach(buffer::writeShort);
            if (size > 1) {
                if (this.textureType == null)
                    IntStream.of(new int[this.sprites.length - 1]).forEach(buffer::writeByte);
                else
                    IntStream.of(this.textureType).forEach(buffer::writeByte);
            }
            if (size > 1) {
                if (this.field1585 == null)
                    IntStream.of(new int[this.sprites.length - 1]).forEach(buffer::writeByte);
                else
                    IntStream.of(this.field1585).forEach(buffer::writeByte);
            }
            if (this.field1591 == null)
                IntStream.of(new int[this.sprites.length]).forEach(buffer::writeInt);
            else
                IntStream.of(this.field1591).forEach(buffer::writeInt);
            buffer.writeByte(getAnimationDirection());
            buffer.writeByte(getAnimationSpeed());
        }
        return buffer;
    }

    @Override
    public String toString() {
        return id + "";
    }

    @Override
    public Map<Field, Integer> getPriority() {
        return null;
    }

    @Override
    public List<Image> getImages() {
        List<Image> images = Lists.newArrayList();
        if (this.sprites != null) {
            for (int sprite : this.sprites) {
                images.addAll(PluginManager.get().getLoaderForType(PluginType.SPRITE).getDefinitions().get(sprite).getImages());
            }
        }
        return images;
    }
}
