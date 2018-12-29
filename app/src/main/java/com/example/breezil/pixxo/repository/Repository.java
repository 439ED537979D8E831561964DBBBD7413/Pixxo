package com.example.breezil.pixxo.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;

import com.example.breezil.pixxo.model.ImagesModel;

public class Repository {

    LiveData<PagedList<ImagesModel>> pagedListLiveData;
    LiveData<PageKeyedDataSource<Integer, ImagesModel>> pageKeyedDataSourceLiveData;

    public Repository(Application application){

    }

    public LiveData<PagedList<ImagesModel>> getPagedListLiveData(){
        return pagedListLiveData;
    }
}
