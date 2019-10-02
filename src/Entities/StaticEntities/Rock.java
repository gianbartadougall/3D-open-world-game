package Entities.StaticEntities;

import Enums.Enum;
import Model.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;

public class Rock extends StaticEntity {



    public Rock(Vector3f position, TexturedModel texture, int minScale, int maxScale, float density, float shine, float reflectivity, Color[] validTerrains,
                float minHeight, float maxHeight, int ID) {
        super(position, texture, minScale, ID);
        this.minScale = minScale;
        this.maxScale = maxScale;
        this.shine = shine;
        this.reflectivity = reflectivity;
        this.density = density;
        this.validTerrains = validTerrains;
        this.texturedModel = Enum.ROCK;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }
}
