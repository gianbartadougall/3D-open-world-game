package GameData;

import Entities.Entity;

import java.util.ArrayList;
import java.util.List;

public class Positions {

    private static List<Entity> grassList = new ArrayList<>();

    public Positions() {

    }

    public static void clearLists() {
        grassList.clear();
    }

    public static void addToGrassList(Entity e) {
        grassList.add(e);
    }

    public static List<Entity> getGrassList() {
        return grassList;
    }
}
