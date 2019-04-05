package com.pixxo.breezil.pixxo.model;

import android.graphics.Bitmap;

public class EditedModel {
    private String path;
    private boolean isDirectory;
    private Bitmap image;

    public EditedModel(String path, boolean isDirectory, Bitmap image) {
        this.path = path;
        this.isDirectory = isDirectory;
        this.image = image;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
