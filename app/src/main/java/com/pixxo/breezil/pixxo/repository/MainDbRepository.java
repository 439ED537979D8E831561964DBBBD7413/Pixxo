package com.pixxo.breezil.pixxo.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.pixxo.breezil.pixxo.db.AppDatabase;
import com.pixxo.breezil.pixxo.db.ImagesDao;
import com.pixxo.breezil.pixxo.model.ImagesModel;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class MainDbRepository {
    private ImagesDao imagesDao;
    private LiveData<List<ImagesModel>> imageModelList;


    @Inject
    public MainDbRepository(Application application){
        AppDatabase database = AppDatabase.getAppDatabase(application);
        imagesDao = database.imagesDao();
        imageModelList = imagesDao.getImages();
    }




    public void insert(ImagesModel imagesModel){
        new InsertImages(imagesDao).execute(imagesModel);
    }

    public void deleteAllImages(){
        new DeleteAllImages(imagesDao).execute();
    }

    public LiveData<List<ImagesModel>> getImageModelList(){
        return imageModelList;
    }





    private static class InsertImages extends AsyncTask<ImagesModel, Void, Void>{
        private ImagesDao imagesDao;

        public InsertImages(ImagesDao imagesDao) {
            this.imagesDao = imagesDao;
        }

        @Override
        protected Void doInBackground(ImagesModel... imagesModels) {
            imagesDao.insert(imagesModels[0]);
            return null;
        }
    }

    private static class DeleteAllImages extends AsyncTask<Void, Void, Void>{
        private ImagesDao imagesDao;

        public DeleteAllImages(ImagesDao imagesDao) {
            this.imagesDao = imagesDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            imagesDao.deleteAllImages();
            return null;
        }
    }
}
