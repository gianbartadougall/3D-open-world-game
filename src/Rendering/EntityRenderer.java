package Rendering;

import Camera.Camera;
import Entities.Entity;
import Lights.Light;
import Model.RawModel;
import Shaders.Basic.BasicShader;
import ToolBox.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import java.util.List;

import static Loader.Loader.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class EntityRenderer {

    private Matrix4f projectionMatrix;
    private BasicShader shader;

    public EntityRenderer(Matrix4f projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
        shader = new BasicShader();
        loadProjectionMatrix();
    }

    public void render(List<Entity> entities, Camera camera, List<Light> lights, Vector4f clipPlane) {
        shader.start();
        shader.loadClipPlane(clipPlane);
        for (Entity entity : entities) {
            RawModel model = entity.getTexturedModel().getModel();
            prepareEntityForRendering(model);
            prepareInstance(entity, camera, lights);
            uploadTextures(entity);
            GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        }
        unbindEntity();
        shader.stop();
    }

    private void uploadTextures(Entity entity) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getTexturedModel().getTexture().getTextureId());
    }

    private void unbindEntity() {
        glDisableVertexAttribArray(VERTICES); // unbinding VBO
        glDisableVertexAttribArray(TEXTURE_COORDINATES);
        glDisableVertexAttribArray(NORMALS);
        glBindVertexArray(0); // unbinding VAO
    }

    private void prepareEntityForRendering(RawModel model) {
        glBindVertexArray(model.getVaoId());
        glEnableVertexAttribArray(VERTICES); // enabling VBO
        glEnableVertexAttribArray(TEXTURE_COORDINATES);
        glEnableVertexAttribArray(NORMALS);
    }

    private void prepareInstance(Entity e, Camera camera, List<Light> lights) {
        shader.loadTransformationMatrix(Maths.createTransformationMatrix(e.getPosition(), e.getRX(), e.getRY(), e.getRZ(), e.getScale()));
        shader.loadViewMatrix(camera);
        shader.loadLight(lights);
        shader.loadSpecular(e.getTexturedModel().getTexture().getShineDamper(),
                                  e.getTexturedModel().getTexture().getReflectivity());
        shader.loadFog(0.0012f, 5, BaseRenderer.background);
    }

    private void loadProjectionMatrix() {
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void cleanUp() {
        shader.cleanUp();
    }

}
