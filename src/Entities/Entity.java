package Entities;

import Model.TexturedModel;
import Terrains.Terrain;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public abstract class Entity {

    private int ID;
    protected Vector3f position;
    protected float rx, ry, rz, scale;
    private TexturedModel texture;
    protected Vector2f grid;

    public Entity(TexturedModel texture) {
        this.texture = texture;
        this.position = new Vector3f(0, 0, 0);
        this.rx = 0;
        this.ry = 0;
        this.rz = 0;
        this.scale = 1;
        grid = new Vector2f(0, 0);
        updateGrid();
    }

    public Entity(Vector3f position, float rx, float ry, float rz, float scale, TexturedModel texture, int ID) {
        this.position = position;
        if (this.position == null) {
            this.position = new Vector3f(0, 0, 0);
        }
        grid = new Vector2f(0, 0);
        updateGrid();
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
        this.scale = scale;
        this.texture = texture;
        this.ID = ID;
    }

    public abstract void update(Terrain terrain);

    public TexturedModel getTexturedModel() {
        return texture;
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

    public float getRX() {
        return rx;
    }

    public float getRY() {
        return ry;
    }

    public float getRZ() {
        return rz;
    }

    public float getScale() {
        return scale;
    }

    public void changeDirection(int angle) {
        ry = angle;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRx(float rx) {
        this.rx = rx;
    }

    public void setRy(float ry) {
        this.ry = ry;
    }

    public void setRZ(float rz) {
        this.rz = rz;
    }

    public void setXPos(float x) {
        new Vector3f(x, position.y, position.z);
    }

    public void setYPos(float y) {
        new Vector3f(position.x, y, position.z);
    }

    public void setZPos(float z) {
        new Vector3f(position.x, position.y, z);
    }

    public int getID() {
        return ID;
    }

    protected void updateGrid() {
        int x = (int) position.x;
        int z = (int) position.z;
        grid.setX((x - (x % 50)) / 50);
        grid.setY((z - (z % 50)) / 50);
    }

    public Vector2f getGrid() {
        return grid;
    }
}
