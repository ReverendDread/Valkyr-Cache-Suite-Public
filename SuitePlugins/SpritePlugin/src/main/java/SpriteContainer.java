

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import com.google.common.collect.Maps;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;
import store.CacheLibrary;
import store.cache.index.Index;
import store.cache.index.archive.Archive;
import store.cache.index.archive.file.File;
import store.io.impl.InputStream;
import store.io.impl.OutputStream;
import suite.annotation.OrderType;
import store.plugin.PluginManager;
import store.plugin.PluginType;
import store.plugin.extension.ConfigExtensionBase;
import store.utilities.ReflectionUtils;

public class SpriteContainer extends ConfigExtensionBase {

	@Getter private ArrayList<BufferedImage> bufferedImages = Lists.newArrayList();
	public int[] palette;
	private int[][] pixelsIndexes;
	private byte[][] alpha;
	private boolean[] usesAlpha;
	@Getter @Setter private int biggestWidth;
	@Getter @Setter private int biggestHeight;
	@Getter @Setter private boolean loaded;
	
	public SpriteContainer() {
		
	}
	
	public SpriteContainer(int id) {
		this.id = id;
	}
	
	public SpriteContainer(int id, byte[] data) {
		decode(-1, new InputStream(data));
		PluginManager.get().getLoaderForType(PluginType.SPRITE).getDefinitions().put(id, this); //replace with loaded version
	}

	public BufferedImage getBufferedImage(int width, int height, int[] pixelsIndexes, byte[] extraPixels, boolean useExtraPixels) {
		if (width > 0 && height > 0) {
			BufferedImage image = new BufferedImage(width, height, 6);
			int[] rgbArray = new int[width * height];
			int i = 0;
			int i_43_ = 0;
			int i_46_;
			int i_47_;
			if (useExtraPixels && extraPixels != null) {
				for (i_46_ = 0; i_46_ < height; ++i_46_) {
					for (i_47_ = 0; i_47_ < width; ++i_47_) {
						rgbArray[i_43_++] = extraPixels[i] << 24 | this.palette[pixelsIndexes[i] & 255];
						++i;
					}
				}
			} else {
				for (i_46_ = 0; i_46_ < height; ++i_46_) {
					for (i_47_ = 0; i_47_ < width; ++i_47_) {
						int i_48_ = this.palette[pixelsIndexes[i++] & 255];
						rgbArray[i_43_++] = i_48_ != 0 ? -16777216 | i_48_ : 0;
					}
				}
			}

			image.setRGB(0, 0, width, height, rgbArray, 0, width);
			image.flush();
			return image;
		} else {
			return null;
		}
	}

	public int getPaletteIndex(int rgb) {
		if (this.palette == null) {
			this.palette = new int[1];
		}

		for (int palette = 0; palette < this.palette.length; ++palette) {
			if (this.palette[palette] == rgb) {
				return palette;
			}
		}

		if (this.palette.length == 256) {
			throw new IllegalStateException("Palette size is to large!");
		} else {
			int[] newPallete = new int[this.palette.length + 1];
			System.arraycopy(this.palette, 0, newPallete, 0, this.palette.length);
			newPallete[this.palette.length] = rgb;
			this.palette = newPallete;
			return this.palette.length - 1;
		}
	}

	public int addImage(BufferedImage image) {
		this.bufferedImages.add(image);
		this.palette = null;
		this.pixelsIndexes = null;
		this.alpha = null;
		this.usesAlpha = null;
		return this.bufferedImages.size();
	}

	public int removeImage(int index) {
		this.bufferedImages.remove(index);
		this.palette = null;
		this.pixelsIndexes = null;
		this.alpha = null;
		this.usesAlpha = null;
		return this.bufferedImages.size();
	}

	public List<SpriteFrame> toSpriteFrames() {
		SpriteFrame[] frames = new SpriteFrame[this.bufferedImages.size()];
		for (int index = 0; index < bufferedImages.size(); index++) {
			BufferedImage buffered = this.bufferedImages.get(index);
			if (buffered == null)
				continue;
			Image image = SwingFXUtils.toFXImage(buffered, null);
			frames[index] = new SpriteFrame(index, image);
		}
		return Arrays.asList(frames);
	}

	public void replaceImage(BufferedImage image, int index) {
		this.bufferedImages.remove(index);
		this.bufferedImages.add(index, image);
		this.palette = null;
		this.pixelsIndexes = null;
		this.alpha = null;
		this.usesAlpha = null;
	}

