package suite.opengl.texture;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.CacheLibrary;
import store.io.impl.InputStream;
import store.plugin.PluginManager;
import store.plugin.PluginType;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * @author ReverendDread on 7/26/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
@RequiredArgsConstructor
public class Texture {

    @Getter private final int id;
    private byte[] data;
    private int[] sprites;
    private int[] textureType;
    private int[] field1585;
    private int averageRGB;
    private boolean field1587;
    private int[] field1591;
    private int animationDirection;
    private int animationSpeed;
    private int[] pixels;

    public void decode(CacheLibrary library) {
        data = CacheLibrary.get().getIndex(9).getFileData(0, this.id);
        InputStream buffer = new InputStream(data);
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
        List<Image> images = PluginManager.get().getLoaderForType(PluginType.SPRITE).getDefinitions().get(sprites[0]).getImages();
        int i = 0;
        Image image = images.get(0);
        this.pixels = new int[(int) (image.getWidth() * image.getHeight())];
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                pixels[i++] = image.getPixelReader().getArgb(x, y);
            }
        }
    }

    /**
     * Convert the pixels of this texture to a {@code ByteBuffer} {@code Object}.
     * @return The byte buffer.
     */
    public ByteBuffer toByteBuffer() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(pixels.length * 4);
        for (int pixel : pixels) {
            buffer.put((byte) ((pixel >> 16) & 0xFF));
            buffer.put((byte) ((pixel >> 8) & 0xFF));
            buffer.put((byte) (pixel & 0xFF));
            buffer.put((byte) (pixel >> 24));
        }
        buffer.flip();
        return buffer;
    }

    public Image getImage() {
        List<Image> images = PluginManager.get().getLoaderForType(PluginType.SPRITE).getDefinitions().get(sprites[0]).getImages();
        return images.get(0);
    }

}
