package com.example.breezil.pixxo.db;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.breezil.pixxo.model.SavedImageModel;

import java.util.List;

@Dao
public interface SavedDao {
    @Insert
    void insert(SavedImageModel savedImageModel);

    @Delete
    void delete(SavedImageModel savedImageModel);

    @Query("DELETE FROM save_image_table")
    void deleteAllSaved();

    @Query("SELECT * FROM save_image_table ORDER BY saved_id DESC")
    LiveData<List<SavedImageModel>> getAllSaved();

    @Query("SELECT * FROM save_image_table WHERE saved_id = :saved_id")
    LiveData<SavedImageModel> getSaveById(int saved_id);
}
