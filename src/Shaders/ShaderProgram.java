package Shaders;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.nio.FloatBuffer;

import static Shaders.ShaderUtils.loaderShader;

public abstract class ShaderProgram {

    private int programId, vertexShaderId, fragmentShaderId;
    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public ShaderProgram(String vertexShaderFileName, String fragmentShaderFileName) {
        this.vertexShaderId = loaderShader(vertexShaderFileName, GL20.GL_VERTEX_SHADER);
        this.fragmentShaderId = loaderShader(fragmentShaderFileName, GL20.GL_FRAGMENT_SHADER);
        createProgram();
        getAllUniformLocations();
    }

    private void createProgram() {
        programId = GL20.glCreateProgram();
        GL20.glAttachShader(programId, vertexShaderId);
        GL20.glAttachShader(programId, fragmentShaderId);
        bindAttributes(); // needs to be called before the glLink or won't work properly
        GL20.glLinkProgram(programId);
        GL20.glValidateProgram(programId);
    }

    protected abstract void bindAttributes();

    protected abstract void getAllUniformLocations();

    protected int getUniformLocation(String uniformName) {
        return GL20.glGetUniformLocation(programId, uniformName);
    }

    protected void bindVAOAttributeToVariable(int attribute, String variableName) {
        GL20.glBindAttribLocation(programId, attribute, variableName);
    }

    protected void loadVarToUniform(int location, Matrix4f m) {
        m.store(matrixBuffer);
        matrixBuffer.flip();
        GL20.glUniformMatrix4(location, false, matrixBuffer);
    }

    protected void loadVarToUniform(int location, Vector4f v) {
        GL20.glUniform4f(location, v.x, v.y, v.z, v.w);
    }

    protected void loadVarToUniform(int location, int val) {
        GL20.glUniform1i(location, val);
    }

    protected void loadVarToUniform(int location, Vector3f v) {
        GL20.glUniform3f(location, v.x, v.y, v.z);
    }

    protected void loadVarToUniform(int location, float amt) {
        GL20.glUniform1f(location, amt);
    }

    public void start() {
        GL20.glUseProgram(programId);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    public void cleanUp() {
        stop();
        GL20.glDetachShader(programId, vertexShaderId);
        GL20.glDetachShader(programId, fragmentShaderId);
        GL20.glDeleteShader(vertexShaderId);
        GL20.glDeleteShader(fragmentShaderId);
        GL20.glDeleteProgram(programId);
    }

}
