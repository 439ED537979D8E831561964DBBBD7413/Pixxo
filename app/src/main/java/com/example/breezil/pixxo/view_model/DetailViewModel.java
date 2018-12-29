package com.example.breezil.pixxo.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.breezil.pixxo.model.ImagesModel;
import com.example.breezil.pixxo.repository.MainRepository;
import com.example.breezil.pixxo.utils.helper.Resource;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class DetailViewModel extends AndroidViewModel {

    private MainRepository mainRepository;
    private LiveData<Resource<List<ImagesModel>>>imagesList;
    private MutableLiveData<ImagesModel> imageModel = new MutableLiveData<>();

    @Inject
    public DetailViewModel(MainRepository mainRepository, Application application) {
        super(application);
        this.mainRepository = mainRepository;
    }

//    public LiveData<Resource<List<ImagesModel>>>getImagesList(Map<String, Object> parameter){
//        if(imagesList == null ){
//            imagesList = mainRepository.getImages(parameter);
//        }
//        return imagesList;
//    }



    public void setImage(ImagesModel image){
        this.imageModel.setValue(image);
    }

    public MutableLiveData<ImagesModel> getImage(){
        return imageModel;
    }




}
