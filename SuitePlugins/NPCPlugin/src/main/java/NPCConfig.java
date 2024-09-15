import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.common.collect.Maps;
import javafx.util.Pair;
import store.io.impl.InputStream;
import store.io.impl.OutputStream;
import suite.annotation.FixedSize;
import suite.annotation.OrderType;
import store.plugin.extension.ConfigExtensionBase;
import store.utilities.ReflectionUtils;

/**
 * @author ReverendDread
 * Sep 19, 2019
 */
public class NPCConfig extends ConfigExtensionBase {

	private boolean rev210HeadIcons;

	public void configureForRevision(int rev) {
		this.rev210HeadIcons = rev >= NPCLoader.REV_210_NPC_ARCHIVE_KEY;
	}

	@Override
	public void decode(int opcode, InputStream buffer) {
		int length;
		int index;
		if (opcode == 1) {
			length = buffer.readUnsignedByte();
			models = new int[length];
			for (index = 0; index < length; ++index) {
				models[index] = buffer.readUnsignedShort();
			}
		} else if (opcode == 2) {
			name = buffer.readString();
		} else if (opcode == 12) {
			tileSpacesOccupied = buffer.readUnsignedByte();
		} else if (opcode == 13) {
			standingAnimation = buffer.readUnsignedShort();
		} else if (opcode == 14) {
			walkAnimation = buffer.readUnsignedShort();
		} else if (opcode == 15) {
			idleRotateLeftAnimation = buffer.readUnsignedShort();
		} else if (opcode == 16) {
			idleRotateRightAnimation = buffer.readUnsignedShort();
		} else if (opcode == 17) {
			walkAnimation = buffer.readUnsignedShort();
			rotate180Animation = buffer.readUnsignedShort();
			rotate90RightAnimation = buffer.readUnsignedShort();
			rotate90LeftAnimation = buffer.readUnsignedShort();
		} else if (opcode == 18) {
			category = buffer.readUnsignedShort();
		} else if (opcode >= 30 && opcode < 35) {
			options[opcode - 30] = buffer.readString();
			if (options[opcode - 30].equalsIgnoreCase("Hidden")) {
				options[opcode - 30] = null;
			}
		} else if (opcode == 40) {
			length = buffer.readUnsignedByte();
			recolorToFind = new int[length];
			recolorToReplace = new int[length];
			for (index = 0; index < length; ++index) {
				recolorToFind[index] = buffer.readUnsignedShort();
				recolorToReplace[index] = buffer.readUnsignedShort();
			}
		} else if (opcode == 41) {
			length = buffer.readUnsignedByte();
			retextureToFind = new int[length];
			retextureToReplace = new int[length];
			for (index = 0; index < length; ++index) {
				retextureToFind[index] = buffer.readUnsignedShort();
				retextureToReplace[index] = buffer.readUnsignedShort();
			}
		} else if (opcode == 60) {
			length = buffer.readUnsignedByte();
			chatheadModels = new int[length];
			for (index = 0; index < length; ++index) {
				chatheadModels[index] = buffer.readUnsignedShort();
			}
		} else if (opcode == 74) {
			stats[0] = buffer.readUnsignedShort();
		} else if (opcode == 75) {
			stats[1] = buffer.readUnsignedShort();
		} else if (opcode == 76) {
			stats[2] = buffer.readUnsignedShort();
		} else if (opcode == 77) {
			stats[3] = buffer.readUnsignedShort();
		} else if (opcode == 78) {
			stats[4] = buffer.readUnsignedShort();
		} else if (opcode == 79) {
			stats[5] = buffer.readUnsignedShort();
		} else if (opcode == 93) {
			isMinimapVisible = false;
		} else if (opcode == 95) {
			combatLevel = buffer.readUnsignedShort();
		} else if (opcode == 97) {
			resizeX = buffer.readUnsignedShort();
		} else if (opcode == 98) {
			resizeY = buffer.readUnsignedShort();
		} else if (opcode == 99) {
			hasRenderPriority = true;
		} else if (opcode == 100) {
			ambient = buffer.readByte();
		} else if (opcode == 101) {
			contrast = buffer.readByte();
		} else if (opcode == 102) {
			if (rev210HeadIcons) {
				int bitfield = buffer.readUnsignedByte();
				int len = 0;
				for (int var5 = bitfield; var5 != 0; var5 >>= 1) {
					++len;
				}
				headIcons = new int[len];
				headIconsSpriteIndex = new int[len];
				for (int i = 0; i < len; i++) {
					if ((bitfield & 1 << i) == 0) {
						headIcons[i] = -1;
						headIconsSpriteIndex[i] = -1;
					} else {
						headIcons[i] = buffer.readBigSmart();
						headIconsSpriteIndex[i] = buffer.readSmartNS();
					}
				}
			} else {
				headIcons = new int[]{-1};
				headIconsSpriteIndex = new int[]{buffer.readUnsignedShort()};
			}
		} else if (opcode == 103) {
			rotationSpeed = buffer.readUnsignedShort();
		} else if (opcode == 106) {
			varbitIndex = buffer.readUnsignedShort();
			if (65535 == varbitIndex) {
				varbitIndex = -1;
			}
			varpIndex = buffer.readUnsignedShort();
			if (65535 == varpIndex) {
				varpIndex = -1;
			}
			length = buffer.readUnsignedByte();
			configs = new int[length + 2];
			for (index = 0; index <= length; ++index) {
				configs[index] = buffer.readUnsignedShort();
				if (configs[index] == 65535) {
					configs[index] = -1;
				}
			}
			configs[length + 1] = -1;
		} else if (opcode == 107) {
			interactable = false;
		} else if (opcode == 109) {
			rotationFlag = false;
		} else if (opcode == 111) {
			isFollower = true;
		} else if (opcode == 114) {
			runAnimation = buffer.readUnsignedShort();
		} else if (opcode == 115) {
			runAnimation = buffer.readUnsignedShort();
			runRotate180Animation = buffer.readUnsignedShort();
			runRotateLeftAnimation = buffer.readUnsignedShort();
			runRotateRightAnimation = buffer.readUnsignedShort();
		} else if (opcode == 116) {
			crawlAnimation = buffer.readUnsignedShort();
		} else if (opcode == 117) {
			crawlAnimation = buffer.readUnsignedShort();
			crawlRotate180Animation = buffer.readUnsignedShort();
			crawlRotateLeftAnimation = buffer.readUnsignedShort();
			crawlRotateRightAnimation = buffer.readUnsignedShort();
		} else if (opcode == 118) {
			varbitIndex = buffer.readUnsignedShort();
			if (varbitIndex == 65535) {
				varbitIndex = -1;
			}
			varpIndex = buffer.readUnsignedShort();
			if (varpIndex == 65535) {
				varpIndex = -1;
			}
			int var = buffer.readUnsignedShort();
			if (var == 65535) {
				var = -1;
			}
			length = buffer.readUnsignedByte();
			configs = new int[length + 2];
			for (index = 0; index <= length; ++index) {
				configs[index] = buffer.readUnsignedShort();
				if (configs[index] == 65535) {
					configs[index] = -1;
				}
			}
			configs[length + 1] = var;
		} else if (opcode == 122) {
			isFollower = true;
		} else if (opcode == 123) {
			lowPriorityFollowerOps = true;
		} else if (opcode == 124) {
			height = buffer.readUnsignedShort();
		} else if (opcode == 249) {
			length = buffer.readUnsignedByte();
			params = new HashMap<>(length);
			for (int i = 0; i < length; i++) {
				boolean isString = buffer.readUnsignedByte() == 1;
				int key = buffer.read24BitInt();
				Object value;
				if (isString) {
					value = buffer.readString();
				}
				else {
					value = buffer.readInt();
				}
				params.put(key, value);
			}
		}
	}

