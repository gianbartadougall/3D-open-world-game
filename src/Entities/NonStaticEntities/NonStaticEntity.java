package Entities.NonStaticEntities;

import Entities.Entity;
import Model.TexturedModel;
import Terrains.Terrain;
import org.lwjgl.util.vector.Vector3f;

public abstract class NonStaticEntity extends Entity {

    public NonStaticEntity(TexturedModel texture) {
        super(texture);
    }

    public NonStaticEntity(Vector3f position, float rx, float ry, float rz, float scale, TexturedModel texture, int ID) {
        super(position, rx, ry, rz, scale, texture, ID);
    }

    public abstract void update(Terrain terrain);
}
