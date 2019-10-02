package Rendering;

import Camera.Camera;
import Lights.Light;
import Shaders.Terrain.TerrainShader;
import Terrains.Terrain;
import ToolBox.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.List;

import static Loader.Loader.*;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class TerrainRenderer {

    private Matrix4f projectionMatrix;
    private TerrainShader shader;
    private float fogDistance = 0.1f;

    public TerrainRenderer(Matrix4f projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
        this.shader = new TerrainShader();
        loadPresetData();
    }

    private void loadPresetData() {
        shader.start();
        loadProjectionMatrix();
        shader.loadTextures();
        shader.stop();
    }

    public void render(List<Terrain> terrains, Camera camera, List<Light> lights, Vector4f clipPlane) {
        shader.start();
        shader.loadClipPlane(clipPlane);
        for (Terrain terrain : terrains) {
            prepareTerrainForRendering(terrain, camera, lights);
            glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            unbindTerrain();
        }
        shader.stop();
    }

    private void unbindTerrain() {
        glDisableVertexAttribArray(VERTICES);
        glDisableVertexAttribArray(TEXTURE_COORDINATES);
        glDisableVertexAttribArray(NORMALS);
        glBindVertexArray(0);
    }

    private void prepareTerrainForRendering(Terrain terrain, Camera camera, List<Light> lights) {
        glBindVertexArray(terrain.getModel().getVaoId());
        glEnableVertexAttribArray(VERTICES);
        glEnableVertexAttribArray(TEXTURE_COORDINATES);
        glEnableVertexAttribArray(NORMALS);

        updateShaderUniforms(terrain, camera, lights);
        uploadTextures(terrain);
    }

    private void updateShaderUniforms(Terrain terrain, Camera camera, List<Light> lights) {
        shader.loadTransformationMatrix(Maths.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()),
                0, 0, 0, 1));
        shader.loadViewMatrix(camera);
        shader.loadLights(lights);
        shader.loadSpecular(0.5f, 0.01f);
        shader.loadFogValue(0.0012f, 5.0f, BaseRenderer.background);
    }

    private void uploadTextures(Terrain terrain) {
        glActiveTexture(GL13.GL_TEXTURE0);
        glBindTexture(GL11.GL_TEXTURE_2D, terrain.getTexturePackage().getTextures().get(0).getTextureID());
        glActiveTexture(GL13.GL_TEXTURE1);
        glBindTexture(GL11.GL_TEXTURE_2D, terrain.getTexturePackage().getTextures().get(1).getTextureID());
        glActiveTexture(GL13.GL_TEXTURE2);
        glBindTexture(GL11.GL_TEXTURE_2D, terrain.getTexturePackage().getTextures().get(2).getTextureID());
        glActiveTexture(GL13.GL_TEXTURE3);
        glBindTexture(GL11.GL_TEXTURE_2D, terrain.getTexturePackage().getTextures().get(3).getTextureID());
        glActiveTexture(GL13.GL_TEXTURE4);
        glBindTexture(GL11.GL_TEXTURE_2D, terrain.getTexturePackage().getTextures().get(4).getTextureID());
    }

    private void loadProjectionMatrix() {
        shader.loadProjectionMatrix(projectionMatrix);
    }

    public void cleanUp() {
        shader.cleanUp();
    }
}
