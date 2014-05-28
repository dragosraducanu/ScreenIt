package com.dragos.androidfilepicker.library.core;

/**
 * Created by dragos on 11.05.2014.
 */
public class ImageSize {
    private int width;
    private int height;

    public ImageSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    public int getWidth() {
        return this.width;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getHeight(){
        return this.height;
    }

}
