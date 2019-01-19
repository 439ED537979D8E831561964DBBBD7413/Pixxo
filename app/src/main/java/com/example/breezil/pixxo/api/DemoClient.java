package com.example.breezil.pixxo.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.breezil.pixxo.BuildConfig.BASE_URL;

public class DemoClient {
    OkHttp okHttp = new OkHttp();
    ImagesApi imagesApi;

    public ImagesApi getClient(){
        Retrofit client = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp.getClient())
                .build();
        imagesApi = client.create(ImagesApi.class);
        return imagesApi;
    }
}
