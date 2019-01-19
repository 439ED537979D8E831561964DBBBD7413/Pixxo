package com.example.breezil.pixxo.db;


import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.example.breezil.pixxo.model.ImagesModel;
import java.util.List;

@Dao
public interface ImagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(ImagesModel... imagesModels);

    @Insert
    void insert(ImagesModel imagesModel);

    @Transaction
    @Query("SELECT * FROM images_model_table ORDER BY roomId DESC ")
    LiveData<List<ImagesModel>> getImages();

    @Transaction
    @Query("SELECT * FROM images_model_table ORDER BY roomId DESC ")
    DataSource.Factory<Integer, ImagesModel> getPagedImages();

    @Query("DELETE FROM images_model_table")
    void deleteAllImages();
}
