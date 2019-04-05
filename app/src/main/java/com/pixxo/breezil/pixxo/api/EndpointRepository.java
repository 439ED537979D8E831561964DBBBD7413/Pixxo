package com.pixxo.breezil.pixxo.api;

import com.pixxo.breezil.pixxo.model.ImagesResult;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.pixxo.breezil.pixxo.BuildConfig.API_KEY;

public class EndpointRepository {

    private ImagesApi imagesApi;

    @Inject
    public EndpointRepository(ImagesApi imagesApi) {
        this.imagesApi = imagesApi;
    }

    public Single<ImagesResult> getImages(String search,String lang, String category, String order,int page, int per_page){
        return imagesApi.getImages(API_KEY,search,lang, category,order, page, per_page)
                .retry(3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
}
