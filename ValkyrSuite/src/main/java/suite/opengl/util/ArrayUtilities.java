package suite.opengl.util;

/**
 * @author ReverendDread on 12/9/2019
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
public class ArrayUtilities {

    public static int[] copyOf(int[] original) {
        if(original == null)
            return null;
        int[] copy = new int[original.length];
        System.arraycopy(original, 0, copy, 0, original.length);
        return copy;
    }

    public static byte[] copyOf(byte[] original) {
        if(original == null)
            return null;
        byte[] copy = new byte[original.length];
        System.arraycopy(original, 0, copy, 0, original.length);
        return copy;
    }

    public static short[] copyOf(short[] original) {
        if(original == null)
            return null;
        short[] copy = new short[original.length];
        System.arraycopy(original, 0, copy, 0, original.length);
        return copy;
    }

    public static long[] copyOf(long[] original) {
        if(original == null)
            return null;
        long[] copy = new long[original.length];
        System.arraycopy(original, 0, copy, 0, original.length);
        return copy;
    }

    public static float[] copyOf(float[] original) {
        if(original == null)
            return null;
        float[] copy = new float[original.length];
        System.arraycopy(original, 0, copy, 0, original.length);
        return copy;
    }

    public static int[][] copyOf(int[][] original) {
        if(original == null)
            return null;
        int[][] copy = new int[original.length][];
        for (int i = 0; i < copy.length; i++) {
            copy[i] = copyOf(original[i]);
        }
        return copy;
    }

}
