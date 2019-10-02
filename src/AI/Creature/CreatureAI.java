package AI.Creature;

import Terrains.Terrain;
import org.lwjgl.util.vector.Vector3f;

public abstract class CreatureAI {

    protected static final int IDLE = 0, WALK = 1, ROTATE = 3, EAT = 4, POINT_OF_FOCUS = 5;
    protected float gravity = -1f;
    protected boolean inAir = false;
    protected Vector3f position;
    protected float rx, ry, rz;
    protected float upwardSpeed = 0;

    public CreatureAI(Vector3f position, float rx, float ry, float rz) {
        this.position = position;
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
    }

    public abstract void update(Vector3f position, Terrain terrain);


    public boolean isInAir() {
        return inAir;
    }

    public void setInAir(boolean inAir) {
        this.inAir = inAir;
    }

    public void increasePosition(float dx, float dy, float dz) {
        position.x += dx;
        position.y += dy;
        position.z += dz;
    }

    public void increaseRotation(float dx, float dy, float dz) {
        rx += dx;
        ry += dy;
        rz += dz;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getRx() {
        return rx;
    }

    public float getRy() {
        return ry;
    }

    public float getRz() {
        return rz;
    }
}
