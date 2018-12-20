package com.example.breezil.pixxo.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class ImageView {
    @Embedded
    public ImagesResult imagesResult;

    @Relation(parentColumn = "id", entityColumn = "imageId")
    public List<ImagesModel> imagesModels;

}
