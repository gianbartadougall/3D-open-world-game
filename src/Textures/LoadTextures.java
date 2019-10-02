package Textures;

import Loader.Loader;

import java.util.ArrayList;
import java.util.List;

public class LoadTextures {

    public static List<TerrainTexture> generateTerrainTextures(Loader loader) {
        List<TerrainTexture> terrainTextures = new ArrayList<>();
        terrainTextures.add(new TerrainTexture(loader.loadTexture("texturePebbledWall", Loader.TERRAIN)));
        terrainTextures.add(new TerrainTexture(loader.loadTexture("textureSand", Loader.TERRAIN)));
        terrainTextures.add(new TerrainTexture(loader.loadTexture("textureCobbleStone", Loader.TERRAIN)));
        terrainTextures.add(new TerrainTexture(loader.loadTexture("terrainGrass", Loader.TERRAIN)));
        terrainTextures.add(new TerrainTexture(loader.loadTexture("blendMapFlat", Loader.TERRAIN)));
        return terrainTextures;
    }


}
