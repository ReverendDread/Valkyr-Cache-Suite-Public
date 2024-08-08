/**
 * 
 */
package misc;

/**
 * Created at: Apr 15, 2017 11:09:48 PM
 * 
 * @author Walied-Yassen
 */
public class FaceBillboard {


    /**
     * The billboard ID.
     */
    private int id;


    /**
     * The billboard face.
     */
    private int face;


    /**
     * The billboard depth.
     */
    private int skin;


    /**
     * The billboard distance.
     */
    private int distance;


    /**
     * Constructs a new {@link FaceBillboard} object instance.
     * 
     * @param id
     *            the billboard ID.
     * @param face
     *            the billboard face.
     * @param skin
     *            the billboard skin.
     * @param distance
     *            the billboard distance.
     */
    public FaceBillboard(int id, int face, int skin, int distance) {
        this.id = id;
        this.face = face;
        this.skin = skin;
        this.distance = distance;
    }


    /**
     * Gets the billboard id.
     * 
     * @return the billboard id.
     */
    public int getId() {
        return id;
    }


    /**
     * Sets the billboard id.
     * 
     * @param id
     *            the new id to set.
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Gets the billboard face.
     * 
     * @return the billboard face.
     */
    public int getFace() {
        return face;
    }


    /**
     * Sets the billboard face.
     * 
     * @param face
     *            the new face to set.
     */
    public void setFace(int face) {
        this.face = face;
    }


    /**
     * Gets the billboard depth.
     * 
     * @return the billboard depth.
     */
    public int getDepth() {
        return skin;
    }


    /**
     * Sets the billboard depth.
     * 
     * @param depth
     *            the new depth to set.
     */
    public void setDepth(int depth) {
        this.skin = depth;
    }


    /**
     * Gets the billboard distance.
     * 
     * @return the billboard distance.
     */
    public int getDistance() {
        return distance;
    }


    /**
     * Sets the billboard distance.
     * 
     * @param distance
     *            the new distance to set.
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }


}
