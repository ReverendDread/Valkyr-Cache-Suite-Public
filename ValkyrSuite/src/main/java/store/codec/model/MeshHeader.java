/**
 * 
 */
package store.codec.model;

import lombok.Data;

/**
 * @author ReverendDread
 * Jun 17, 2019
 */
@Data
public class MeshHeader {

	private byte[] data;
	private int vertices;
	private int faceCount;
	private int texturedFaceCount;
	
	//Buffer offsets
	private int xDataOffset, yDataOffset, zDataOffset;
	private int vertexDirectionOffset;
	private int faceTypeOffset;
	private int facePriorityOffset;
	private int faceSkinOffset;
	private int colourDataOffset;
	private int texturePointerOffset;
	private int vertexSkinOffset;
	private int vertexBoneOffset;
	private int faceDataOffset;
	private int faceAlphaOffset;
	private int uvMapFaceOffset;
	private int faceBoneOffset;
	
}
