package com.example.androidlockpattern;

public class Dot {
    int id;
    float centerX, centerY;
    boolean selected;

    long timestamp;
    public Dot(int id, float centerX, float centerY) {
        this.id = id;
        this.centerX = centerX;
        this.centerY = centerY;
        this.selected = false;
    }

    public int getId() {
        return id;
    }

    public float getCenterX() {
        return centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

