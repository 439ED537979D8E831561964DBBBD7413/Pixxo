package com.pixxo.breezil.pixxo.repository.paging;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import com.pixxo.breezil.pixxo.model.ImagesModel;

import javax.inject.Inject;

public class ImageDataSourceFactory extends DataSource.Factory<Integer, ImagesModel> {
    private MutableLiveData<ImageModelDataSource> imageDataSourceMutableLiveData;
    private ImageModelDataSource dataSource;
    @Inject
    public ImageDataSourceFactory(ImageModelDataSource dataSource) {
        this.dataSource = dataSource;
        imageDataSourceMutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource<Integer, ImagesModel> create() {
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
