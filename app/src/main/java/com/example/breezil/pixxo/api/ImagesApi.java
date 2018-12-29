package com.example.breezil.pixxo.api;

import android.arch.lifecycle.LiveData;
import android.support.annotation.Nullable;

import com.example.breezil.pixxo.model.ImagesResult;
import com.example.breezil.pixxo.utils.helper.ApiResponse;

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

    enum Category {
        NATURE("nature");

        private String category;

        Category(String category) {
            this.category = category;
        }

        public String getValue() {
            return category;
        }
    }

    enum Search {
        NATURE("nature");

        private String search;

        Search(String search) {
            this.search = search;
        }

        public String getValue() {
            return search;
        }
    }


//    @GET("api/")
//    LiveData<ApiResponse<ImagesResult>> getImages(@QueryMap Map<String, Object> parameter
//    );


    @GET("api/")
    Call<ImagesResult> getImagess(@QueryMap Map<String, Object> parameter
    );

    @GET("api/")
    Call<ImagesResult> getImages(@Query("key")@Nullable String key,
                                   @Query("page")int page,
                                   @Query("per_page")int per_page
    );
}