	@Override
	public OutputStream encode(OutputStream buffer) {
		
		if (models != null) {
			buffer.writeByte(1);
			buffer.writeByte(models.length);
            for (int model : models) {
                buffer.writeShort(model);
            }
		}
		
		if (!name.equals("null")) {
			buffer.writeByte(2);
			buffer.writeString(name);
		}
		
		if (tileSpacesOccupied > 1) {
			buffer.writeByte(12);
			buffer.writeByte(tileSpacesOccupied);
		}
		
		if (standingAnimation > -1) {
			buffer.writeByte(13);
			buffer.writeShort(standingAnimation);
		}
		
		if (walkAnimation > -1) {
			buffer.writeByte(14);
			buffer.writeShort(walkAnimation);
		}
		
		if (idleRotateLeftAnimation > -1) {
			buffer.writeByte(15);
			buffer.writeShort(idleRotateLeftAnimation);
		}
		
		if (idleRotateRightAnimation > -1) {
			buffer.writeByte(16);
			buffer.writeShort(idleRotateRightAnimation);
		}
		
		if (walkAnimation > -1 && rotate180Animation > -1 && rotate90LeftAnimation > -1 && rotate90RightAnimation > -1) {
			buffer.writeByte(17);
			buffer.writeShort(walkAnimation);
			buffer.writeShort(rotate180Animation);
			buffer.writeShort(rotate90RightAnimation);
			buffer.writeShort(rotate90LeftAnimation);
		}

		if (category > 0) {
			buffer.writeByte(18);
			buffer.writeShort(category);
		}
		
		if (options != null) {
			for (int index = 0; index < options.length; index++) {
				if(options[index] == null || options[index].isEmpty())
					continue;
				buffer.writeByte(index + 30);
				buffer.writeString(options[index]);
			}
		}
		
		if (recolorToFind != null && recolorToReplace != null) {
			buffer.writeByte(40);
			int length = Math.min(recolorToFind.length, recolorToReplace.length);
			buffer.writeByte(length);
			for (int index = 0; index < length; index++) {
				buffer.writeShort(recolorToFind[index]);
				buffer.writeShort(recolorToReplace[index]);
			}
		}
		
		if (retextureToFind != null && retextureToReplace != null) {
			buffer.writeByte(41);
			int length = Math.min(retextureToFind.length, retextureToReplace.length);
			buffer.writeByte(length);
			for (int index = 0; index < length; index++) {
				buffer.writeShort(retextureToFind[index]);
				buffer.writeShort(retextureToReplace[index]);
			}
		}
		
		if (chatheadModels != null) {
			buffer.writeByte(60);
			buffer.writeByte(chatheadModels.length);
            for (int chatheadModel : chatheadModels) {
                buffer.writeShort(chatheadModel);
            }
		}

		if (stats[0] > 1) {
			buffer.writeByte(74);
			buffer.writeShort(stats[0]);
		}

		if (stats[1] > 1) {
			buffer.writeByte(75);
			buffer.writeShort(stats[1]);
		}

		if (stats[2] > 1) {
			buffer.writeByte(76);
			buffer.writeShort(stats[2]);
		}

		if (stats[3] > 1) {
			buffer.writeByte(77);
			buffer.writeShort(stats[3]);
		}

		if (stats[4] > 1) {
			buffer.writeByte(78);
			buffer.writeShort(stats[4]);
		}

		if (stats[5] > 1) {
			buffer.writeByte(79);
			buffer.writeShort(stats[5]);
		}
		
		if (!isMinimapVisible) {
			buffer.writeByte(93);
		}
		
		if (combatLevel > 0) {
			buffer.writeByte(95);
			buffer.writeShort(combatLevel);
		}
		
		if (resizeX != 128) {
			buffer.writeByte(97);
			buffer.writeShort(resizeX);
		}
		
		if (resizeY != 128) {
			buffer.writeByte(98);
			buffer.writeShort(resizeY);
		}
		
		if (hasRenderPriority) {
			buffer.writeByte(99);
		}
		
		if (ambient > 0) {
			buffer.writeByte(100);
			buffer.writeByte(ambient);
		}
		
		if (contrast > 0) {
			buffer.writeByte(101);
			buffer.writeByte(contrast);
		}

//		public static final int REV_210_NPC_ARCHIVE_REV = 1493;
//
//		private int defaultHeadIconArchive = -1;
//		private boolean rev210HeadIcons = true;
//
//		public NpcLoader configureForRevision(int rev)
//		{
//			this.rev210HeadIcons = rev >= NpcLoader.REV_210_NPC_ARCHIVE_REV;
//			return this;
//		}

//		else if (opcode == 102)
//		{
//			if (!rev210HeadIcons)
//			{
//				def.headIconArchiveIds = new int[]{defaultHeadIconArchive};
//				def.headIconSpriteIndex = new short[]{(short) stream.readUnsignedShort()};
//			}
//		}

		if (headIcons != null && headIconsSpriteIndex != null) {
			buffer.writeByte(102);
			int bitfield = 0;
			for (int i = 0; i < headIcons.length; i++) {
				if (headIcons[i] != -1 && headIconsSpriteIndex[i] != -1) {
					bitfield |= 1 << i;
				}
			}
			buffer.writeByte(bitfield);
			for (int i = 0; i < headIcons.length; i++) {
				if (headIcons[i] != -1 && headIconsSpriteIndex[i] != -1) {
					buffer.writeBigSmart(headIcons[i]);
					buffer.writeUnsignedSmart(headIconsSpriteIndex[i] - 1);
				}
			}
		}
		
		if (rotationSpeed != 32) {
			buffer.writeByte(103);
			buffer.writeShort(rotationSpeed);
		}
		
		if ((varbitIndex != -1 && varpIndex != -1) && (configs != null && configs.length > 0)) {	
			int length = configs.length;
			boolean hasTransforms = configs[configs.length - 1] != -1;
			buffer.writeByte(hasTransforms ? 118 : 106);
			buffer.writeShort(varbitIndex);
			buffer.writeShort(varpIndex);
			if (hasTransforms) {
				buffer.writeShort(configs[length - 1]);
			}
			buffer.writeByte(length - 2);
			for (int transform = 0; transform <= (length - 2); transform++) {
				buffer.writeShort(configs[transform]);
			}
		}
		
		if (!interactable) {
			buffer.writeByte(107);
		}
		
		if (!rotationFlag) {
			buffer.writeByte(109);
		}

		if (runAnimation != -1) {
			buffer.writeByte(114);
			buffer.writeShort(runAnimation);
		}

		if (runAnimation != -1 && runRotate180Animation != -1 && runRotateLeftAnimation != -1 && runRotateRightAnimation != -1) {
			buffer.writeByte(115);
			buffer.writeShort(runAnimation);
			buffer.writeShort(runRotate180Animation);
			buffer.writeShort(runRotateLeftAnimation);
			buffer.writeShort(runRotateRightAnimation);
		}

		if (crawlAnimation != -1) {
			buffer.writeByte(116);
			buffer.writeShort(crawlAnimation);
		}

		if (crawlAnimation != -1 && crawlRotate180Animation != -1 && crawlRotateLeftAnimation != -1 && crawlRotateRightAnimation != -1) {
			buffer.writeByte(117);
			buffer.writeShort(crawlAnimation);
			buffer.writeShort(crawlRotate180Animation);
			buffer.writeShort(crawlRotateLeftAnimation);
			buffer.writeShort(crawlRotateRightAnimation);
		}
		
		if (isFollower) {
			buffer.writeByte(122);
		}

		if (lowPriorityFollowerOps) {
			buffer.writeByte(123);
		}

		if (height > -1) {
			buffer.writeByte(124);
			buffer.writeShort(height);
		}
		
		if (Objects.nonNull(params)) {
			buffer.writeByte(249);
			buffer.writeByte(params.size());
			for (int key : params.keySet()) {
				Object value = params.get(key);
				buffer.writeByte(value instanceof String ? 1 : 0);
				buffer.write24BitInt(key);
				if (value instanceof String) {
					buffer.writeString((String) value);
				} else {
					buffer.writeInt((Integer) value);
				}
			}
		}
		
		buffer.writeByte(0);
		
		return buffer;
	}

