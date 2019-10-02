package Camera;

import Display.DisplayManager;
import Entities.Entity;
import Terrains.Terrain;
import org.lwjgl.util.vector.Vector3f;

import static Enums.Enum.*;
import static ToolBox.Maths.r;

// 295 lines currently
public class CameraAI {

    public static final int ROTATE_INWARDS = 0, ROTATE_OUTWARDS = 1, BOUNCE_X = 2, BOUNCE_Z = 3,
                            AEROPLANE = 4, PLAYER = 6, EAST = 0, WEST = 1, NORTH = 2, SOUTH = 3;
    private float TIME_BEFORE_CHANGE = 3.0f, NEW_TIME_BEFORE_CHANGE, ONE_SECOND = 1.0f;
    private int CURRENT_DIRECTION;
    private float ROTATE_SPEED = 0.02f, CAMERA_SPEED = 10f, angleTurnSpeed = 10f, rollSpeed = 0.007f;

    private Point point;
    private float zoomLevel = 50, angleAroundTerrain = 0, distanceMoved = 0, averageDistancePerSecond;
    private int AIMovementType;
    private Camera camera;

    // Variables for Rotation changes
    private float maxRZRotation, minRZRotation, switchNumber;
    private float angleLeftToTurn = 0, rollAngle = 0, nextAngleToTurn;
    private float rotationSpeed = 0.1f;
    private int direction;
    private int maxRYRotation = 120, minRYRotation = 30, rzDirection;

    public boolean automatic;

    private long currentTime, lastTime;
    private double timePassed, clock2;
    private Entity entity;

    private CameraUtils utils;

    public CameraAI(Camera camera, Entity entity, Vector3f pointOfFocus, float rx, float ry, float rz) {
        this.camera = camera;
        this.entity = entity;
        utils = new CameraUtils(camera, 0.05f, 200, 40, 0.2f, 1, 80);
        point = new Point(pointOfFocus, rx, ry, rz);
        lastTime = System.currentTimeMillis()/1000;
    }

    private void applyLeftSmoothing() {
        if (angleLeftToTurn > -2) {
            rotationSpeed = 0.01f;
        } else if (angleLeftToTurn > -4) {
            rotationSpeed = 0.03f;
        } else if (angleLeftToTurn > -6) {
            rotationSpeed = 0.05f;
        } else if (angleLeftToTurn > -8) {
            rotationSpeed = 0.07f;
        } else if (angleLeftToTurn > -10) {
            rotationSpeed = 0.09f;
        } else rotationSpeed = 0.1f;
    }

    private void applyRightSmoothing() {
        if (angleLeftToTurn < 2) {
            rotationSpeed = 0.01f;
        } else if (angleLeftToTurn < 4) {
            rotationSpeed = 0.03f;
        } else if (angleLeftToTurn < 6) {
            rotationSpeed = 0.05f;
        } else if (angleLeftToTurn < 8) {
            rotationSpeed = 0.07f;
        } else if (angleLeftToTurn < 10) {
            rotationSpeed = 0.09f;
        } else rotationSpeed = 0.1f;
    }

    public void update() {
        calcCameraMovement(AIMovementType);

        if (angleLeftToTurn < 0) {
            rotateLeft();
        } else if (angleLeftToTurn > 0) {
            rotateRight();
        }
    }

    private void checkForRZBounds() {
        if (rollAngle >= maxRZRotation) {
            rollAngle = maxRZRotation;
            rzDirection = UP;
        }

        if (rollAngle <= minRZRotation) {
            rollAngle = minRZRotation;
            rzDirection = NIL;
        }
    }

    private void applyLeftRotationRealism() {
        rollAngle += (rzDirection == UP && angleLeftToTurn >= switchNumber) ? -rollSpeed :
                rzDirection == DOWN ? rollSpeed : rollAngle;
        checkForRZBounds();
    }

