package Shaders.Water;

import Camera.Camera;
import Shaders.ShaderProgram;
import ToolBox.Maths;
import org.lwjgl.util.vector.Matrix4f;

import static Loader.Loader.VERTICES;

public class WaterShader extends ShaderProgram {

    private static final String VERTEX_FILE = "Water/waterVertexShader.glsl",
                                FRAGMENT_FILE = "Water/waterFragmentShader.glsl";

    private int transformationMatrix_Location, viewMatrix_Location, projectionMatrix_Location,
                refractionTexture_Location, reflectionTexture_Location;

    public WaterShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
        getAllUniformLocations();
    }

    @Override
    protected void bindAttributes() {
        bindVAOAttributeToVariable(VERTICES, "position");
    }

    @Override
    protected void getAllUniformLocations() {
        transformationMatrix_Location = getUniformLocation("transformationMatrix");
        projectionMatrix_Location = getUniformLocation("projectionMatrix");
        viewMatrix_Location = getUniformLocation("viewMatrix");
        reflectionTexture_Location = getUniformLocation("reflectionTexture");
        refractionTexture_Location = getUniformLocation("refractionTexture");

    }

    public void loadProjectionMatrix(Matrix4f m) {
        loadVarToUniform(projectionMatrix_Location, m);
    }

    public void loadTransformationMatrix(Matrix4f m) {
        loadVarToUniform(transformationMatrix_Location, m);
    }

    public void loadViewMatrix(Camera camera) {
        loadVarToUniform(viewMatrix_Location, Maths.createViewMatrix(camera));
    }

    public void loadWaterTextures() {
        loadVarToUniform(reflectionTexture_Location, 0);
        loadVarToUniform(refractionTexture_Location, 1);
    }


}
