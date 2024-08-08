/**
 * 
 */


import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a sub-sprite frame image.
 * 
 * @author ReverendDread Sep 11, 2018
 */
@Getter @Setter
public class SpriteFrame {

	private int id;
	private Image image;

	/**
	 * The SpriteFrame constructor.
	 * 
	 * @param id    the id of the frame.
	 * @param image the image assosicated with the frame.
	 */
	public SpriteFrame(int id, Image image) {
		this.id = id;
		this.image = image;
	}

	@Override
	public String toString() {
		return "Frame - " + id;
	}

}