    private void rotateLeft() {
        applyLeftRotationRealism();
        applyLeftSmoothing();
        camera.setRy(camera.getRY() - rotationSpeed);
        updateCameraRZRotation();
        angleLeftToTurn += rotationSpeed;
        if (angleLeftToTurn > 0) {
            resetRotation();
        }
    }

    private void updateCameraRZRotation() {
        //System.out.println("direction: " + direction);
        if (direction >=0 && direction <90 || direction == 360) {
            camera.setRz(rollAngle);
        } else if (direction >= 90 && direction <180) {
            camera.setRz(rollAngle);
        } else if (direction >= 180 && direction <270) {
            camera.setRz(-rollAngle); // this one is not correct and doesnt work for opposite
        } else if (direction >= 270 && direction <360) {
            camera.setRz(-rollAngle);
        } else throw new IllegalArgumentException();
    }

    private void rotateRight() {
        applyRightRotationRealism();
        applyRightSmoothing();
        camera.setRy(camera.getRY() + rotationSpeed);
        updateCameraRZRotation();
        angleLeftToTurn -= rotationSpeed;
        if (angleLeftToTurn < 0) {
            resetRotation();
        }
    }

    private void calculateRZVariables() {
        angleLeftToTurn = CURRENT_DIRECTION;
        updateDirection();
        maxRZRotation = angleLeftToTurn / (angleLeftToTurn > 0 ? 15 : -15);
        switchNumber = angleLeftToTurn / 3;
        rzDirection = DOWN;
        rollSpeed = 0.02f;
        minRZRotation = 0.0f;
    }

    private void applyRightRotationRealism() {
        rollAngle += (rzDirection == UP && angleLeftToTurn <= switchNumber) ? -rollSpeed :
                rzDirection == DOWN ? rollSpeed : rollAngle;
        checkForRZBounds();
    }

    private void resetRotation() {
        angleLeftToTurn = 0;
        rotationSpeed = 0.1f;
    }

    private void calculateRegularCameraVariables(float horizontal, float vertical) {
        camera.getPosition().y = point.getPosition().y + vertical;
        float theta = point.getRy() + angleAroundTerrain;
        float xOffset = (float) (horizontal * Math.sin(Math.toRadians(theta)));
        float zOffset = (float) (horizontal * Math.cos(Math.toRadians(theta)));
        camera.setPosition(new Vector3f(point.getPosition().x - xOffset,
                point.getPosition().y + vertical,
                point.getPosition().z - zOffset));
    }

    public void setToAutomatic(int movement) {
        this.AIMovementType = movement;
        automatic = true;
        camera.setRy(270);
        setCameraPosition(movement);
        CURRENT_DIRECTION = SOUTH;
        direction = 270;
    }

    private void setCameraPosition(int movement) {
        camera.setPosition(movement == PLAYER ? new Vector3f(point.getPosition().x, camera.getPosition().y, point.getPosition().z) :
                                                new Vector3f(point.getPosition().x, camera.getPosition().y, point.getPosition().z));
    }

    private void calcCameraMovement(int type) {
        if (type == PLAYER) {
            point.setPosition(new Vector3f(entity.getPosition().x, entity.getPosition().y, entity.getPosition().z));
        }

        if (type != AEROPLANE) {
            zoomLevel = utils.calculateCamerasZoom(zoomLevel);
            camera.setRx(utils.calculateCamerasPitch());
            point.setRy(point.getRy() + ROTATE_SPEED);
            calculateRegularCameraVariables(utils.calculateHorizontalDistance(zoomLevel), utils.calculateVerticalDistance(zoomLevel));
            switch (type) {
                case ROTATE_INWARDS: camera.setRy(180 - (point.getRy() + angleAroundTerrain)); break;
                case ROTATE_OUTWARDS: camera.setRy(0 - (point.getRy() - angleAroundTerrain)); break;
                case BOUNCE_X: camera.setRy(180 + (point.getRy() - angleAroundTerrain)); break;
                case PLAYER: camera.setRy(180 - (point.getRy() + angleAroundTerrain)); break;
                default: camera.setRy(180 - (point.getRy() - angleAroundTerrain)); break;
            }
        } else calcAeroplane();
    }

