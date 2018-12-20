package com.example.breezil.pixxo.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.breezil.pixxo.model.ImagesModel;
import com.example.breezil.pixxo.model.ImagesResult;
import com.example.breezil.pixxo.model.SavedImageModel;

@Database(entities = {SavedImageModel.class, ImagesResult.class, ImagesModel.class}, version = 1)
public abstract class AppDatabase  extends RoomDatabase {
    private static AppDatabase appDatabase;
    public abstract SavedDao savedDao();
    public abstract ImagesDao imagesDao();

    public static synchronized AppDatabase getAppDatabase(Context context){
        if(appDatabase == null){
            appDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "pixxo.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return appDatabase;
    }
}
