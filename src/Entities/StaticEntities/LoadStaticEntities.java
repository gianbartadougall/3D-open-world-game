package Entities.StaticEntities;

import Enums.Enum;
import Loader.Loader;
import Model.ModelsPackage;
import Model.TexturedModel;
import Textures.ModelTexture;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LoadStaticEntities {

    public static List<StaticEntity> generateStaticEntities(Loader loader, ModelsPackage modelsPackage) {
        List<StaticEntity> entities = new ArrayList<>();
//        Color[] validRockTerrains = new Color[] {new Color(255, 0, 0), new Color(0, 0, 0)};
//        entities.add(new Rock(null, new TexturedModel(modelsPackage.get("rockPile"),
//                     new ModelTexture(loader.loadTexture("rockTexture", Loader.ENTITY),
//                     10, 1)), 7, 10, 1f,
//                     10f, 1f, validRockTerrains, -100, 25, 0));

        Color[] validGrassTerrains = new Color[] {new Color(0, 0, 0)};
        entities.add(new Grass(null, new TexturedModel(modelsPackage.get("lowPolyGrass"),
                     new ModelTexture(loader.loadTexture("tree", Loader.ENTITY),
                     10, 1)), 1, 1, 1f,
                     10, 1, validGrassTerrains, -100, 25, Enum.GRASS));

        Color[] validTreeTerrains = new Color[] {new Color(0, 0, 0)};
        entities.add(new Tree(null, new TexturedModel(modelsPackage.get("tree"),
                     new ModelTexture(loader.loadTexture("tree", Loader.ENTITY),
                     10, 1)), 7, 10, 100f,
                     10f, 1f, validTreeTerrains, -15, 50, 2));

        return entities;
    }


}
