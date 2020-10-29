package store.codec.model;

/**
 * @author ReverendDread on 7/26/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
public class Particle {

    public float[][] coordinates;
    public int[] verticesZ;
    public int[] verticesX;
    public int[] verticesY;

    public Particle(int[] verticesX, int[] verticesY, int[] verticesZ, float[][] coordinates) {
        this.verticesX = verticesX;
        this.verticesY = verticesY;
        this.verticesZ = verticesZ;
        this.coordinates = coordinates;
    }

}
