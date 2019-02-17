package com.example.breezil.pixxo.repository.paging;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.breezil.pixxo.api.EndpointRepository;
import com.example.breezil.pixxo.db.AppDatabase;
import com.example.breezil.pixxo.db.ImagesDao;
import com.example.breezil.pixxo.model.ImagesModel;
import com.example.breezil.pixxo.model.ImagesResult;
import com.example.breezil.pixxo.repository.MainDbRepository;
import com.example.breezil.pixxo.repository.NetworkState;
import com.example.breezil.pixxo.repository.PaginationListener;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.example.breezil.pixxo.utils.Constant.ONE;
import static com.example.breezil.pixxo.utils.Constant.TEN;
import static com.example.breezil.pixxo.utils.Constant.TWO;
import static com.example.breezil.pixxo.utils.Constant.ZERO;

@Singleton
public class ImageModelDataSource extends PageKeyedDataSource<Integer, ImagesModel>
        implements PaginationListener<ImagesResult, ImagesModel> {

    private String mSearch;
    private String mCategory;
    private String mLang;
    private String mOrder;
    private CompositeDisposable compositeDisposable;
    EndpointRepository endpointRepository;
    MainDbRepository mainDbRepository;

    private final MutableLiveData<NetworkState> mNetworkState;
    private final MutableLiveData<NetworkState> mInitialLoading;
    ImagesDao imagesDao;


    @Inject
    public ImageModelDataSource(EndpointRepository endpointRepository,
                                MainDbRepository mainDbRepository,
                                CompositeDisposable compositeDisposable,
                                Application application) {
        mNetworkState = new MutableLiveData<>();
        mInitialLoading = new MutableLiveData<>();
        this.compositeDisposable = compositeDisposable;
        this.endpointRepository = endpointRepository;
        this.mainDbRepository = mainDbRepository;
        AppDatabase database = AppDatabase.getAppDatabase(application);
        imagesDao = database.imagesDao();
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
    public String getLang(){
        return mLang;
    }
    public void setLang(String lang){
        mLang = lang;
    }
    public void setOrder(String order){
        mOrder = order;
    }
    public String getOrder(){
        return mOrder;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, ImagesModel> callback) {

        List<ImagesModel> modelList = new ArrayList<>();
        Disposable imagesModel = endpointRepository.getImages(getSearch(),getLang(),getCategory(),getOrder(),ONE,TEN)
                .subscribe( imagesResult -> {
                    onInitialSuccess(imagesResult, callback, modelList);
                        for(ImagesModel img : imagesResult.getHits()){
                            mainDbRepository.insert(img);
                        }
                    }, throwable -> onInitialError(throwable));
        compositeDisposable.add(imagesModel);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ImagesModel> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ImagesModel> callback) {
        List<ImagesModel> modelList = new ArrayList<>();
        Disposable result = endpointRepository.getImages(getSearch(),getLang(), getCategory(),getOrder(), params.key,TEN)
                .subscribe(response -> onPaginationSuccess(response, callback, params, modelList), this::onPaginationError);

        compositeDisposable.add(result);
    }

    @Override
    public void onInitialError(Throwable throwable) {
        mInitialLoading.postValue(new NetworkState(NetworkState.Status.FAILED));
        mNetworkState.postValue(new NetworkState(NetworkState.Status.FAILED));
        Timber.e(throwable);
    }

    @Override
    public void onInitialSuccess(ImagesResult imagesResult,
                                 LoadInitialCallback<Integer, ImagesModel> callback,
                                 List<ImagesModel> imagesModels) {
        if (imagesResult.getHits() != null && imagesResult.getHits().size() > ZERO) {
            imagesModels.addAll(imagesResult.getHits());
            callback.onResult(imagesModels, null, TWO);



            mInitialLoading.postValue(NetworkState.LOADED);
            mNetworkState.postValue(NetworkState.LOADED);


        } else {
            mInitialLoading.postValue(new NetworkState(NetworkState.Status.NO_RESULT));
            mNetworkState.postValue(new NetworkState(NetworkState.Status.NO_RESULT));
        }
    }

    @Override
    public void onPaginationError(Throwable throwable) {
        mNetworkState.postValue(new NetworkState(NetworkState.Status.FAILED));
        Timber.e(throwable);
    }

    @Override
    public void onPaginationSuccess(ImagesResult imagesResult, LoadCallback<Integer, ImagesModel>
            callback, LoadParams<Integer> params, List<ImagesModel> imagesModels) {
        if (imagesResult.getHits()!= null && imagesResult.getHits().size() > ZERO) {
            imagesModels.addAll(imagesResult.getHits());

            Integer key=(params.key>ONE)?params.key+ONE:null;
            callback.onResult(imagesModels, key);

            mNetworkState.postValue(NetworkState.LOADED);
        } else {
            mNetworkState.postValue(new NetworkState(NetworkState.Status.NO_RESULT));
        }
    }


    @Override
    public void clear() {
        compositeDisposable.clear();
    }
}
