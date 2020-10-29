/**
 * 
 */


import lombok.Data;

/**
 * @author ReverendDread Apr 10, 2019
 */
@Data
public class SpriteDefinition {

	private int id;
	private int frame;
	private int offsetX;
	private int offsetY;
	private int width;
	private int height;
	private int[] pixels;
	private int maxWidth;
	private int maxHeight;

	public transient byte[] pixelIdx;
	public transient int[] palette;

	public void normalize() {
		if (this.width != this.maxWidth || this.height != this.maxHeight) {
			byte[] var1 = new byte[this.maxWidth * this.maxHeight];
			int var2 = 0;
			for (int var3 = 0; var3 < this.height; ++var3) {
				for (int var4 = 0; var4 < this.width; ++var4) {
					var1[var4 + (var3 + this.offsetY) * this.maxWidth + this.offsetX] = this.pixelIdx[var2++];
				}
			}
			this.pixelIdx = var1;
			this.width = this.maxWidth;
			this.height = this.maxHeight;
			this.offsetX = 0;
			this.offsetY = 0;
		}
	}

}
