package Model;

import Loader.Loader;

public class ModelData {

    private float[] vertices,
                    textureCoords,
                    normals;
    private int[] modelIndices;
    private float furthestPoint;

    public ModelData(float[] vertices, float[] textureCoords, float[] normals, int[] modelIndices, float furthestPoint) {
        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.normals = normals;
        this.modelIndices = modelIndices;
        this.furthestPoint = furthestPoint;
    }

    public float[] getVertices() {
        return vertices;
    }

    public float[] getTextureCoords() {
        return textureCoords;
    }

    public float[] getNormals() {
        return normals;
    }

    public int[] getModelIndices() {
        return modelIndices;
    }

    public float getFurthestPoint() {
        return furthestPoint;
    }

    public RawModel convertToRawModel(Loader loader) {
        return loader.loadToVao(vertices, modelIndices, textureCoords, normals);
    }

}
