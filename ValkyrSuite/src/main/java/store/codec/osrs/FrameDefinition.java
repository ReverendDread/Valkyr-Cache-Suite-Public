/**
 * 
 */
package store.codec.osrs;

import store.io.impl.InputStream;
import store.io.impl.OutputStream;

/**
 * @author ReverendDread Sep 6, 2018
 */
public class FrameDefinition {

	int[] translator_x;
	static int[] alter_x = new int[500];
	static int[] alter_y = new int[500];
	BaseDefinition skin = null;
	static int[] instructions = new int[500];
	int stepCount = -1;
	int[] opCodeTable;
	int[] translator_y;
	static int[] alter_z = new int[500];
	int[] translator_z;
	boolean hasAlpha = false;
	int animationCount;
	int[] settings;

	public FrameDefinition(byte[] data, BaseDefinition skin) {
		this.skin = skin;
		InputStream settings = new InputStream(data);
		InputStream stream = new InputStream(data);
		settings.skip(2);
		animationCount = settings.readUnsignedByte();
		int var10 = -1;
		int stepCount = 0;
		stream.skip(settings.getPosition() + animationCount);
		this.settings = new int[animationCount];
		for (int animation = 0; animation < animationCount; animation++) {
			int setting = settings.readUnsignedByte();
			this.settings[animation] = setting;
			if (setting > 0) {
				if (this.skin.transformations[animation] != 0) { // set reference point
					for (int var4 = animation - 1; var4 > var10; --var4) {
						if (this.skin.transformations[var4] == 0) {
							instructions[stepCount] = var4;
							alter_x[stepCount] = 0;
							alter_y[stepCount] = 0;
							alter_z[stepCount] = 0;
							++stepCount;
							break;
						}
					}
				}

				instructions[stepCount] = animation;

				short var11 = 0;
				if (this.skin.transformations[animation] == 3) {
					var11 = 128;
				}

				if ((setting & 1) != 0) {
					alter_x[stepCount] = stream.readSmart();
				} else {
					alter_x[stepCount] = var11;
				}

				if ((setting & 2) != 0) {
					alter_y[stepCount] = stream.readSmart();
				} else {
					alter_y[stepCount] = var11;
				}

				if ((setting & 4) != 0) {
					alter_z[stepCount] = stream.readSmart();
				} else {
					alter_z[stepCount] = var11;
				}

				var10 = animation;
				stepCount++;

				if (this.skin.transformations[animation] == 5) {
					hasAlpha = true;
				}

			}
		}

		if (stream.getPosition() != data.length) {
			System.out.println(
					"FrameDefinition - Remaining: " + stream.getRemaining() + ", Position: " + stream.getPosition());
		} else {
			this.stepCount = stepCount;
			this.opCodeTable = new int[stepCount];
			this.translator_x = new int[stepCount];
			this.translator_y = new int[stepCount];
			this.translator_z = new int[stepCount];
			for (int index = 0; index < stepCount; ++index) {
				this.opCodeTable[index] = instructions[index];
				this.translator_x[index] = alter_x[index];
				this.translator_y[index] = alter_y[index];
				this.translator_z[index] = alter_z[index];
			}
		}

	}

	public byte[] encode() {
		OutputStream stream = new OutputStream();

		// Write anon data
		stream.writeByte(0);

		// Write the base id for the frame.
		stream.writeShort(skin.id);

		// Write anon data
		stream.writeByte(0);
		stream.writeByte(0);

		// Write how many anims are in the frame.
		stream.writeByte(animationCount);

		// Loop through anims and write respective setting data.
		for (int animation = 0; animation < animationCount; animation++) {
			int setting = settings[animation];
			stream.writeByte(setting); // Write axis
			if ((setting & 1) != 0) {
				stream.writeSmart(alter_x[animation]); // Write value
			} else if ((setting & 2) != 0) {
				stream.writeSmart(alter_y[animation]); // Write value
			} else if ((setting & 4) != 0) {
				stream.writeSmart(alter_z[animation]); // Write value
			}
		}

		return stream.flip();
	}

}
