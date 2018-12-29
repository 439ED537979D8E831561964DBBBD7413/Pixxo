package com.example.breezil.pixxo.repository;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.breezil.pixxo.api.ImagesApi;
import com.example.breezil.pixxo.db.AppDatabase;
import com.example.breezil.pixxo.model.ImagesModel;
import com.example.breezil.pixxo.model.ImagesResult;
import com.example.breezil.pixxo.utils.helper.ApiResponse;
import com.example.breezil.pixxo.utils.helper.AppExecutors;
import com.example.breezil.pixxo.utils.helper.NetworkBoundResource;
import com.example.breezil.pixxo.utils.helper.RateLimiter;
import com.example.breezil.pixxo.utils.helper.Resource;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class MainRepository {
    private final ImagesApi imagesApi;

    private MediatorLiveData<List<ImagesModel>> images_list;
    AppExecutors appExecutors;
    AppDatabase database;
    RateLimiter rateLimiter;

    @Inject
    MainRepository(ImagesApi imagesApi,AppExecutors appExecutors, AppDatabase database){
        this.imagesApi = imagesApi;
        this.appExecutors = appExecutors;
        this.database = database;
        this.rateLimiter = new RateLimiter(10, TimeUnit.MINUTES);
    }



//    public LiveData<Resource<List<ImagesModel>>> getImages(Map<String, Object> parameter){
//        return new NetworkBoundResource<List<ImagesModel>, ImagesResult>(appExecutors){
//
//            @Override
//            protected void saveCallResult(@NonNull ImagesResult item) {
//                database.imagesDao().saveImages(item);
//            }
//
//            @Override
//            protected boolean shouldFetch(@Nullable List<ImagesModel> data) {
//                return data == null || data.size() == 0;
//            }
//
//            @NonNull
//            @Override
//            protected LiveData<List<ImagesModel>> loadFromDb() {
//                return  database.imagesDao().getImages();
//            }
//
//            @NonNull
//            @Override
//            protected LiveData<ApiResponse<ImagesResult>> createCall() {
//                return imagesApi.getImages(parameter);
//            }
//            @Override
//            protected void onFetchFailed() {
//                rateLimiter.reset(parameter);
//            }
//
//        }.asLiveData();
//    }

//    public LiveData<Resource<List<ImagesModel>>> getSearchImages(Map<String, Object> parameter){
//        return new NetworkBoundResource<List<ImagesModel>, ImagesResult>(appExecutors){
//
//            @Override
//            protected void saveCallResult(@NonNull ImagesResult item) {
//                database.imagesDao().saveImages(item);
//            }
//
//            @Override
//            protected boolean shouldFetch(@Nullable List<ImagesModel> data) {
//                return data == null || data.size() == 0 ;
//            }
//
//            @NonNull
//            @Override
//            protected LiveData<List<ImagesModel>> loadFromDb() {
//
//                return  database.imagesDao().getImages();
//
//            }
//
//            @NonNull
//            @Override
//            protected LiveData<ApiResponse<ImagesResult>> createCall() {
//                return imagesApi.getImages(parameter);
//            }
//
//            @Override
//            protected void onFetchFailed() {
//                rateLimiter.reset(parameter);
//            }
//        }.asLiveData();
//    }

    public LiveData<List<ImagesModel>> getImages(Map<String, Object> parameter){
        if(images_list == null ){
            images_list = new MediatorLiveData<>();

            imagesApi.getImagess(parameter).enqueue(new Callback<ImagesResult>() {
                @Override
                public void onResponse(Call<ImagesResult> call, Response<ImagesResult> response) {
                    images_list.setValue(response.body().getHits());
                }

                @Override
                public void onFailure(Call<ImagesResult> call, Throwable t) {

                }
            });
        }
       return images_list;
    }

    public LiveData<List<ImagesModel>> getSearchImages(Map<String, Object> parameter){
        if(images_list == null ){
            images_list = new MediatorLiveData<>();

            imagesApi.getImagess(parameter).enqueue(new Callback<ImagesResult>() {
                @Override
                public void onResponse(Call<ImagesResult> call, Response<ImagesResult> response) {
                    images_list.setValue(response.body().getHits());
                }

                @Override
                public void onFailure(Call<ImagesResult> call, Throwable t) {

                }
            });
        }
        return images_list;
    }

}
