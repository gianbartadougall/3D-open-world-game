package Entities.NonStaticEntities;

import Display.DisplayManager;
import Model.TexturedModel;
import Terrains.Terrain;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Player extends NonStaticEntity {

    public static final float RUN_SPEED = 150, TURN_SPEED = 160, GRAVITY = -50, JUMP_POWER = 30;
    private float currentSpeed = 0, currentTurnSpeed = 0, upwardSpeed = 0;
    private boolean isInAir = false;

    public Player(TexturedModel texture) {
        super(texture);
        position = new Vector3f(0, 0, 0);
        rx = 0;
        ry = 0;
        rz = 0;
        scale = 1;
    }

    public Player(Vector3f position, float rx, float ry, float rz, float scale, TexturedModel texture, int ID) {
        super(position, rx, ry, rz, scale, texture, ID);
    }

    @Override
    public void update(Terrain terrain) {
        checkForInput();
        updateMovement();
        applyGravity(terrain);
        updateGrid();
    }

    private void updateMovement() {
        increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(getRY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(getRY())));
        increasePosition(dx, 0, dz);
    }

    private void applyGravity(Terrain terrain) {
        upwardSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
        increasePosition(0, upwardSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        float terrainHeight = terrain.getHeightOfTerrain(getPosition().x, getPosition().z);
        if (getPosition().y < terrainHeight) {
            getPosition().y = terrainHeight;
            isInAir = false;
        }
    }

    private void jump() {
        if (!isInAir) {
            upwardSpeed = JUMP_POWER;
            isInAir = true;
        }
    }

    private void checkForInput() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            this.currentSpeed = RUN_SPEED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            this.currentSpeed = -RUN_SPEED;
        } else currentSpeed = 0;

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            this.currentTurnSpeed = TURN_SPEED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            this.currentTurnSpeed = -TURN_SPEED;
        } else currentTurnSpeed = 0;

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            jump();
        }
    }
}
