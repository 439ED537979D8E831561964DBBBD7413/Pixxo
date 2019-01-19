package com.example.breezil.pixxo.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.example.breezil.pixxo.api.ImagesApi;
import com.example.breezil.pixxo.db.AppDatabase;
import com.example.breezil.pixxo.repository.MainDbRepository;
import com.example.breezil.pixxo.repository.NetworkState;
import com.example.breezil.pixxo.model.ImagesModel;
import com.example.breezil.pixxo.repository.ImageDataSourceFactory;
import com.example.breezil.pixxo.repository.ImageModelDataSource;
import com.example.breezil.pixxo.utils.helper.AppExecutors;

import java.util.List;

import javax.inject.Inject;


public class MainViewModel extends AndroidViewModel {
    private LiveData<PagedList<ImagesModel>> imageList;
    private LiveData<PagedList<ImagesModel>> imageDBList;
    private LiveData<NetworkState> networkState;
    private LiveData<NetworkState> initialLoading;
    private AppExecutors appsExecutor;
    private ImageDataSourceFactory imageDataSourceFactory;
    private MainDbRepository mainDbRepository;
    private LiveData<List<ImagesModel>> fromDbList;
    private AppDatabase appDatabase;
    private List<ImagesModel> imgList;
    ImagesApi imagesApi;



    @Inject
    MainViewModel(ImageDataSourceFactory imageDataSourceFactory, AppExecutors appsExecutor,@NonNull Application application) {
        super(application);
        this.imageDataSourceFactory = imageDataSourceFactory;
        this.appsExecutor = appsExecutor;


        mainDbRepository = new MainDbRepository(imagesApi, application);
        fromDbList = mainDbRepository.getImageModelList();
        appDatabase = AppDatabase.getAppDatabase(this.getApplication());



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


    public LiveData<PagedList<ImagesModel>>getImageList(){
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


    public void deleteAllInDb(){
        mainDbRepository.deleteAllImages();
    }

    public LiveData<PagedList<ImagesModel>> getFromDbList(){

        DataSource.Factory<Integer,ImagesModel> factory = appDatabase.imagesDao().getPagedImages();
        LivePagedListBuilder<Integer, ImagesModel> livePagedListBuilder = new LivePagedListBuilder<Integer,ImagesModel>(factory,5);
        imageDBList = livePagedListBuilder.build();
        return imageDBList;
    }

    public void insertImageDb(String search,String lang, String category,String order){
       mainDbRepository.insertData(search,lang,category,order);
    }




}

