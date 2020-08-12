package com.example.mumbaiguide;

import java.util.HashMap;

public class GuideItem {

    private static int idCounter;
    private static HashMap<Integer, GuideItem> guideItemHashMap = new HashMap<>();
    private String name;
    private String subcategory;
    private String description;
    private String neighborhood;
    private String location;
    private int id;
    private int image;
    private boolean isStarred;

    public GuideItem(String name, String subcategory, String description,
                     String neighborhood, String location, int image) {
        this.name = name;
        this.subcategory = subcategory;
        this.description = description;
        this.neighborhood = neighborhood;
        this.location = location;
        this.image = image;
        isStarred = false;
        idCounter++;
        id = idCounter;
        guideItemHashMap.put(id, this);
    }

    public static GuideItem getGuideItemById(int id) {
        if (guideItemHashMap != null) {
            return guideItemHashMap.get(id);
        } else return null;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public String getDescription() {
        return description;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getLocation() {
        return location;
    }

    public int getImage() {
        return image;
    }

    public boolean isStarred() {
        return isStarred;
    }

    public void toggleStar() {
        if (isStarred) {
            isStarred = false;
        } else {
            isStarred = true;
        }
    }
}
