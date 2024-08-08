//package misc;
//
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//public class GameObject extends Tile {
//
//	private int id;
//	private int type;
//	private int rotation;
//
//	public GameObject(int id, int type, int rotation, Tile tile) {
//		super(tile.getX(), tile.getY(), tile.getPlane());
//		this.id = id;
//		this.type = type;
//		this.rotation = rotation;
//	}
//
//	public GameObject(int id, int type, int rotation, int x, int y, int plane) {
//		super(x, y, plane);
//		this.id = id;
//		this.type = type;
//		this.rotation = rotation;
//	}
//
//	public GameObject(int id, int type, int rotation, int x, int y, int plane, int life) {
//		super(x, y, plane);
//		this.id = id;
//		this.type = type;
//		this.rotation = rotation;
//	}
//
//	public GameObject(GameObject object) {
//		super(object.getX(), object.getY(), object.getPlane());
//		this.id = object.id;
//		this.type = object.type;
//		this.rotation = object.rotation;
//	}
//
//}
