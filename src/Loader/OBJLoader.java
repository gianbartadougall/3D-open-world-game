package Loader;

import Model.ModelData;
import Model.RawModel;
import ToolBox.General;
import Vertices.Vertex;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OBJLoader {

    public RawModel loadObjModel(String fileName, Loader loader) {
        FileReader fr = null;
        try {
            fr = new FileReader(new File("res/models/" + fileName + ".obj"));
        } catch (FileNotFoundException e) {
            General.activateError(e, "Error: Could not load obj file", 1);
        }

        BufferedReader bufferedReader = new BufferedReader(fr);
        String line;
        List<Vector3f> vertices = new ArrayList<>(); // creating a list of vertices to be able to hold all the vertices of the model
        List<Vector2f> textures = new ArrayList<>(); // creating a list of texture coordinates to hold coordinates for the textures on the model
        List<Vector3f> normals = new ArrayList<>(); // creating a list of normals
        List<Integer> indices = new ArrayList<>(); // creating a list of indices which will tell open gl in what order to use each vertex and texture to build the model correctly

        // creating float list versions of the Lists as float[] is the data type needed to create the vao's
        float[] verticesArray = null;
        float[] texturesArray = null;
        float[] normalsArray = null;
        int[] indicesArray = null;

        try {

            while (true) {
                line = bufferedReader.readLine();
                String[] currentLine = line.split(" ");
                if (line.startsWith("v ")) { // getting data for vertices
                    vertices.add(new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3])));

                } else if (line.startsWith("vt ")) { // getting data for textures
                    textures.add(new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2])));

                } else if (line.startsWith("vn ")) { // getting data for normals
                    normals.add(new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3])));

                } else if (line.startsWith("f ")) {  // getting data for faces
                    texturesArray = new float[vertices.size()*2]; // multiplied by 2 as a texture has an x and y
                    normalsArray = new float[vertices.size()*3]; // multiplied by 2 as a vertex has an x, y, z
                    break;
                }
            }

            while (line != null) {
                if (!line.startsWith("f ")) {
                    line = bufferedReader.readLine();
                    continue;
                }

                String[] currentLine = line.split(" "); // this split gives each vertex

                // this split gives each vertex of a triangle the vertex, texture and normal reference numbers for the vectors used by that vertex
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1, indices, textures, normals, texturesArray, normalsArray);
                processVertex(vertex2, indices, textures, normals, texturesArray, normalsArray);
                processVertex(vertex3, indices, textures, normals, texturesArray, normalsArray);

                line = bufferedReader.readLine();
            }

            bufferedReader.close();

        } catch (Exception e) {
            General.activateError(e, "Error: Invalid file format", 1);
        }

        verticesArray = new float[vertices.size()*3]; // multiply by 3 as putting list in as floats not vector3fs
        indicesArray = new int[indices.size()];

        // converting list of Vector3's to floatList
        int vertexPoint = 0;
        for (Vector3f vertex : vertices) {
            verticesArray[vertexPoint++] = vertex.x;
            verticesArray[vertexPoint++] = vertex.y;
            verticesArray[vertexPoint++] = vertex.z;
        }

        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }
        return null;
        //return loader.loadToVao(verticesArray, indicesArray, texturesArray, normalsArray);
    }

    private void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures,
                                      List<Vector3f> normals, float[] textureArray, float[] normalsArray) {

        int currentVertexIndex = Integer.parseInt(vertexData[0]) - 1; // minus one on the end as obj loader starts at one where as the computer starts counting at 0
        indices.add(currentVertexIndex);

        Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
        textureArray[currentVertexIndex*2] = currentTex.x; // multiply by 2 as each texture has 2 floats
        textureArray[currentVertexIndex*2+1] = 1 - currentTex.y; // 1 - as start as open gl starts from top left where as blender starts from bottom left

        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
        // normals are a vector describing the direction the object is facing as the normal is perpendicular to the point it represents
        normalsArray[currentVertexIndex*3] = currentNorm.x;
        normalsArray[currentVertexIndex*3+1] = currentNorm.y;
        normalsArray[currentVertexIndex*3+2] = currentNorm.z;
    }


    private static final String RES_LOC = "res/models/";

    public ModelData loadOBJ(String objFileName) {
        FileReader fr = null;
        File objFile = new File(RES_LOC + objFileName + ".obj");
        System.out.println(objFile.toString());
        try {
            fr = new FileReader(objFile);
        } catch (FileNotFoundException e) {
            System.err.println("File not found in res; don't use any extention");
        }
        BufferedReader reader = new BufferedReader(fr);
        String line;
        List<Vertex> vertices = new ArrayList<Vertex>();
        List<Vector2f> textures = new ArrayList<Vector2f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        List<Integer> indices = new ArrayList<Integer>();
        float maxFloat = Float.MAX_VALUE;
        float minX = maxFloat,
              maxX = -maxFloat,
              minY = maxFloat,
              maxY = -maxFloat,
              minZ = maxFloat,
              maxZ = -maxFloat;

        try {
            while (true) {
                line = reader.readLine();
                if (line.startsWith("v ")) {
                    String[] currentLine = line.split(" ");
                    Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),
                                                   Float.parseFloat(currentLine[2]),
                                                   Float.parseFloat(currentLine[3]));

                    minX = Math.min(vertex.x, minX);
                    maxX = Math.max(vertex.x, maxX);
                    minY = Math.min(vertex.y, minY);
                    maxY = Math.max(vertex.y, maxY);
                    minZ = Math.min(vertex.z, minZ);
                    maxZ = Math.max(vertex.z, maxZ);

                    Vertex newVertex = new Vertex(vertices.size(), vertex);
                    vertices.add(newVertex);

                } else if (line.startsWith("vt ")) {
                    String[] currentLine = line.split(" ");
                    Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]),
                                                    Float.parseFloat(currentLine[2]));
                    textures.add(texture);
                } else if (line.startsWith("vn ")) {
                    String[] currentLine = line.split(" ");
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),
                                                   Float.parseFloat(currentLine[2]),
                                                   Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                } else if (line.startsWith("f ")) {
                    break;
                }
            }
            while (line != null && line.startsWith("f ")) {
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");
                processVertex(vertex1, vertices, indices);
                processVertex(vertex2, vertices, indices);
                processVertex(vertex3, vertices, indices);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading the file");
        }
        removeUnusedVertices(vertices);
        float[] verticesArray = new float[vertices.size() * 3 + 24];
        float[] texturesArray = new float[vertices.size() * 2];
        float[] normalsArray = new float[vertices.size() * 3];
        float furthest = convertDataToArrays(vertices, textures, normals, verticesArray,
                texturesArray, normalsArray);
        int[] indicesArray = convertIndicesListToArray(indices);

        return new ModelData(verticesArray, texturesArray, normalsArray, indicesArray, furthest);
    }

    private void processVertex(String[] vertex, List<Vertex> vertices, List<Integer> indices) {
        int index = Integer.parseInt(vertex[0]) - 1;
        Vertex currentVertex = vertices.get(index);
        int textureIndex = Integer.parseInt(vertex[1]) - 1;
        int normalIndex = Integer.parseInt(vertex[2]) - 1;
        if (!currentVertex.isSet()) {
            currentVertex.setTextureIndex(textureIndex);
            currentVertex.setNormalIndex(normalIndex);
            indices.add(index);
        } else {
            dealWithAlreadyProcessedVertex(currentVertex, textureIndex, normalIndex, indices,
                    vertices);
        }
    }

    private int[] convertIndicesListToArray(List<Integer> indices) {
        int[] indicesArray = new int[indices.size() + 36];
        for (int i = 0; i < indicesArray.length - 36; i++) {
            indicesArray[i] = indices.get(i);
        }
        return indicesArray;
    }

    private float convertDataToArrays(List<Vertex> vertices, List<Vector2f> textures,
                                             List<Vector3f> normals, float[] verticesArray, float[] texturesArray,
                                             float[] normalsArray) {
        float furthestPoint = 0;
        for (int i = 0; i < vertices.size(); i++) {
            Vertex currentVertex = vertices.get(i);
            if (currentVertex.getLength() > furthestPoint) {
                furthestPoint = currentVertex.getLength();
            }
            Vector3f position = currentVertex.getPosition();
            Vector2f textureCoord = textures.get(currentVertex.getTextureIndex());
            Vector3f normalVector = normals.get(currentVertex.getNormalIndex());
            verticesArray[i * 3] = position.x;
            verticesArray[i * 3 + 1] = position.y;
            verticesArray[i * 3 + 2] = position.z;
            texturesArray[i * 2] = textureCoord.x;
            texturesArray[i * 2 + 1] = 1 - textureCoord.y;
            normalsArray[i * 3] = normalVector.x;
            normalsArray[i * 3 + 1] = normalVector.y;
            normalsArray[i * 3 + 2] = normalVector.z;
        }
        return furthestPoint;
    }

    private void dealWithAlreadyProcessedVertex(Vertex previousVertex, int newTextureIndex,
                                                       int newNormalIndex, List<Integer> indices, List<Vertex> vertices) {
        if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
            indices.add(previousVertex.getIndex());
        } else {
            Vertex anotherVertex = previousVertex.getDuplicateVertex();
            if (anotherVertex != null) {
                dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex, newNormalIndex,
                        indices, vertices);
            } else {
                Vertex duplicateVertex = new Vertex(vertices.size(), previousVertex.getPosition());
                duplicateVertex.setTextureIndex(newTextureIndex);
                duplicateVertex.setNormalIndex(newNormalIndex);
                previousVertex.setDuplicateVertex(duplicateVertex);
                vertices.add(duplicateVertex);
                indices.add(duplicateVertex.getIndex());
            }

        }
    }

    private void removeUnusedVertices(List<Vertex> vertices){
        for(Vertex vertex:vertices){
            if(!vertex.isSet()){
                vertex.setTextureIndex(0);
                vertex.setNormalIndex(0);
            }
        }
    }

}
