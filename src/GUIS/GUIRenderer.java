package GUIS;

import Loader.Loader;
import Model.RawModel;
import Shaders.GUI.GUIShader;
import ToolBox.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;

import static Enums.Enum.QUAD;
import static Loader.Loader.VERTICES;

public class GUIRenderer {

    private RawModel quad;
    private GUIShader guiShader;

    public GUIRenderer(Loader loader) {
        quad = loader.loadToVao(QUAD, 2);
        guiShader = new GUIShader();
    }

    public void renderGUI(List<GUITexture> guis) {
        prepareGUI();
        for (GUITexture gui : guis) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0); // bind texture
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture()); // binding the texture to the gui
            Matrix4f matrix4f = Maths.createTransformationMatrix(gui.getPosition(), gui.getScale()); // setting transformation matrix up
            guiShader.loadTransformation(matrix4f); // loading transformation matrix to shader and files
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
        }
        deconstructGUI();
    }

    private void prepareGUI() {
        GL30.glBindVertexArray(quad.getVaoId());
        GL20.glEnableVertexAttribArray(VERTICES);
        guiShader.start();
    }

    private void deconstructGUI() {
        GL20.glDisableVertexAttribArray(VERTICES);
        GL30.glBindVertexArray(0);
        guiShader.stop();
    }

    public void cleanUp() {
        guiShader.cleanUp();
    }
}
