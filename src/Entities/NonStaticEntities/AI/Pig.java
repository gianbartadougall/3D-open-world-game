package Entities.NonStaticEntities.AI;

import AI.Creature.PigAI;
import Model.TexturedModel;
import Terrains.Terrain;
import org.lwjgl.util.vector.Vector3f;

public class Pig extends Animal {

    private float rotationSpeed = 0.5f, walkSpeed = 0.1f;
    public static final int WALK = 0;
    public int movement;
    private float speed;

    // AI chooses what to do, the pig class does it i.e ai chooses to making pig start walking so
    // sets walking mode on in pig class which causes pig to start walking


    public Pig(TexturedModel model) {
        super(model);
        position = new Vector3f(400, 0, 400);
        scale = 5;
        speed = 60;
    }

    @Override
    public void update(Terrain terrain) {
        AI.update(position, terrain);
        float terrainHeight = terrain.getHeightOfTerrain(position.x, position.z);
        if (position.y < terrainHeight) {
            position.y = terrainHeight;
            AI.setInAir(false);
        }
        updateGrid();
    }

    public int getMovement() {
        return movement;
    }

    public void setAI(Pig pig) {
        AI = new PigAI(pig);
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public float getWalkSpeed() {
        return walkSpeed;
    }
}
