package com.example.breezil.pixxo.repository;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import com.example.breezil.pixxo.api.DemoClient;
import com.example.breezil.pixxo.api.ImagesApi;
import com.example.breezil.pixxo.api.NetworkState;
import com.example.breezil.pixxo.model.ImagesModel;
import com.example.breezil.pixxo.model.ImagesResult;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.breezil.pixxo.BuildConfig.API_KEY;

@Singleton
public class ImageModelDataSource extends PageKeyedDataSource<Integer, ImagesModel> {
    public static final int PAGE_SIZE = 50;
    public static final int FIRST_PAGE = 1;
    public static final String SITE = "";
    ImagesApi imagesApi;
    private String mSearch;
    private String mCategory;

    private final MutableLiveData<NetworkState> mNetworkState;
    private final MutableLiveData<NetworkState> mInitialLoading;
    DemoClient.RestApiInterface restApiInterface = DemoClient.getClient();

    @Inject
    public ImageModelDataSource(ImagesApi imagesApi) {
        this.imagesApi = imagesApi;
        mNetworkState = new MutableLiveData<>();
        mInitialLoading = new MutableLiveData<>();
    }

    public MutableLiveData getNetworkState() {
        return mNetworkState;
    }

    public MutableLiveData getInitialLoading() {
        return mInitialLoading;
    }

    public void setSearch(String search){
        mSearch = search;
    }
    public String getSearch(){
        return mSearch;
    }
    public void setCategory(String category){
        mCategory = category;
    }
    public String getCategory(){
        return mCategory;
    }



    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, ImagesModel> callback) {

        restApiInterface.getImages(API_KEY,getSearch(),getCategory(),1,5).enqueue(new Callback<ImagesResult>() {
            @Override
            public void onResponse(Call<ImagesResult> call, Response<ImagesResult> response) {
                if(response.isSuccessful()) {
                    if(response.body() != null){
                        callback.onResult(response.body().getHits(),null,FIRST_PAGE +1);
                    }
                }else {
                    mNetworkState.postValue(NetworkState.LOADED);
                    mInitialLoading.postValue(NetworkState.LOADED);
                }

            }

            @Override
            public void onFailure(Call<ImagesResult> call, Throwable t) {
                mNetworkState.postValue(NetworkState.LOADED);
                mInitialLoading.postValue(NetworkState.LOADED);
            }
        });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ImagesModel> callback) {


        restApiInterface.getImages(API_KEY,getSearch(),getCategory(),params.key,5).enqueue(new Callback<ImagesResult>() {
            @Override
            public void onResponse(Call<ImagesResult> call, Response<ImagesResult> response) {
                if(response.isSuccessful()) {
                    Integer adjacencyKey = (params.key > 1) ? params.key - 1 : null;
                    if (response.body() != null) {
                        callback.onResult(response.body().getHits(), adjacencyKey);
                    }
                }else {
                    mNetworkState.postValue(NetworkState.LOADED);
                    mInitialLoading.postValue(NetworkState.LOADED);
                }

            }

            @Override
            public void onFailure(Call<ImagesResult> call, Throwable t) {
                mNetworkState.postValue(NetworkState.LOADED);
                mInitialLoading.postValue(NetworkState.LOADED);
            }
        });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ImagesModel> callback) {

        restApiInterface.getImages(API_KEY,getSearch(),getCategory(),params.key,5).enqueue(new Callback<ImagesResult>() {
            @Override
            public void onResponse(Call<ImagesResult> call, Response<ImagesResult> response) {
               if(response.isSuccessful()){
                   Integer key=(params.key>1)?params.key+1:null;
                   if(response.body() != null){
                       callback.onResult(response.body().getHits(),key);
                   }
               }else {
                   mNetworkState.postValue(NetworkState.LOADED);
                   mInitialLoading.postValue(NetworkState.LOADED);
               }

            }

            @Override
            public void onFailure(Call<ImagesResult> call, Throwable t) {
                mInitialLoading.postValue(new NetworkState(NetworkState.Status.FAILED));
                mNetworkState.postValue(new NetworkState(NetworkState.Status.FAILED));

            }
        });

    }
}
