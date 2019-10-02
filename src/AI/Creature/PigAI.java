package AI.Creature;

import Display.DisplayManager;
import Entities.Entity;
import Entities.NonStaticEntities.AI.Pig;
import GameData.Positions;
import Terrains.Terrain;
import ToolBox.Maths;
import org.lwjgl.util.vector.Vector3f;

import static ToolBox.Maths.lengthOfLine;

public class PigAI extends CreatureAI {

    private static final float SPEED = 10;
    private int currentActivity = IDLE, currentActivityTimeFrame, timeFinishingMoving, timeBeforeMoving;
    private double nextDecisionClock, currentActivityClock, currentTime, lastTime = System.currentTimeMillis(), nextDecision = 7000;
    private Vector3f aPosition, pointOfFocus = null;
    private Pig pig;
    private Terrain terrain;

    public PigAI(Pig pig) {
        super(pig.getPosition(), pig.getRX(), pig.getRY(), pig.getRZ());
        this.pig = pig;
        pig.increaseRotation(0, 0, 0);
        calculateFutureMovement();
    }

    @Override
    public void update(Vector3f position, Terrain terrain) {
        this.terrain = terrain;
        this.aPosition = position;
        updateMove();
        applyGravity();
    }

    private void updateMove() {
        currentTime = System.currentTimeMillis();
        nextDecisionClock += currentTime - lastTime;
        currentActivityClock += currentTime - lastTime;
        lastTime = currentTime;

        // every 4-7 seconds make a decision on what to do next
        // System.out.println(pointOfFocus);
        if (nextDecisionClock > nextDecision) {
            currentActivity = calculateNextActivity();
            resetClock();
        }

        if (pointOfFocus == null) {
            if (currentActivityClock > currentActivityTimeFrame) {
                currentActivity = IDLE;
            }
        }

        checkIfFoodIsNearBy();

        switch (currentActivity) {
            case WALK: moveAnimal(); break;
            case ROTATE: pig.increaseRotation(0, pig.getRotationSpeed(), 0); break;
            case EAT: System.out.println("eating"); break;
            case POINT_OF_FOCUS: walkToPointOfFocus();
            default:
        }

    }

    private void walkToPointOfFocus() {
//        System.out.println("Moving Animal");
        moveAnimal();
    }

    private void resetClock() {
        nextDecisionClock = 0;
        currentActivityClock = 0;
    }

    private int calculateNextActivity() {
        if (pointOfFocus != null) {
            return currentActivity;
        }

        int decision = currentActivity == IDLE ? Maths.r.nextInt(120 - 40 + 1) + 40 : Maths.r.nextInt(120);
        currentActivityTimeFrame = Maths.r.nextInt(5000 - 1000 + 1) + 1000;
        return decision < 40 ? IDLE : decision < 80 ? WALK : decision < 100 ? ROTATE : EAT;
    }

//    private void checkForFood() {
//        if (pointOfFocus == null) {
//            for (Entity e : Positions.getGrassList()) {
//                if (foodIsWithinRange(e)) {
//
//                    //pointOfFocus = e.getPosition();
//                    currentActivity = POINT_OF_FOCUS;
//
//                    double radius = lengthOfLine(pig.getPosition().x, e.getPosition().x, pig.getPosition().z, e.getPosition().z);
//                    double adjacent = lengthOfLine(pig.getPosition().x, pig.getPosition().z, pig.getPosition().x, e.getPosition().z);
//                    double angle = (Math.acos(adjacent/radius) * 180 / Math.PI);
//                    pig.setRy((float) angle);
//
//                    float distance = SPEED * DisplayManager.getFrameTimeSeconds();
//                    float dx = (float) (distance * Math.cos(pig.getRX()));
//                    float dz = (float) (distance * Math.sin(pig.getRX()));
//
//                    pig.setXPos(pig.getRX() - dx);
//                    pig.setZPos(pig.getRZ() - dz);
//                }
//            }
//        }
//    }

    private void checkIfFoodIsNearBy() {
        if (pointOfFocus == null) {
            for (Entity e : Positions.getGrassList()) {
                if (foodIsWithinRange(e)) {
                    // find the angle between the pigs current rotation and position and the grasses position
                    System.out.println("pig: x " + pig.getPosition().x + " z " + pig.getPosition().z);
                    System.out.println("grass: x " + e.getPosition().x + " z " + e.getPosition().z);
                    double radius = lengthOfLine(pig.getPosition().x, e.getPosition().x, pig.getPosition().z, e.getPosition().z);
                    double adjacent = lengthOfLine(pig.getPosition().x, pig.getPosition().z, pig.getPosition().x, e.getPosition().z);
                    double angle = (Math.acos(adjacent/radius) * 180 / Math.PI);
                    System.out.println("r " + radius + " a " + adjacent);
                    System.out.println("angle was " + angle);
                    pointOfFocus = e.getPosition();
                    currentActivity = POINT_OF_FOCUS;
                    System.out.println("food within range, walking towards food, setting angle to " + angle);
                    pig.setRy((float) angle);
                }
            }
        }
    }

    private boolean foodIsWithinRange(Entity e) {
        return true;
        //System.out.println("GRASS " + e.getGrid());
        //aSystem.out.println("ANIMAL " + animal.getGrid());
//        int aMinX = (int) pig.getGrid().x - 1;
//        int aMinZ = (int) pig.getGrid().y - 1;
//        if (aMinX >= e.getGrid().x && e.getGrid().x <= aMinX+1 &&
//            aMinZ >= e.getGrid().y && e.getGrid().y <= aMinZ+1) {
//            return true;
//        } else return false;
    }

    private void applyGravity() {
        upwardSpeed += gravity * DisplayManager.getFrameTimeSeconds();
        float terrainHeight = terrain.getHeightOfTerrain(aPosition.x, aPosition.z);
        if (aPosition.y <= terrainHeight) {
            aPosition.y = terrainHeight;
        } else {
            pig.increasePosition(0, upwardSpeed, 0);
            setInAir(true);
        }
    }

    private void calculateFutureMovement() {
        timeBeforeMoving = (Maths.r.nextInt(10) * 1000);
        timeFinishingMoving = (Maths.r.nextInt(timeBeforeMoving+5 - timeBeforeMoving + 1) + timeBeforeMoving);
    }

    private boolean noDanger() {
        return true;
    }

    private void moveAnimal() {
        float distance = pig.getSpeed() * DisplayManager.getFrameTimeSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(pig.getRY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(pig.getRY())));
        if (withinBounds(dx, dz)) {
            pig.increasePosition(dx, 0, dz);
        }
    }

    private boolean withinBounds(float dx, float dz) {
        float futureX = position.x + dx;
        float futureZ = position.z + dz;
        return futureX >=0 && futureX <=800 && futureZ >=0 && futureZ <=800;
    }

}
