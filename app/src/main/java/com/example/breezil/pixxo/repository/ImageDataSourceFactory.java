package com.example.breezil.pixxo.repository;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;

import com.example.breezil.pixxo.api.ImagesApi;
import com.example.breezil.pixxo.model.ImagesModel;

import javax.inject.Inject;

public class ImageDataSourceFactory extends DataSource.Factory<Integer, ImagesModel> {
    private MutableLiveData<ImageModelDataSource> imageDataSourceMutableLiveData;
    private ImageModelDataSource dataSource;
    ImagesApi imagesApi;

    @Inject
    public ImageDataSourceFactory(ImageModelDataSource dataSource) {
        this.dataSource = dataSource;
        imageDataSourceMutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource<Integer, ImagesModel> create() {
//        dataSource = new ImageModelDataSource(imagesApi);
        imageDataSourceMutableLiveData.postValue(dataSource);

        return dataSource;
    }

    public MutableLiveData<ImageModelDataSource> getImageDataSources(){
        return imageDataSourceMutableLiveData;
    }

    public ImageModelDataSource getDataSource() {
        return dataSource;
    }
}
