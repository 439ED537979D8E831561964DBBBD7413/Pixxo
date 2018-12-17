package com.example.breezil.pixxo.api;

import android.support.annotation.Nullable;

import com.example.breezil.pixxo.model.ImagesResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ImagesApi {

//    @GET("api/")
//    Call<ImagesResult>getImages(@Query("key")@Nullable String key,
//                              @Query("q")@Nullable String query,
//                              @Query("lang")@Nullable String lang,
//                              @Query("image_type")@Nullable String image_type,
//                              @Query("category")@Nullable String category,
//                              @Query("order")@Nullable String order,
//                              @Query("page")int page
//    );


    @GET("api/")
    Call<ImagesResult>getImages(@QueryMap Map<String, Object> parameter
    );
}


