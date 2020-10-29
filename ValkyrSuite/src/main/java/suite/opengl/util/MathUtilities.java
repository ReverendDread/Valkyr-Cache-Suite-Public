package suite.opengl.util;

/**
 * @author ReverendDread on 12/9/2019
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
public class MathUtilities {

    public static final int[] SINE, COSINE;

    static {
        SINE = new int[2048];
        COSINE = new int[2048];
        {
            for (int i = 0; i < 2048; i++) {
                SINE[i] = (int) (65536D * Math.sin((double) i * 0.0030679614999999999D));
                COSINE[i] = (int) (65536D * Math.cos((double) i * 0.0030679614999999999D));
            }
        }
    }

}
