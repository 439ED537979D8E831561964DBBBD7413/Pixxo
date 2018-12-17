package com.example.breezil.pixxo.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.breezil.pixxo.model.ImagesModel;
import com.example.breezil.pixxo.model.ImagesResult;
import com.example.breezil.pixxo.repository.MainRepository;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class MainViewModel extends AndroidViewModel {
    private LiveData<List<ImagesModel>> imagesList;
    private MainRepository mainRepository;

    @Inject
    public MainViewModel( MainRepository mainRepository, Application application) {
        super(application);
        this.mainRepository = mainRepository;
    }

    public LiveData<List<ImagesModel>> getImagesList(Map<String, Object> parameter){
        if(imagesList == null ){
            imagesList = mainRepository.getImages(parameter);
        }
        return imagesList;
    }


}
