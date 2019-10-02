package Rendering;

import Camera.Camera;
import Display.DisplayManager;
import Entities.Entity;
import Lights.Light;
import Terrains.Terrain;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class BaseRenderer {

    private static final float FOV = 70.0f, NEAR_PLANE = 0.1f, FAR_PLANE = 1000;
    public static Vector4f background = new Vector4f(0f, 0.9f, 0.8f, 1.0f);
    public static float RED = 1, GREEN = 1, BLUE = 1;
    private Matrix4f projectionMatrix;
    private EntityRenderer entityRenderer;
    private TerrainRenderer terrainRenderer;

    public BaseRenderer() {
        createProjectionMatrix();
        entityRenderer = new EntityRenderer(projectionMatrix);
        terrainRenderer = new TerrainRenderer(projectionMatrix);
        enableCulling();
    }

    public void render(Camera camera, List<Entity> entities, List<Terrain> terrains, List<Light> lights, Vector4f clipPlane) {
        resetScreen();
        terrainRenderer.render(terrains, camera, lights, clipPlane);
        entityRenderer.render(entities, camera, lights, clipPlane);
    }

    public static void enableCulling() {
        glEnable(GL11.GL_CULL_FACE); // this enables face culling
        GL11.glCullFace(GL_BACK); // this sets it so the faces that you cannot see will not render making rendering times quicker
    }

    public static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    private void resetScreen() {
        glEnable(GL11.GL_DEPTH_TEST);
        glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        glClearColor(background.x, background.y, background.z, background.w);
    }

    private void createProjectionMatrix() {
        float aspectRatio = (float) DisplayManager.WIDTH / (float) DisplayManager.HEIGHT;
        float y_scale = (float) (1f / Math.tan(Math.toRadians(FOV / 2))) * aspectRatio;
        float x_scale = y_scale / aspectRatio;
        float frustumLength = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE)) / frustumLength;
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustumLength);
        projectionMatrix.m33 = 0;
    }

    public void cleanUp() {
        entityRenderer.cleanUp();
        terrainRenderer.cleanUp();
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
}
