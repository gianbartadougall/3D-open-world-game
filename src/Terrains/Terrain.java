package Terrains;

import Loader.Loader;
import Model.RawModel;
import Textures.TerrainTexture;
import Textures.TexturePackage;
import ToolBox.General;
import ToolBox.Maths;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Terrain {

    public static final float MAX_PIXEL_COLOR = 256 * 256 * 256;
    private int SIZE = 800, MAX_HEIGHT = 40, x, z;;
    public static final int WIDTH = 800, HEIGHT = 800;
    private RawModel model;
    private TexturePackage texturePackage;
    private float heights[][];
    private BufferedImage heightMap, blendMap;
    private int distanceFromOrigin = 800;


    public Terrain(int gridX, int gridZ, Loader loader, List<TerrainTexture> textureList , String heightMap, String blendMap) {
        long startTime = System.nanoTime()/1000000;
        getImage(heightMap, blendMap);
        this.model = generateTerrain(loader, this.heightMap);
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        this.texturePackage = new TexturePackage(textureList);
        long endTime = System.nanoTime()/1000000;
        System.out.println("It took " + (endTime - startTime) + " milliseconds to generate the terrain");
    }

    private void getImage(String heightMapName, String terrainLayout) {
        heightMap = null;
        blendMap = null;
        try {
            heightMap = ImageIO.read(new File("res/textures/Terrain/" + heightMapName + ".png"));
            blendMap = ImageIO.read(new File("res/textures/Terrain/" + terrainLayout + ".png"));
        } catch (IOException e) {
            General.activateError(e, "Error: Could not load TerrainMaps", 1);
        }
    }

    public RawModel getModel() {
        return model;
    }

    public RawModel generateTerrain(Loader loader, BufferedImage heightMap) {
        // get the textures width and height
        int count = heightMap.getWidth() * heightMap.getHeight();
        int VERTEX_COUNT = heightMap.getHeight();
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        heights = new float[VERTEX_COUNT][VERTEX_COUNT];
        int vertexNumber = 0;

        for(int i=0;i<VERTEX_COUNT;i++){
            for(int j=0;j<VERTEX_COUNT;j++){

                int index = vertexNumber * 3; // multiplied by 3 as each vertex has an xyz
                float chosenX = (float)j/((float)VERTEX_COUNT - 1);
                float chosenY = (float)i/((float)VERTEX_COUNT - 1);
                float height = getHeightFromPixel(heightMap, j, i);
                heights[j][i] = height;
                vertices[index] = chosenX * SIZE; // this determins the vertices x pos
                vertices[index+1] = height;
                // this determins the vertices y pos
                vertices[index+2] = chosenY * SIZE; // this determins the vertices z pos
                // This determins the x y z vector making up the direction that the normal of this vertex is facing
                Vector3f normal = calculateNormal(j, i, heightMap);
                normals[index] = normal.x;
                normals[index+1] = normal.y;
                normals[index+2] = normal.z;
                // this determins the texture coordinates of the vertex
                textureCoords[vertexNumber*2] = chosenX;
                textureCoords[vertexNumber*2+1] = chosenY;
                vertexNumber++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }

        return loader.loadToVao(vertices, indices, textureCoords, normals);
    }
    
    private float getHeightFromPixel(BufferedImage img, int x, int z) {
        if (x<0 || x>= img.getHeight() || z<0 || z>=img.getHeight()) {
            return 0; // out of bounds
        }
        float height = img.getRGB(x, z);
        height += MAX_PIXEL_COLOR/2;
        height /= MAX_PIXEL_COLOR/2;
        return height * MAX_HEIGHT;
    }

    private Vector3f calculateNormal(int x, int z, BufferedImage image) {
        // to calculate height of a vertex need to calc height of all 4 neighbouring vertices
        float heightL = getHeightFromPixel(image, x-1, z); // [0]
        float heightR = getHeightFromPixel(image,x+1, z); // [1]
        float heightD = getHeightFromPixel(image, x, z-1); // [2]
        float heightU = getHeightFromPixel(image, x, z+1); // [3]
        Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
        normal.normalise();
        return normal;
    }

    public TexturePackage getTexturePackage() {
        return texturePackage;
    }

    public float getHeightOfTerrain(float worldX, float worldZ) {
        // convert the world coordinate into a position relative to the terrain
        float terrainX = worldX - this.x;
        float terrainZ = worldZ - this.z;
        // find the number of grid squares along the side of the terrain (-1 at the end as 4 vertices creates 3 grids)
        float gridSquareSize = SIZE / ((float) heights.length - 1);
        // finding out which grid square on the terrain is the player in
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

        if (gridX >= heights.length-1 || gridZ >= heights.length -1 || gridX < 0 || gridZ < 0) {
            return -40; // this checks to see if the grid calculated is a valid grid on the terrain, if it isn't the return 0;
        }

        // finding out where on the grid square the player is
        float xCoord = (terrainX % gridSquareSize)/gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize)/gridSquareSize;

        // testing what triangle in the grid square the player is standing on

        if (xCoord <= 1-z) {
            // calculating at what height the pixel is at the players xy position on the triangle
            return Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
                                      heights[gridX + 1][gridZ], 0), new Vector3f(0,
                                      heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        }
        return Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
                                  heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
                                  heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
    }

    public BufferedImage getHeightMap() {
        return heightMap;
    }

    public BufferedImage getBlendMap() {
        return blendMap;
    }

    public int getDistanceFromOrigin() {
        return distanceFromOrigin;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }
}
