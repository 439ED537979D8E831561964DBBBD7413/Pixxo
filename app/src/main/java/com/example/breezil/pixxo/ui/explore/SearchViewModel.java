package com.example.breezil.pixxo.ui.explore;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.example.breezil.pixxo.repository.NetworkState;
import com.example.breezil.pixxo.model.ImagesModel;
import com.example.breezil.pixxo.repository.paging.ImageDataSourceFactory;
import com.example.breezil.pixxo.repository.paging.ImageModelDataSource;
import com.example.breezil.pixxo.utils.helper.AppExecutors;

import javax.inject.Inject;

public class SearchViewModel  extends ViewModel {

    private LiveData<PagedList<ImagesModel>> imageList;
    private LiveData<NetworkState> networkState;
    private LiveData<NetworkState> initialLoading;
    private AppExecutors appsExecutor;

    private ImageDataSourceFactory imageDataSourceFactory;

    @Inject
    SearchViewModel(ImageDataSourceFactory imageDataSourceFactory, AppExecutors appsExecutor) {
        this.imageDataSourceFactory = imageDataSourceFactory;
        this.appsExecutor = appsExecutor;


        networkState = Transformations.switchMap(imageDataSourceFactory.getImageDataSources(),
                ImageModelDataSource::getNetworkState);
        initialLoading = Transformations.switchMap(imageDataSourceFactory.getImageDataSources(),
                ImageModelDataSource::getInitialLoading);


        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(5)
                .setPrefetchDistance(5)
                .setPageSize(5)
                .build();

        imageList = new LivePagedListBuilder<>(imageDataSourceFactory,config)
                .setFetchExecutor(appsExecutor.networkIO())
                .build();

    }



    public LiveData<PagedList<ImagesModel>>getSearchList(){
        return imageList;
    }
    public void setParameter(String search, String category, String lang,String order){
        imageDataSourceFactory.getDataSource().setSearch(search);
        imageDataSourceFactory.getDataSource().setCategory(category);
        imageDataSourceFactory.getDataSource().setLang(lang);
        imageDataSourceFactory.getDataSource().setOrder(order);
    }


    public void setNetworkState() {
    }
    public LiveData<PagedList<ImagesModel>> refreshImages(){
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPageSize(5)
                .build();

        imageList = new LivePagedListBuilder<>(imageDataSourceFactory, config)
                .setFetchExecutor(appsExecutor.networkIO())
                .build();

        return imageList;
    }



    public LiveData<NetworkState> getInitialLoading() {
        return initialLoading;
    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

}
