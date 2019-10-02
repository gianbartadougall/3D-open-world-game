package Terrains;

import Loader.Loader;
import Textures.LoadTextures;
import Textures.TerrainTexture;

import java.util.ArrayList;
import java.util.List;

public class LoadTerrains {

    public static List<Terrain> generateTerrains(Loader loader) {
        List<Terrain> terrains = new ArrayList<>();
        List<TerrainTexture> terrainTextures = LoadTextures.generateTerrainTextures(loader);
        // you need to change the blendmap in the terrain loader for it to work if changing blendMap texture
        terrains.add(new Terrain(0, 0, loader, terrainTextures, "heightMapPond", "blendMapFlat"));
        return terrains;
    }

}