	@Override
	public String toString() {
		return "[" + this.id + "] " + this.name;
	}

	@OrderType(priority = 1)
	public String name = "null";
	@OrderType(priority = 6)
	public int[] recolorToFind;
	public int rotationSpeed = 32;
	@OrderType(priority = 7)
	public int[] recolorToReplace;
	public int[] models;
	public int[] chatheadModels;
	public int standingAnimation = -1;
	public int idleRotateLeftAnimation = -1;
	public int tileSpacesOccupied = 1;
	public int walkAnimation = -1;
	@OrderType(priority = 8)
	public int[] retextureToReplace;
	public int rotate90RightAnimation = -1;
	public boolean rotationFlag = true;
	public int resizeX = 128;
	public int contrast = 0;
	public int rotate180Animation = -1;
	public int varbitIndex = -1;
	@OrderType(priority = 3)
	public String[] options = new String[5];
	@OrderType(priority = 5)
	public boolean isMinimapVisible = true;
	@OrderType(priority = 2)
	public int combatLevel = -1;
	public int rotate90LeftAnimation = -1;
	public int resizeY = 128;
	public boolean hasRenderPriority = false;
	public int ambient = 0;
	public int[] headIcons;
	public int[] headIconsSpriteIndex;
	public int[] configs;
	@OrderType(priority = 7)
	public int[] retextureToFind;
	public int varpIndex = -1;
	@OrderType(priority = 4)
	public boolean interactable = true;
	public int idleRotateRightAnimation = -1;
	public boolean isFollower = false;
	public int category;
	public int runAnimation = -1;
	public int runRotate180Animation = -1;
	public int runRotateLeftAnimation = -1;
	public int runRotateRightAnimation = -1;
	public int crawlAnimation = -1;
	public int crawlRotate180Animation = -1;
	public int crawlRotateLeftAnimation = -1;
	public int crawlRotateRightAnimation = -1;
	public boolean lowPriorityFollowerOps;
	public int height = -1;
	@FixedSize
	public int[] stats = { 1, 1, 1, 1, 1, 1 };
	public Map<Integer, Object> params = null;

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

}
