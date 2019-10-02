package Camera;

import org.lwjgl.util.vector.Vector3f;

public class Point {

    private Vector3f position;
    private float rx, ry, rz;

    public Point(Vector3f position, float rx, float ry, float rz) {
        this.position = position;
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosX(float x) {
        position = new Vector3f(x, position.y, position.z);
    }

    public void setPosZ(float z) {
        position = new Vector3f(position.x, position.y, z);
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRx() {
        return rx;
    }

    public float getRy() {
        return ry;
    }

    public void setRy(float ry) {
        this.ry = ry;
    }
}
