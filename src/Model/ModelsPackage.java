package Model;

import Loader.Loader;
import Loader.OBJLoader;

public class ModelsPackage {

    private Loader loader;
    private static OBJLoader objLoader = new OBJLoader();

    public ModelsPackage(Loader loader) {
        this.loader = loader;
    }

    public RawModel get(String fileName) {
        ModelData data = objLoader.loadOBJ(fileName);
        return loader.loadToVao(data.getVertices(), data.getModelIndices(), data.getTextureCoords(), data.getNormals());
    }

}
