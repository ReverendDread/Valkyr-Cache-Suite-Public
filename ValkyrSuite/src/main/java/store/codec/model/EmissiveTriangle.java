package store.codec.model;

import lombok.Getter;

@Getter
public class EmissiveTriangle {

	public int id;
	public int faceX;
	public int faceY;
	public int faceZ;
	public int type;
	public byte priority;

	public EmissiveTriangle(int id, int type, int faceX, int faceY, int faceZ, byte priority) {
		this.id = id;
		this.type = type;
		this.faceX = faceX;
		this.faceY = faceY;
		this.faceZ = faceZ;
		this.priority = priority;
	}

}
