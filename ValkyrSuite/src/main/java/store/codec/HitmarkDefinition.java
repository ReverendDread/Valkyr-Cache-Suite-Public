/**
 * 
 */
package store.codec;

import store.CacheLibrary;
import store.io.impl.InputStream;
import store.io.impl.OutputStream;

/**
 * @author ReverendDread Jan 14, 2019
 */
public class HitmarkDefinition implements AbstractDefinition, Cloneable {

	public int id;
	public int fontId;
	public int fontColor;
	public int attackStyle;
	public int leftSprite;
	public int middleSprite;
	public int rightSprite;
	public int horizontalSpeed;
	public String text;
	public int fadeSpeed;
	public int verticalSpeed;
	public int anInt3861;
	public int anInt3863;
	public int verticalTextOffset;

	public HitmarkDefinition(int id) {
		this.id = id;
		fontColor = 16777215;
		fadeSpeed = 70;
		attackStyle = -1;
		middleSprite = -1;
		leftSprite = -1;
		rightSprite = -1;
		horizontalSpeed = 0;
		verticalSpeed = 0;
		anInt3861 = -1;
		text = "";
		anInt3863 = -1;
		verticalTextOffset = 0;
	}

	/* (non-Javadoc)
	 * @see com.alex.definition.AbstractDefinition#decode(com.alex.io.InputStream)
	 */
	@Override
	public void decode(InputStream stream) {
		while (true) {

			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;

			if (opcode == 1) {
				fontId = stream.readBigSmart();
			} else if (opcode == 2) {
				fontColor = stream.read24BitInt();
			} else if (opcode == 3) {
				attackStyle = stream.readBigSmart();
			} else if (opcode == 4) {
				leftSprite = stream.readBigSmart();
			} else if (opcode == 5) {
				middleSprite = stream.readBigSmart();
			} else if (opcode == 6) {
				rightSprite = stream.readBigSmart();
			} else if (opcode == 7) {
				horizontalSpeed = stream.readShort();
			} else if (opcode == 8) {
				text = stream.readJagString();
			} else if (opcode == 9) {
				fadeSpeed = stream.readUnsignedShort();
			} else if (opcode == 10) {
				verticalSpeed = stream.readShort();
			} else if (opcode == 11) {
				anInt3861 = 0;
			} else if (opcode == 12) {
				anInt3863 = stream.readUnsignedByte();
			} else if (opcode == 13) {
				verticalTextOffset = stream.readShort();
			} else if (opcode == 14) {
				anInt3861 = stream.readUnsignedShort();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.alex.definition.AbstractDefinition#encode()
	 */
	@Override
	public byte[] encode() {

		OutputStream stream = new OutputStream();

		if (fontId != -1) {
			stream.writeByte(1);
			stream.writeBigSmart(fontId);
		}

		if (fontId != 16777215) {
			stream.writeByte(2);
			stream.write24BitInt(fontColor);
		}

		if (attackStyle != -1) {
			stream.writeByte(3);
			stream.writeBigSmart(attackStyle);
		}

		if (leftSprite != -1) {
			stream.writeByte(4);
			stream.writeBigSmart(leftSprite);
		}

		if (middleSprite != -1) {
			stream.writeByte(5);
			stream.writeBigSmart(middleSprite);
		}

		if (rightSprite != -1) {
			stream.writeByte(6);
			stream.writeBigSmart(rightSprite);
		}

		if (horizontalSpeed != -1) {
			stream.writeByte(7);
			stream.writeShort(horizontalSpeed);
		}

		if (!text.isEmpty()) {
			stream.writeByte(8);
			stream.writeJagString(text);
		}

		if (fadeSpeed != -1) {
			stream.writeByte(9);
			stream.writeShort(fadeSpeed);
		}

		if (verticalSpeed != -1) {
			stream.writeByte(10);
			stream.writeShort(verticalSpeed);
		}

		if (anInt3861 == 0) {
			stream.writeByte(11);
		} else {
			stream.writeByte(14);
			stream.writeShort(anInt3861);
		}

		if (anInt3863 != -1) {
			stream.writeByte(12);
			stream.writeByte(anInt3863);
		}

		if (verticalTextOffset != 0) {
			stream.writeByte(13);
			stream.writeShort(verticalTextOffset);
		}

		stream.writeByte(0);
		return stream.flip();

	}

	/* (non-Javadoc)
	 * @see com.alex.definition.AbstractDefinition#save(com.alex.store.FileStore)
	 */
	@Override
	public boolean save(CacheLibrary cache) {
		cache.getIndex(2).addArchive(46).addFile(id, encode());
		return cache.getIndex(2).update();
	}

	@Override
	public String toString() {
		return "" + this.id;
	}

}
