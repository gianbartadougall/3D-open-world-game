package Lights;

import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class LoadLights {

    public static List<Light> generateLights() {
        List<Light> lights = new ArrayList<>();
        lights.add(new Light(new Vector3f(2000, 2000, 2000), new Vector3f(1f, 1f, 1f),
                1f));
        lights.add(new Light(new Vector3f(400, 10, 400), new Vector3f(1f, 0f, 0f),
                3.8f, new Vector3f(1, 0.01f, 0.001f)));
        lights.add(new Light(new Vector3f(50, 10, 400), new Vector3f(0f, 1f, 0f),
                3.8f, new Vector3f(1, 0.01f, 0.001f)));
        lights.add(new Light(new Vector3f(100, 10, 100), new Vector3f(0f, 0f, 1f),
                7.8f, new Vector3f(1, 0.01f, 0.001f)));
        return lights;
    }

}