	public void generatePallete() {
		this.pixelsIndexes = new int[this.bufferedImages.size()][];
		this.alpha = new byte[this.bufferedImages.size()][];
		this.usesAlpha = new boolean[this.bufferedImages.size()];

		for (int index = 0; index < this.bufferedImages.size(); ++index) {
			BufferedImage image = this.bufferedImages.get(index);
			int[] rgbArray = new int[image.getWidth() * image.getHeight()];
			image.getRGB(0, 0, image.getWidth(), image.getHeight(), rgbArray, 0, image.getWidth());
			this.pixelsIndexes[index] = new int[image.getWidth() * image.getHeight()];
			this.alpha[index] = new byte[image.getWidth() * image.getHeight()];

			for (int pixel = 0; pixel < this.pixelsIndexes[index].length; ++pixel) {
				int rgb = rgbArray[pixel];
				int medintrgb = this.convertToMediumInt(rgb);
				int i = this.getPaletteIndex(medintrgb);
				this.pixelsIndexes[index][pixel] = i;
				if (rgb >> 24 != 0) {
					this.alpha[index][pixel] = (byte) (rgb >> 24);
					this.usesAlpha[index] = true;
				}
			}
		}

	}

	public int convertToMediumInt(int rgb) {
		OutputStream out = new OutputStream(4);
		out.writeInt(rgb);
		InputStream stream = new InputStream(out.getBytes());
		stream.setPosition(1);
		rgb = stream.read24BitInt();
		return rgb;
	}
	
	public boolean hasImages(int indexId, int archiveId, int fileId) {
		Archive archive = CacheLibrary.get().getIndex(indexId).getArchive(archiveId);
		if (Objects.isNull(archive))
			return false;
		File file = archive.getFile(fileId);
		return Objects.nonNull(file) && Objects.nonNull(file.getData());
	}
	
	public SpriteContainer requestLoad() {
		if (isLoaded()) {
			return this;
		}
		Index index = CacheLibrary.get().getIndex(8);
		Archive archive = index.getArchive(id);
		if (Objects.nonNull(archive) && archive.containsData()) {
			File file = archive.getFile(0);
			if (Objects.nonNull(file) && Objects.nonNull(file.getData()))
				return new SpriteContainer(id, file.getData());
		}
		return null;
	}

	@Override
	public void decode(int opcode, InputStream buffer) {
		buffer.setPosition(buffer.buffer.length - 2);
		int count = buffer.readUnsignedShort();
		this.pixelsIndexes = new int[count][];
		this.alpha = new byte[count][];
		this.usesAlpha = new boolean[count];
		int[] imagesMinX = new int[count];
		int[] imagesMinY = new int[count];
		int[] imagesWidth = new int[count];
		int[] imagesHeight = new int[count];
		buffer.setPosition(buffer.buffer.length - 7 - count * 8);
		this.setBiggestWidth(buffer.readShort());
		this.setBiggestHeight(buffer.readShort());
		int palleteLength = (buffer.readUnsignedByte() & 255) + 1;

		int index;
		for (index = 0; index < count; ++index) {
			imagesMinX[index] = buffer.readUnsignedShort();
		}

		for (index = 0; index < count; ++index) {
			imagesMinY[index] = buffer.readUnsignedShort();
		}

		for (index = 0; index < count; ++index) {
			imagesWidth[index] = buffer.readUnsignedShort();
		}

		for (index = 0; index < count; ++index) {
			imagesHeight[index] = buffer.readUnsignedShort();
		}

		buffer.setPosition(buffer.buffer.length - 7 - count * 8 - (palleteLength - 1) * 3);
		this.palette = new int[palleteLength];

		for (index = 1; index < palleteLength; ++index) {
			this.palette[index] = buffer.read24BitInt();
			if (this.palette[index] == 0) {
				this.palette[index] = 1;
			}
		}

		buffer.setPosition(0);

		for (index = 0; index < count; ++index) {
			int pixelsIndexesLength = imagesWidth[index] * imagesHeight[index];
			this.pixelsIndexes[index] = new int[pixelsIndexesLength];
			this.alpha[index] = new byte[pixelsIndexesLength];
			int maskData = buffer.readUnsignedByte();
			int i_31_;
			if ((maskData & 2) == 0) {
				int var201;
				if ((maskData & 1) == 0) {
					for (var201 = 0; var201 < pixelsIndexesLength; ++var201) {
						this.pixelsIndexes[index][var201] = (byte) buffer.readByte();
					}
				} else {
					for (var201 = 0; var201 < imagesWidth[index]; ++var201) {
						for (i_31_ = 0; i_31_ < imagesHeight[index]; ++i_31_) {
							this.pixelsIndexes[index][var201 + i_31_ * imagesWidth[index]] = (byte) buffer
									.readByte();
						}
					}
				}
			} else {
				this.usesAlpha[index] = true;
				boolean var20 = false;
				if ((maskData & 1) == 0) {
					for (i_31_ = 0; i_31_ < pixelsIndexesLength; ++i_31_) {
						this.pixelsIndexes[index][i_31_] = (byte) buffer.readByte();
					}

					for (i_31_ = 0; i_31_ < pixelsIndexesLength; ++i_31_) {
						byte var21 = this.alpha[index][i_31_] = (byte) buffer.readByte();
						var20 |= var21 != -1;
					}
				} else {
					int var211;
					for (i_31_ = 0; i_31_ < imagesWidth[index]; ++i_31_) {
						for (var211 = 0; var211 < imagesHeight[index]; ++var211) {
							this.pixelsIndexes[index][i_31_ + var211 * imagesWidth[index]] = buffer.readByte();
						}
					}

					for (i_31_ = 0; i_31_ < imagesWidth[index]; ++i_31_) {
						for (var211 = 0; var211 < imagesHeight[index]; ++var211) {
							byte i_33_ = this.alpha[index][i_31_ + var211 * imagesWidth[index]] = (byte) buffer
									.readByte();
							var20 |= i_33_ != -1;
						}
					}
				}

				if (!var20) {
					this.alpha[index] = null;
				}
			}
			this.bufferedImages.add(getBufferedImage(imagesWidth[index], imagesHeight[index], this.pixelsIndexes[index], this.alpha[index], this.usesAlpha[index]));
			loaded = true;
		}
	}

