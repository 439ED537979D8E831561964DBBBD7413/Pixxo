package com.pixxo.breezil.pixxo.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.pixxo.breezil.pixxo.db.AppDatabase;
import com.pixxo.breezil.pixxo.repository.MainDbRepository;
import com.pixxo.breezil.pixxo.repository.NetworkState;
import com.pixxo.breezil.pixxo.model.ImagesModel;
import com.pixxo.breezil.pixxo.repository.paging.ImageDataSourceFactory;
import com.pixxo.breezil.pixxo.repository.paging.ImageModelDataSource;
import com.pixxo.breezil.pixxo.utils.helper.AppExecutors;

import javax.inject.Inject;

import static com.pixxo.breezil.pixxo.utils.Constant.FIVE;


public class MainViewModel extends AndroidViewModel {
    private LiveData<PagedList<ImagesModel>> imageList;
    private LiveData<PagedList<ImagesModel>> imageDBList;
    private LiveData networkState;
    private AppExecutors appsExecutor;
    private ImageDataSourceFactory imageDataSourceFactory;
    private MainDbRepository mainDbRepository;
    private AppDatabase appDatabase;



    @Inject
    MainViewModel(ImageDataSourceFactory imageDataSourceFactory, AppExecutors appsExecutor,@NonNull Application application) {
        super(application);
        this.imageDataSourceFactory = imageDataSourceFactory;
        this.appsExecutor = appsExecutor;


        mainDbRepository = new MainDbRepository(application);
        appDatabase = AppDatabase.getAppDatabase(this.getApplication());


        networkState = Transformations.switchMap(imageDataSourceFactory.getImageDataSources(),
                ImageModelDataSource::getNetworkState);


        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(FIVE)
                .setPrefetchDistance(FIVE)
                .setPageSize(FIVE)
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



    public LiveData<PagedList<ImagesModel>> refreshImages(){
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPageSize(FIVE)
                .build();

        imageList = new LivePagedListBuilder<>(imageDataSourceFactory, config)
                .setFetchExecutor(appsExecutor.networkIO())
                .build();

        return imageList;
    }


    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }


    public void deleteAllInDb(){
        mainDbRepository.deleteAllImages();
    }


    public LiveData<PagedList<ImagesModel>> getFromDbList(){

        DataSource.Factory<Integer,ImagesModel> factory = appDatabase.imagesDao().getPagedImages();
        LivePagedListBuilder<Integer, ImagesModel> livePagedListBuilder = new LivePagedListBuilder<Integer,ImagesModel>(factory,FIVE);
        imageDBList = livePagedListBuilder.build();
        return imageDBList;
    }

}

