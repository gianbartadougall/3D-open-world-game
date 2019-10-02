package Display;

import ToolBox.General;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;

public class DisplayManager {

    private static final int MILLI_SECOND = 1_000_000_000 / 1000;
    public static final int WIDTH = 1600, HEIGHT = 900;
    private static final int FPS_CAP = 120;
    private static long lastFrameTime;
    private static float delta;

    public DisplayManager(String title) {

        ContextAttribs attributes = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);

        try {
            //Display.setLocation(2000, 70);
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), attributes);
            Display.setTitle(title);
        } catch (LWJGLException e) {
            General.activateError(e, "Could not create Display", 1);
        }

        GL11.glViewport(0, 0, WIDTH, HEIGHT);
        lastFrameTime = getCurrentTime();
    }

    private static long getCurrentTime() {
        return Sys.getTime()*1000/Sys.getTimerResolution();
    }

    public static void updateDisplay() {
        Display.sync(FPS_CAP); // this sets the game to run at a steady fps rate
        org.lwjgl.opengl.Display.update();
        long currentFrameTime = getCurrentTime();
        delta = (currentFrameTime - lastFrameTime)/1000f; // gets time it took for frame to render in seconds by dividing by 1000
        lastFrameTime = currentFrameTime;
    }

    public static float getFrameTimeSeconds() {
        // delta is just the time it took to render the frame in seconds
        return delta;
    }

    public static void closeDisplay() {
        org.lwjgl.opengl.Display.destroy();
    }

}
