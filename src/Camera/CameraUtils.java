package Camera;

import org.lwjgl.input.Mouse;

public class CameraUtils {

    private Camera camera;
    private float zoomSpeed, maxZoom, minZoom, pitchSpeed, maxPitch, minPitch;

    public CameraUtils(Camera camera, float zoomSpeed, float maxZoom, float minZoom, float pitchSpeed, float maxPitch, float minPitch) {
        this.camera = camera;
        this.zoomSpeed = zoomSpeed;
        this.maxZoom = maxZoom;
        this.minZoom = minZoom;
        this.pitchSpeed = pitchSpeed;
        this.maxPitch = maxPitch;
        this.minPitch = minPitch;
    }

    public float calculateHorizontalDistance(float distance) {
        return (float) (distance * Math.cos(Math.toRadians(camera.getRX())));
    }

    public float calculateVerticalDistance(float distance) {
        return (float) (distance * Math.sin(Math.toRadians(camera.getRX())));
    }

    public float calculateCamerasZoom(float currentZoom) {
        float zoomLevel = Mouse.getDWheel() * zoomSpeed;
        float futureValue = currentZoom - zoomLevel;
        if (zoomLevel > 0) {
            return Math.max(futureValue, minZoom);
        } else return Math.min(futureValue, maxZoom);
    }

    public float calculateCamerasPitch() {
        if (Mouse.isButtonDown(1)) {
            float pitchChange = Mouse.getDY() * pitchSpeed;
            float futureValue = camera.getRX() - pitchChange;
            if (pitchChange > 0) {
                return Math.max(futureValue, maxPitch);
            } else return Math.min(futureValue, minPitch);
        } else return camera.getRX();
    }


}
