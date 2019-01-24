package com.example.breezil.pixxo.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.breezil.pixxo.api.DemoClient;
import com.example.breezil.pixxo.api.ImagesApi;
import com.example.breezil.pixxo.api.OkHttp;
import com.example.breezil.pixxo.db.AppDatabase;
import com.example.breezil.pixxo.db.ImagesDao;
import com.example.breezil.pixxo.model.ImagesModel;
import com.example.breezil.pixxo.model.ImagesResult;
import com.example.breezil.pixxo.model.SavedImageModel;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.breezil.pixxo.BuildConfig.API_KEY;


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