    private void calcAeroplane() {
        currentTime = System.currentTimeMillis()/1000;
        timePassed += currentTime - lastTime;
        clock2 += currentTime - lastTime;
        lastTime = currentTime;

        if (clock2 >= ONE_SECOND) {
            averageDistancePerSecond = distanceMoved;
            //System.out.println("dist moved in last second " + averageDistancePerSecond);
            distanceMoved = 0;
            clock2 -= ONE_SECOND;
        }

        if (timePassed > TIME_BEFORE_CHANGE && angleLeftToTurn == 0) {
            // calculate two moves ahead
            boolean validDirection = false;
            while (!validDirection) {
                CURRENT_DIRECTION = calcNewAngle();
                calculateTimeBeforeChange();
                if (directionIsValid()) {
                    validDirection = true;
                } else System.out.println("Direction " + CURRENT_DIRECTION + " is not valid, calculating new direction");
            }

            calculateRZVariables();

            timePassed -= TIME_BEFORE_CHANGE;
            TIME_BEFORE_CHANGE = NEW_TIME_BEFORE_CHANGE;
        }
        moveCamera();
    }

    private int calcNewAngle() {
        int angle = r.nextInt(maxRYRotation - minRYRotation + 1) + minRYRotation;
        return  r.nextInt(2) == 0 ? -angle : angle;
    }

    private void updateDirection() {
        direction += CURRENT_DIRECTION;
        if (direction > 360) {
            direction = direction - 360;
        }
        if (direction < 0) {
            direction = 360 - Math.abs(direction);
        }
    }

    private void calculateTimeBeforeChange() {
        NEW_TIME_BEFORE_CHANGE = (r.nextInt(8 - 6 + 1) + 6) + Math.abs(CURRENT_DIRECTION * angleTurnSpeed * DisplayManager.getFrameTimeSeconds());
    }

    private void moveCamera() {
        float distance = CAMERA_SPEED * DisplayManager.getFrameTimeSeconds();
        float xDistance = (float) (distance * Math.sin(Math.toRadians(-camera.getRY())));
        float zDistance = (float) (distance * Math.cos(Math.toRadians(-camera.getRY())));

        camera.setPosX(camera.getPosition().x - xDistance);
        camera.setPosZ(camera.getPosition().z - zDistance);
        distanceMoved += calcDistanceMoved(xDistance, zDistance);
        //System.out.println("distance moved is now " + distanceMoved);
    }

    private float calcDistanceMoved(float xDistance, float zDistance) {
        return (float) Math.sqrt((xDistance * xDistance) + (zDistance * zDistance));
    }

    // Defs does not work
    private boolean directionIsValid() {
        float distance = calcDistance();
        System.out.println("Added amount " + distance);
        float xAmount = camera.getPosition().x;
        float zAmount = camera.getPosition().z;
        if (CURRENT_DIRECTION >=315 || CURRENT_DIRECTION <45) {
            zAmount += distance; // east
        } else if (CURRENT_DIRECTION <135) {
            xAmount += distance; // north
        } else if (CURRENT_DIRECTION <225) {
            zAmount -= distance; // west
        } else xAmount -= distance; // south

        System.out.println("final xAmount " + xAmount);
        System.out.println("final zAmount " + zAmount);

        return zAmount >= 0 && zAmount <= Terrain.HEIGHT && xAmount >=0 && xAmount <= Terrain.WIDTH;
    }

    // READ ME - EVERYTHING IS WORKING PRETTY WELL SO FAR ASIDE FROM THE CALC DISTANCE FUNCTION. THE ONLY
    // ISSUE IS THAT THE CAMERA WILL GO OUT OF BOUNDS AFTER A WHILE

    private float calcDistance() {
        System.out.println("new time " + NEW_TIME_BEFORE_CHANGE);
        return NEW_TIME_BEFORE_CHANGE * averageDistancePerSecond;
    }

}
