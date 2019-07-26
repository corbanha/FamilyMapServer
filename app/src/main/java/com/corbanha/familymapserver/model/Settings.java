package com.corbanha.familymapserver.model;

public class Settings {

    enum MapType {
        NORMAL, TERRAIN, SATELLITE, MIXED;
    }

    private boolean lifeStoryLinesEnabled = true;
    private boolean familyTreeLinesEnabled = true;
    private boolean spouseLinesEnabled = true;

    private int lifeStoryLinesSelection = 4; //Green
    private int familyTreeLinesSelection = 5; //Violet
    private int spouseLinesSelection = 6; //Azure

    private int mapTypeSelection = 1; //Normal


    public boolean isLifeStoryLinesEnabled() {
        return lifeStoryLinesEnabled;
    }

    public void setLifeStoryLinesEnabled(boolean lifeStoryLinesEnabled) {
        this.lifeStoryLinesEnabled = lifeStoryLinesEnabled;
    }

    public boolean isFamilyTreeLinesEnabled() {
        return familyTreeLinesEnabled;
    }

    public void setFamilyTreeLinesEnabled(boolean familyTreeLinesEnabled) {
        this.familyTreeLinesEnabled = familyTreeLinesEnabled;
    }

    public boolean isSpouseLinesEnabled() {
        return spouseLinesEnabled;
    }

    public void setSpouseLinesEnabled(boolean spouseLinesEnabled) {
        this.spouseLinesEnabled = spouseLinesEnabled;
    }

    public int getLifeStoryLinesSelection() {
        return lifeStoryLinesSelection;
    }

    public void setLifeStoryLinesSelection(int lifeStoryLinesSelection) {
        this.lifeStoryLinesSelection = lifeStoryLinesSelection;
    }

    public int getFamilyTreeLinesSelection() {
        return familyTreeLinesSelection;
    }

    public void setFamilyTreeLinesSelection(int familyTreeLinesSelection) {
        this.familyTreeLinesSelection = familyTreeLinesSelection;
    }

    public int getSpouseLinesSelection() {
        return spouseLinesSelection;
    }

    public void setSpouseLinesSelection(int spouseLinesSelection) {
        this.spouseLinesSelection = spouseLinesSelection;
    }

    public int getMapTypeSelection() {
        return mapTypeSelection;
    }

    public void setMapTypeSelection(int mapTypeSelection) {
        this.mapTypeSelection = mapTypeSelection;
    }
}
