package Loader;

import Model.RawModel;
import ToolBox.General;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {
    public static final int ENTITY = 0, TERRAIN = 1;
    public static final int VERTICES = 0,
                            TEXTURE_COORDINATES = 1,
                            NORMALS = 2;

    private List<Integer> VAOS = new ArrayList<>(), VBOS = new ArrayList<>();
    private ArrayList<Integer> textures = new ArrayList<>();

    public RawModel loadToVao(float[] vertices, int[] indices, float[] textureCoords, float[] normals) {
        int vaoId = createNewVao();
        bindIndicesBuffer(indices);
        storeDataInVao(vertices, VERTICES, 3);
        storeDataInVao(textureCoords, TEXTURE_COORDINATES, 2);
        storeDataInVao(normals, NORMALS, 3);
        unbindVAO();
        return new RawModel(vaoId, indices.length);
    }

    public RawModel loadToVao(float[] positions, int dimensions) {
        int vaoID = createNewVao();
        this.storeDataInVao(positions, VERTICES, dimensions);
        unbindVAO();
        return new RawModel(vaoID, positions.length/dimensions);
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboId = GL15.glGenBuffers();
        VBOS.add(vboId);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private IntBuffer storeDataInIntBuffer(int[] data) {
        return BufferUtils.createIntBuffer(data.length).put(data).flip();
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    private void storeDataInVao(float[] vertices, int attributeIndex, int dimensions) {
        int vboId = GL15.glGenBuffers(); // creating vbo id
        VBOS.add(vboId);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId); // binding buffer before use
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, storeDataInNewFloatBuffer(vertices), GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeIndex, dimensions, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private FloatBuffer storeDataInNewFloatBuffer(float[] data) {
        return BufferUtils.createFloatBuffer(data.length).put(data).flip();
    }

    public int createNewVao() {
        int vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);
        VAOS.add(vaoId);
        return vaoId;
    }

    public int loadTexture(String fileName, int type) {
        Texture texture = null;

        try {
            texture = TextureLoader.getTexture("PNG", new FileInputStream("res/textures/" + (type == 0 ? "Entity/" : "Terrain/") + fileName + ".png"));

            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.6f);

        } catch (IOException e) {
            General.activateError(e, "Error: Unable to load texture", 1);
        }

        int textureID = texture.getTextureID();
        textures.add(textureID);
        return textureID;
    }

    public void cleanUp() {
        for (Integer vao : VAOS) {
            GL30.glDeleteVertexArrays(vao);
        }
        for (Integer vbo : VBOS) {
            GL15.glDeleteBuffers(vbo);
        }
        for (Integer texture : textures) {
            GL11.glDeleteTextures(texture);
        }
    }

}
