package com.example.breezil.pixxo.api;

import android.support.annotation.Nullable;

import com.example.breezil.pixxo.model.ImagesResult;

import java.util.Map;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ImagesApi {


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

    enum Language{
        ;
        private String lang;

        Language(String lang) {
            this.lang = lang;
        }
    }
    enum Order {
        ;
        private String order;

        Order(String order) {
            this.order = order;
        }
    }



    @GET("api/")
    Single<ImagesResult> getImages(@Query("key")@Nullable String key,
                                   @Query("q")@Nullable String search,
                                   @Query("lang")@Nullable String lang,
                                   @Query("category")@Nullable String category,
                                   @Query("order")@Nullable String order,
                                   @Query("page")int page,
                                   @Query("per_page")int per_page
    );


}


