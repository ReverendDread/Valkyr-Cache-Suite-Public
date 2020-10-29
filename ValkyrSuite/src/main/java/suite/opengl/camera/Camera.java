package suite.opengl.camera;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.util.vector.Vector3f;

/**
 * @author ReverendDread on 7/26/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
@Getter @Setter
public class Camera {

    public Vector3f position;
    public Vector3f rotation;
    public Vector3f lastPosition;

    public Camera() {
        this.position = new Vector3f(0, 0, 0);
        this.rotation = new Vector3f(0, 0, 0);
    }

    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public void position(float x, float y, float z) {
        //TODO limit camera movement so it doesn't go below
        System.out.println("position x " + x + ", y " + y + ", z " + z);
        position.x = y;
        position.y = x;
        position.z = z;
    }

    public void rotate(float x, float y, float z) {
        System.out.println("rotate x " + x + ", y " + y + ", z " + z);
        rotation.x += x;
        rotation.y += y;
        rotation.z += z;
    }

}
