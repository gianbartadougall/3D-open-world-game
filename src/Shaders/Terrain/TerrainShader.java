package Shaders.Terrain;

import Camera.Camera;
import Lights.Light;
import Shaders.ShaderProgram;
import ToolBox.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.List;

import static Loader.Loader.TEXTURE_COORDINATES;
import static Loader.Loader.VERTICES;

public class TerrainShader extends ShaderProgram {

    private static final String VERTEX_FILE = "Terrain/terrainVertexShader.glsl",
                                FRAGMENT_FILE = "Terrain/terrainFragmentShader.glsl";
    private static final int MAX_LIGHTS = 4;

    private int transformationMatrix_Location, viewMatrix_Location, projectionMatrix_Location, texture1_location, texture2_location,
            texture3_location, background_location, blendMap_Location, fogDensity_Location, backgroundColor_Location, fogGradient_Location,
            shineDamper_Location, reflectivity_Location, plane_Location;

    private int[] lightPosition_locations, lightAttenuation_Locations, lightColor_Locations, lightIntensity_Locations;

    public TerrainShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
        getAllUniformLocations();
    }

    @Override
    protected void bindAttributes() {
        bindVAOAttributeToVariable(VERTICES, "position");
        bindVAOAttributeToVariable(TEXTURE_COORDINATES, "textureCoordinates");
    }

    @Override
    protected void getAllUniformLocations() {
        transformationMatrix_Location = getUniformLocation("transformationMatrix");
        projectionMatrix_Location = getUniformLocation("projectionMatrix");
        viewMatrix_Location = getUniformLocation("viewMatrix");
        texture1_location = getUniformLocation("texture1");
        texture2_location = getUniformLocation("texture2");
        texture3_location = getUniformLocation("texture3");
        background_location = getUniformLocation("backgroundTexture");
        blendMap_Location = getUniformLocation("blendMap");
        fogDensity_Location = getUniformLocation("fogDensity");
        backgroundColor_Location = getUniformLocation("backgroundColor");
        fogGradient_Location = getUniformLocation("fogGradient");
        shineDamper_Location = getUniformLocation("sineDamper");
        reflectivity_Location = getUniformLocation("reflectivity");
        plane_Location = getUniformLocation("plane");


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

    public void loadProjectionMatrix(Matrix4f m) {
        loadVarToUniform(projectionMatrix_Location, m);
    }

    public void loadTransformationMatrix(Matrix4f m) {
        loadVarToUniform(transformationMatrix_Location, m);
    }

    public void loadViewMatrix(Camera camera) {
        loadVarToUniform(viewMatrix_Location, Maths.createViewMatrix(camera));
    }

    public void loadClipPlane(Vector4f v) {
        loadVarToUniform(plane_Location, v);
    }

    public void loadSpecular(float shineDamper, float reflectivity) {
        loadVarToUniform(shineDamper_Location, shineDamper);
        loadVarToUniform(reflectivity_Location, reflectivity);
    }

    public void loadLights(List<Light> lights) {
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

    public void loadTextures() {
        loadVarToUniform(texture1_location, 0);
        loadVarToUniform(texture2_location, 1);
        loadVarToUniform(texture3_location, 2);
        loadVarToUniform(background_location, 3);
        loadVarToUniform(blendMap_Location, 4);
    }

    public void loadFogValue(float fogDensity, float fogGradient, Vector4f backgroundColor) {
        loadVarToUniform(fogDensity_Location, fogDensity);
        loadVarToUniform(backgroundColor_Location, backgroundColor);
        loadVarToUniform(fogGradient_Location, fogGradient);
    }

}
