package com.example.breezil.pixxo.db;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.example.breezil.pixxo.model.ImageView;
import com.example.breezil.pixxo.model.ImagesModel;
import com.example.breezil.pixxo.model.ImagesResult;

import java.util.List;

@Dao
public abstract class ImagesDao {
    public void saveImages(ImagesResult imagesResult){
        List<ImagesModel> imagesModelList = imagesResult.getHits();
        for(ImagesModel imagesModel:imagesModelList){
            imagesModel.setImageId(imagesResult.getId());

        }
        insertImageModel(imagesModelList);
        insertImageResult(imagesResult);
    }

    @Transaction
    @Query("SELECT * FROM ImagesModel ORDER BY roomId DESC ")
    public abstract LiveData<List<ImagesModel>> getImages();

    @Transaction
    @Query("SELECT * FROM ImagesResult WHERE id = :id")
    public abstract ImageView getImage(int id);

    @Insert
    public abstract void insertImageResult(ImagesResult imagesResult);

    @Insert
    public abstract void insertImageModel(List<ImagesModel> imagesModels);
}
