package com.pixxo.breezil.pixxo.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.model.ImagesModel;
import com.pixxo.breezil.pixxo.model.ImagesResult;
import com.pixxo.breezil.pixxo.model.SavedImageModel;

@Database(entities = {SavedImageModel.class, ImagesModel.class}, version = 1, exportSchema = false)
public abstract class AppDatabase  extends RoomDatabase {
    private static AppDatabase appDatabase;
    public abstract SavedDao savedDao();
    public abstract ImagesDao imagesDao();

    public static synchronized AppDatabase getAppDatabase(Context context){
        if(appDatabase == null){
            appDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, context.getString(R.string.pixxo_db))
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return appDatabase;
    }
}