	@Override
	public OutputStream encode(OutputStream buffer) {
		
		this.biggestHeight = 0;
		this.biggestWidth = 0;

		if (this.palette == null) {
			this.generatePallete();
		}

		int container;
		int len$;
		int i$;
		for (container = 0; container < this.bufferedImages.size(); ++container) {
			len$ = 0;
			if (this.usesAlpha[container]) {
				len$ |= 2;
			}

			buffer.writeByte(len$);

			for (i$ = 0; i$ < this.pixelsIndexes[container].length; ++i$) {
				buffer.writeByte(this.pixelsIndexes[container][i$]);
			}

			if (this.usesAlpha[container]) {
				for (i$ = 0; i$ < this.alpha[container].length; ++i$) {
					buffer.writeByte(this.alpha[container][i$]);
				}
			}
		}

		for (container = 0; container < this.palette.length; ++container) {
			buffer.write24BitInt(this.palette[container]);
		}

		if (this.biggestWidth == 0 && this.biggestHeight == 0) {
			BufferedImage[] var7 = this.bufferedImages.toArray(new BufferedImage[this.bufferedImages.size()]);
			len$ = var7.length;

			for (i$ = 0; i$ < len$; ++i$) {
				BufferedImage image = var7[i$];
				if (image.getWidth() > this.biggestWidth) {
					this.biggestWidth = image.getWidth();
				}

				if (image.getHeight() > this.biggestHeight) {
					this.biggestHeight = image.getHeight();
				}
			}
		}

		buffer.writeShort(this.biggestWidth);
		buffer.writeShort(this.biggestHeight);
		buffer.writeByte(this.palette.length - 1);

		for (container = 0; container < this.bufferedImages.size(); ++container) {
			buffer.writeShort(this.bufferedImages.get(container).getMinX());
		}

		for (container = 0; container < this.bufferedImages.size(); ++container) {
			buffer.writeShort(this.bufferedImages.get(container).getMinY());
		}

		for (container = 0; container < this.bufferedImages.size(); ++container) {
			buffer.writeShort(this.bufferedImages.get(container).getWidth());
		}

		for (container = 0; container < this.bufferedImages.size(); ++container) {
			buffer.writeShort(this.bufferedImages.get(container).getHeight());
		}

		buffer.writeShort(this.bufferedImages.size());
		
		return buffer;
	}
	
	@Override
	public String toString() {
		return "Sprite - " + this.id;
	}

	private static Map<Field, Integer> fieldPriorities;

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

	@Override
	public List<Image> getImages() {
		return requestLoad().toSpriteFrames().stream().filter(Objects::nonNull).map(SpriteFrame::getImage).collect(Collectors.toList());
	}
}
