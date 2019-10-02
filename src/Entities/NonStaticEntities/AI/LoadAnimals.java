package Entities.NonStaticEntities.AI;

import Entities.NonStaticEntities.NonStaticEntity;
import Loader.Loader;
import Model.ModelsPackage;
import Model.TexturedModel;
import Textures.ModelTexture;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class LoadAnimals {

    public static List<NonStaticEntity> generateNonStaticEntities(Loader loader, ModelsPackage modelsPackage) {
        List<NonStaticEntity> entities = new ArrayList<>();

        for (int i=0; i<0; i++) {
            Pig pig = new Pig(new TexturedModel(modelsPackage.get("lowPolyPig"),
                    new ModelTexture(loader.loadTexture("pigTexture", Loader.ENTITY), 10, 1)));
            pig.setAI(pig);
            pig.setPosition(new Vector3f((float) (i+5) * 20, -40, (float) (i+5) * 20));
            entities.add(pig);
        }

        return entities;
    }

}
