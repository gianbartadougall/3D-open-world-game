package Camera;

import Entities.Entity;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private final float ROTATE_SPEED = 0.3f;
    private Vector3f position;
    private float rx, ry, rz;
    private float zoomLevel = 140, angleAroundPlayer = 0;

    CameraUtils utils;
    private Entity entity;
    private CameraAI cameraAI;

    public Camera(Entity entity) {
        this.entity = entity;
        utils = new CameraUtils(this, 0.05f, 140, 26, 0.2f, 1, 90);
        this.position = new Vector3f(entity.getPosition().x, entity.getPosition().y, entity.getPosition().z+30);
        rx = 10;
        ry = 0;
        rz = 0;
        cameraAI = new CameraAI(this, entity, new Vector3f(400, 0, 400), 0, 0, 0);
        //cameraAI.setToAutomatic(CameraAI.AEROPLANE);
    }

    public void update() {
        if (cameraAI.automatic) {
            cameraAI.update();
            return;
        }
        zoomLevel = utils.calculateCamerasZoom(zoomLevel);
        calculateAngle();
        rx = utils.calculateCamerasPitch();
        calculateCameraPosition(calculateHorizontalDistance(zoomLevel),
                                calculateVerticalDistance(zoomLevel));
    }

    private void calculateAngle() {
        if (Mouse.isButtonDown(0)) {
            angleAroundPlayer -= Mouse.getDX() * ROTATE_SPEED;
        }
    }

    private void calculateCameraPosition(float horizontal, float vertical) {
        position.y = entity.getPosition().y + vertical;
        float theta = entity.getRY() + angleAroundPlayer;
        float xOffset = (float) (horizontal * Math.sin(Math.toRadians(theta)));
        float zOffset = (float) (horizontal * Math.cos(Math.toRadians(theta)));
        position.x = entity.getPosition().x - xOffset;
        position.z = entity.getPosition().z - zOffset;
        position.y = entity.getPosition().y + vertical;
        this.ry = 180 - (entity.getRY() + angleAroundPlayer);
    }

    private float calculateHorizontalDistance(float distance) {
        return (float) (distance * Math.cos(Math.toRadians(rx)));
    }

    private float calculateVerticalDistance(float distance) {
        return (float) (distance * Math.sin(Math.toRadians(rx)));
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

    public void setRx(float rx) {
        this.rx = rx;
    }

    public void setRy(float ry) {
        this.ry = ry;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setPosX(float x) {
        this.position = new Vector3f(x, position.y, position.z);
    }

    public void setPosZ(float z) {
        this.position = new Vector3f(position.x, position.y, z);
    }

    public float getRZ() {
        return rz;
    }

    public float getRz() {
        return rz;
    }

    public void setRz(float rz) {
        this.rz = rz;
    }

    public void invertPitch() {
        ry = -ry;
    }

}
