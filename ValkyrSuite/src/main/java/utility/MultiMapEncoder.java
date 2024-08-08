package utility;


import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class MultiMapEncoder {


	@Data
	public static class Chunk {

		public int tileMapId = -1;
		public int objectMapId = -1;
		public String objectMapName;
		public String tileMapName;
		public byte[] tileMapData;

		public byte[] objectMapData;

		private int mapObjectCount;

		public int offsetX, offsetY;
	}

	public static byte[] encode(List<Chunk> chunks) {
		ByteBuffer buffer = ByteBuffer.allocate(5_000_000);//5mb
		buffer.putInt(chunks.size());
		for(Chunk chunk : chunks) {
				byte[] objectMap = chunk.objectMapData;
				byte[] tileMap = chunk.tileMapData;


				buffer.putInt(chunk.objectMapId);
				buffer.putInt(chunk.tileMapId);

				buffer.putInt(chunk.offsetX / 64);
				buffer.putInt(chunk.offsetY / 64);

				buffer.putInt(objectMap.length);
				buffer.put(objectMap);

				buffer.putInt(tileMap.length);
				buffer.put(tileMap);
		}

		return Arrays.copyOf(buffer.array(), buffer.position());
	}

	public static Vector3f getSize(byte[] encoded) {
		ByteBuffer buffer = ByteBuffer.wrap(encoded);
		int size = buffer.getInt();

		int maximumX = 0;
		int maximumY = 0;
		for(int i = 0;i<size;i++) {
			buffer.getInt();
			buffer.getInt();

			int positionX = buffer.getInt();
			int positionY = buffer.getInt();

			if(positionX > maximumX)
				maximumX = positionX;
			if(positionY > maximumY)
				maximumY = positionY;

			buffer.get(new byte[buffer.getInt()]);
			buffer.get(new byte[buffer.getInt()]);

		}

		return new Vector3f((float) maximumX, (float) maximumY, 0f);
	}
	public static List<Chunk> decode(byte[] encoded){
		List<Chunk> chunks = Lists.newArrayList();
		ByteBuffer buffer = ByteBuffer.wrap(encoded);
		int size = buffer.getInt();

		for(int i = 0;i<size;i++) {
			int objectMapId = buffer.getInt();
			int tileMapId = buffer.getInt();

			int positionX = buffer.getInt();
			int positionY = buffer.getInt();

			int objLen = buffer.getInt();
			byte[] objData = new byte[objLen];
			buffer.get(objData);

			int landscapeLen = buffer.getInt();
			byte[] landscapeData = new byte[landscapeLen];
			buffer.get(landscapeData);

			Chunk chunk = new Chunk();

			chunk.offsetX = positionX;
			chunk.offsetY = positionY;

			chunk.objectMapData = objData;
			chunk.tileMapData = landscapeData;
			chunk.objectMapId = objectMapId;
			chunk.tileMapId = tileMapId;

			chunks.add(chunk);
		}
		return chunks;
	}

}
