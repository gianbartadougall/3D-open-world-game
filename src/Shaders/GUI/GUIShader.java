package Shaders.GUI;

import Shaders.ShaderProgram;
import org.lwjgl.util.vector.Matrix4f;

import static Loader.Loader.VERTICES;

public class GUIShader extends ShaderProgram {

    private static final String VERTEX_FILE = "GUI/guiVertexShader.glsl";
    private static final String FRAGMENT_FILE = "GUI/guiFragmentShader.glsl";

    private int location_transformationMatrix;

    public GUIShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindVAOAttributeToVariable(VERTICES, "position");
    }

    public void loadTransformation(Matrix4f matrix){
        super.loadVarToUniform(location_transformationMatrix, matrix);
    }

}
