package Lights;

import org.lwjgl.util.vector.Vector3f;

public class Light {

    private Vector3f position;
    private Vector3f color;
    private Vector3f attenuation;
    private float intensity;

    public Light(Vector3f position, Vector3f color, float intensity) {
        this.position = position;
        this.color = color;
        this.intensity = intensity;
        this.attenuation = new Vector3f(1, 0, 0);
    }

    public Light(Vector3f position, Vector3f color, float intensity, Vector3f attenuation) {
        this.position = position;
        this.color = color;
        this.intensity = intensity;
        this.attenuation = attenuation;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getColor() {
        return color;
    }

    public Vector3f getAttenuation() {
        return attenuation;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }
}
