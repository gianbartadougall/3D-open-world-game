package Entities.NonStaticEntities.AI;

import AI.Creature.CreatureAI;
import Entities.NonStaticEntities.NonStaticEntity;
import Model.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

public abstract class Animal extends NonStaticEntity {

    protected int mode;
    protected float speed = 40;

    protected CreatureAI AI;

    public Animal(TexturedModel texture) {
        super(texture);
        this.AI = null;
        mode = 0;
    }

    public Animal(Vector3f position, float rx, float ry, float rz, float scale, TexturedModel texture, int ID) {
        super(position, rx, ry, rz, scale, texture, ID);
        this.AI = null;
        mode = 0;
    }

    public int getMode() {
        return mode;
    }

    public float getSpeed() {
        return speed;
    }
}
