package com.example.breezil.pixxo.repository;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.example.breezil.pixxo.api.ImagesApi;
import com.example.breezil.pixxo.model.ImagesModel;
import com.example.breezil.pixxo.model.ImagesResult;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class MainRepository {
    private final ImagesApi imagesApi;

    private MediatorLiveData<List<ImagesModel>> images_list;
    private ImagesResult images_listo;

    @Inject
    MainRepository(ImagesApi imagesApi){
        this.imagesApi = imagesApi;
    }

    public LiveData<List<ImagesModel>> getImages(Map<String, Object> parameter){
        if(images_list == null ){
            images_list = new MediatorLiveData<>();

            imagesApi.getImages(parameter).enqueue(new Callback<ImagesResult>() {
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
