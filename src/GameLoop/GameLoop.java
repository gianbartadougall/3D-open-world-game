package GameLoop;

import Camera.Camera;
import Display.DisplayManager;
import Entities.Entity;
import Entities.NonStaticEntities.AI.LoadAnimals;
import Entities.NonStaticEntities.Player;
import Entities.StaticEntities.LoadStaticEntities;
import Entities.StaticEntities.StaticEntity;
import GUIS.GUIRenderer;
import GUIS.GUITexture;
import GameData.Positions;
import Lights.Light;
import Lights.LoadLights;
import Loader.Loader;
import Model.ModelsPackage;
import Model.TexturedModel;
import Rendering.BaseRenderer;
import Shaders.Water.WaterShader;
import Terrains.LoadTerrains;
import Terrains.Terrain;
import Textures.ModelTexture;
import Water.WaterFrameBuffer;
import Water.WaterRenderer;
import Water.WaterTile;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static Enums.Enum.GRASS;

public class GameLoop {

    public static void main(String[] args) {

        new DisplayManager("Genesis Version 0.2");
        Loader loader = new Loader();
        BaseRenderer renderer = new BaseRenderer();
        ModelsPackage modelsPackage = new ModelsPackage(loader);

        List<Terrain> terrains = LoadTerrains.generateTerrains(loader);

        List<Entity> entities = new ArrayList<>();
        entities.addAll(StaticEntity.randomizeStaticEntities(terrains.get(0), LoadStaticEntities.generateStaticEntities(loader, modelsPackage)));
        entities.addAll(LoadAnimals.generateNonStaticEntities(loader, modelsPackage));

        Player player = new Player(new Vector3f(80, -20, 80), 0, 0, 0, 5f, new TexturedModel(modelsPackage.get("lowPolyPig"),
                new ModelTexture(loader.loadTexture("pigTexture", Loader.ENTITY), 10, 1)), 3);
        entities.add(player);

        List<Light> lights = LoadLights.generateLights();
        Camera camera = new Camera(player);

        List<GUITexture> guis = new ArrayList<>();
        GUITexture gui = new GUITexture(loader.loadTexture("evilOctopus", Loader.ENTITY), new Vector2f(0.8f, 0.8f), new Vector2f(0.1f, 0.1f));
        ///guis.add(gui);
        GUIRenderer guiRenderer = new GUIRenderer(loader);

        WaterFrameBuffer waterFrameBuffer = new WaterFrameBuffer();

        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), waterFrameBuffer);
        List<WaterTile> waterTiles = new ArrayList<>();
        WaterTile water = new WaterTile(400, 400, -25, 150);
        waterTiles.add(water);

        boolean pause = false;

        while (!Display.isCloseRequested()) {
            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                pause = true;
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
                pause = false;
            }

            if (!pause) {
                for (Entity e : entities) {
                    e.update(terrains.get(0));
                    if (e.getID() == GRASS) {
                        Positions.addToGrassList(e);
                    }
                }
                camera.update();

                // rendering reflection texture
                waterFrameBuffer.bindReflectionFrameBuffer();
                float distance = 2* (camera.getPosition().y - water.getHeight());
                camera.getPosition().y -= distance;
                camera.invertPitch();
                renderer.render(camera, entities, terrains, lights, new Vector4f(0, 1, 0, -water.getHeight()));
                camera.getPosition().y += distance;
                camera.invertPitch();

                // rendering refraction texture
                waterFrameBuffer.bindRefractionFrameBuffer();
                renderer.render(camera, entities, terrains, lights, new Vector4f(0, -1, 0, water.getHeight()));

                // rendering regular scene
                waterFrameBuffer.unbindCurrentFrameBuffer();
                renderer.render(camera, entities, terrains, lights, new Vector4f(0, -1, 0, 10000));
                waterRenderer.render(waterTiles, camera);

                guiRenderer.renderGUI(guis);
            }
            DisplayManager.updateDisplay();
            Positions.clearLists();
        }

        guiRenderer.cleanUp();
        waterFrameBuffer.cleanUp();
        waterShader.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }


}
