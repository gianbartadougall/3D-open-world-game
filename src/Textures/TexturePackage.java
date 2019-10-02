package Textures;

import java.util.List;

public class TexturePackage {

    private List<TerrainTexture> textures;

    public TexturePackage(List<TerrainTexture> textures) {
        this.textures = textures;
    }

    public void addTexture(TerrainTexture texture) {
        textures.add(texture);
    }

    public List<TerrainTexture> getTextures() {
        return textures;
    }
}
