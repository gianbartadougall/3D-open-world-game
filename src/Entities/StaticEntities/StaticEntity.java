package Entities.StaticEntities;

import Entities.Entity;
import Model.TexturedModel;
import Terrains.Terrain;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class StaticEntity extends Entity {

    protected Color[] validTerrains;
    protected int texturedModel, minScale, maxScale;
    protected float density, shine, reflectivity, minHeight, maxHeight;

    private static HashMap<Integer, TexturedModel> models;

    public StaticEntity(Vector3f position, TexturedModel texture, float scale, int ID) {
        super(position, 0, 0, 0, scale, texture, ID);
        models = new HashMap<>();
    }

    public static List<StaticEntity> randomizeStaticEntities(Terrain terrain, List<StaticEntity> entities) {
        double startTime = System.currentTimeMillis();
        Random random = new Random();
        double scalar = Terrain.WIDTH / terrain.getBlendMap().getWidth();
        List<StaticEntity> list = new ArrayList<>();

        for (int i = 0; i < entities.size(); i++) {
            for (int j = 0; j < entities.get(i).density; j++) {
                StaticEntity e = entities.get(i);
                boolean validLocation = false;
                double x, z;
                while (!validLocation) {
                    x = random.nextFloat() * terrain.getDistanceFromOrigin();
                    z = random.nextFloat() * terrain.getDistanceFromOrigin();
                    double scaledX = x / scalar;
                    double scaledZ = z / scalar;

                    if (scaledX<0 || scaledX>= terrain.getBlendMap().getHeight() || scaledZ<0 || scaledZ>=terrain.getBlendMap().getHeight()) {
                        continue;
                    }

                    float height = terrain.getHeightOfTerrain((float) x, (float) z);
                    if (height < entities.get(i).minHeight || height > entities.get(i).maxHeight) {
                        continue;
                    }

                    Color c = new Color(terrain.getBlendMap().getRGB((int) scaledX, (int) scaledZ));
                    //System.out.println(c.getRed() + " " + c.getBlue() + " " + c.getGreen());

                    for (int color = 0; color < entities.get(i).validTerrains.length; color++) {
                        if (c.getRed() == e.validTerrains[color].getRed() &&
                                c.getBlue() == e.validTerrains[color].getGreen() &&
                                c.getGreen() == e.validTerrains[color].getBlue()) {

                            float scale = random.nextInt(entities.get(i).maxScale - entities.get(i).minScale + 1) + entities.get(i).minScale;

                            list.add(new StaticEntity(new Vector3f((float) x, terrain.getHeightOfTerrain((float) x, (float) z), (float) z),
                                    entities.get(i).getTexturedModel(), scale, e.getID()));
//                            list.add(new StaticEntity(new Vector3f((float) x, terrain.getHeightOfTerrain((float) x, (float) z), (float) z),
//                                    entities.get(i).getTexturedModel(), scale, e.getID()));
                            validLocation = true;
                        }
                    }
                }
            }
        }
        double totalTime = System.currentTimeMillis() - startTime;
        System.out.println("It took " + totalTime + " milliseconds to randomize the static entities on the terrain");

        return list;
    }

    @Override
    public void update(Terrain terrain) {

    }
}
