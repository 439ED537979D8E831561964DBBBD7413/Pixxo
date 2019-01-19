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
    private ImagesApi imagesApi;
    private List<ImagesModel> imgList;


    @Inject
    public MainDbRepository(ImagesApi imagesApi, Application application){
        this.imagesApi = imagesApi;
        AppDatabase database = AppDatabase.getAppDatabase(application);
        imagesDao = database.imagesDao();
        imageModelList = imagesDao.getImages();
    }



    public void insertData(String search,String lang, String category,String order){
       ImagesApi imagesApii = new DemoClient().getClient();
        imagesApii.getImageDb(API_KEY,search,lang,category,order,1,20).enqueue(new Callback<ImagesResult>() {
            @Override
            public void onResponse(Call<ImagesResult> call, Response<ImagesResult> response) {
                 imgList = response.body().getHits();
                 for(ImagesModel imagesModel : imgList){
                    insert(imagesModel);
                 }
            }

            @Override
            public void onFailure(Call<ImagesResult> call, Throwable t) {

            }
        });
//        return imgList;
    }


    public void insertAll(ImagesModel imagesModels){
        new InsertAllImages(imagesDao).execute(imagesModels);
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



    private static class InsertAllImages extends AsyncTask<ImagesModel, Void, Void>{
        private ImagesDao imagesDao;

        public InsertAllImages(ImagesDao imagesDao) {
            this.imagesDao = imagesDao;
        }


        @Override
        protected Void doInBackground(ImagesModel... imagesModels) {
           imagesDao.insertAll(imagesModels);
           return null;
        }
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
