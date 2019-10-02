package Shaders.Basic;

import Camera.Camera;
import Lights.Light;
import Shaders.ShaderProgram;
import ToolBox.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.List;

import static Loader.Loader.*;

public class BasicShader extends ShaderProgram {

    private static final String VERTEX_FILE = "Basic/basicVertexShader.glsl",
                                FRAGMENT_FILE = "Basic/basicFragmentShader.glsl";
    private static final int MAX_LIGHTS = 4;

    private int transformationMatrix_Location, viewMatrix_Location, projectionMatrix_Location,
            reflectivity_Location, shineDamper_Location, fogDensity_Location, fogGradient_Location, backgroundColor_Location,
            transformedPosition_Location, plane_location;
    private int[] lightPosition_locations, lightAttenuation_Locations, lightColor_Locations, lightIntensity_Locations;

    public BasicShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
        getAllUniformLocations();
    }

    @Override
    protected void bindAttributes() {
        bindVAOAttributeToVariable(VERTICES, "position");
        bindVAOAttributeToVariable(TEXTURE_COORDINATES, "textureCoordinates");
        bindVAOAttributeToVariable(NORMALS, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        transformationMatrix_Location = getUniformLocation("transformationMatrix");
        projectionMatrix_Location = getUniformLocation("projectionMatrix");
        viewMatrix_Location = getUniformLocation("viewMatrix");
        reflectivity_Location = getUniformLocation("reflectivity");
        shineDamper_Location = getUniformLocation("shineDamper");
        fogDensity_Location = getUniformLocation("fogDensity");
        backgroundColor_Location = getUniformLocation("backgroundColor");
        fogGradient_Location = getUniformLocation("fogGradient");
        transformedPosition_Location = getUniformLocation("transformedPosition");
        plane_location = getUniformLocation("plane");

        lightPosition_locations = new int[MAX_LIGHTS];
        lightColor_Locations = new int[MAX_LIGHTS];
        lightAttenuation_Locations = new int[MAX_LIGHTS];
        lightIntensity_Locations = new int[MAX_LIGHTS];
        for (int i = 0; i < MAX_LIGHTS; i++) {
            lightPosition_locations[i] = getUniformLocation("lightPosition["+i+"]");
            lightColor_Locations[i] = getUniformLocation("lightColor["+i+"]");
            lightAttenuation_Locations[i] = getUniformLocation("attenuation["+i+"]");
            lightIntensity_Locations[i] = getUniformLocation("lightIntensity["+i+"]");
        }
    }

    public void loadTransformationMatrix(Matrix4f m) {
        loadVarToUniform(transformationMatrix_Location, m);
    }

    public void loadViewMatrix(Camera camera) {
        loadVarToUniform(viewMatrix_Location, Maths.createViewMatrix(camera));
    }

    public void loadProjectionMatrix(Matrix4f m) {
        loadVarToUniform(projectionMatrix_Location, m);
    }

    public void loadLight(List<Light> lights) {
        for (int i = 0; i < MAX_LIGHTS; i++) {
            if (i<lights.size()) {
                loadVarToUniform(lightPosition_locations[i], lights.get(i).getPosition());
                loadVarToUniform(lightIntensity_Locations[i], lights.get(i).getIntensity());
                loadVarToUniform(lightAttenuation_Locations[i], lights.get(i).getAttenuation());
                loadVarToUniform(lightColor_Locations[i], lights.get(i).getColor());
            } else {
                // if less then 4 lights exist then loading up fake lights to shader to make sure it still runs
                loadVarToUniform(lightPosition_locations[i], new Vector3f(0,0,0));
                loadVarToUniform(lightIntensity_Locations[i], new Vector3f(0,0,0));
                loadVarToUniform(lightAttenuation_Locations[i], new Vector3f(1,0,0));
                loadVarToUniform(lightColor_Locations[i], new Vector3f(0,0,0));
            }
        }
    }

    public void loadClipPlane(Vector4f v) {
        loadVarToUniform(plane_location, v);
    }

    public void loadSpecular(float damper, float reflectivity) {
        loadVarToUniform(shineDamper_Location, damper);
        loadVarToUniform(reflectivity_Location, reflectivity);
    }

    public void loadFog(float fogDensity, float fogGradient, Vector4f backgroundColor) {
        loadVarToUniform(fogDensity_Location, fogDensity);
        loadVarToUniform(fogGradient_Location, fogGradient);
        loadVarToUniform(backgroundColor_Location, backgroundColor);
    }

    public void loadTransformedPosition(Vector3f pos) {
        loadVarToUniform(transformedPosition_Location, pos);
    }

}
