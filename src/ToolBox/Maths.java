package ToolBox;

import Camera.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.Random;

public class Maths {

    public static Random r = new Random();

    public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }

    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
        Matrix4f m = newIdentityMatrix();
        Matrix4f.translate(translation, m, m);
        Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), m, m);
        Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), m, m);
        Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), m, m);
        Matrix4f.scale(new Vector3f(scale, scale, scale), m, m);
        return m;
    }

    // translating guis to the preferred position
    public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
        return matrix;
    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = newIdentityMatrix();
        Matrix4f.rotate((float) Math.toRadians(camera.getRX()), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getRY()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getRZ()), new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
        Vector3f cameraPos = camera.getPosition();
        Vector3f inverseCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        Matrix4f.translate(inverseCameraPos, viewMatrix, viewMatrix);
        return viewMatrix;
    }

    private static Matrix4f newIdentityMatrix() {
        Matrix4f m = new Matrix4f();
        m.setIdentity();
        return m;
    }

    public static double lengthOfLine(float x1, float x2, float y1, float y2) {
        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
    }
}
