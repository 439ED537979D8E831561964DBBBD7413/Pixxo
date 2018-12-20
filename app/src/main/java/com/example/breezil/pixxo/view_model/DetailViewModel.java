package com.example.breezil.pixxo.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.breezil.pixxo.model.ImagesModel;
import com.example.breezil.pixxo.repository.MainRepository;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class DetailViewModel extends AndroidViewModel {

    MainRepository mainRepository;
    private MutableLiveData<List<ImagesModel>> imagesList = new MutableLiveData<>();
    private MutableLiveData<ImagesModel> imageModel = new MutableLiveData<>();

    @Inject
    public DetailViewModel(MainRepository mainRepository, Application application) {
        super(application);
        this.mainRepository = mainRepository;
    }

    public void setImagesListValue(List<ImagesModel> imagesModelList){
        this.imagesList.setValue(imagesModelList);
    }

    public LiveData<List<ImagesModel>> getImagesList(Map<String, Object> parameter){
        if(imagesList == null ){
            imagesList = (MutableLiveData<List<ImagesModel>>) mainRepository.getImages(parameter);
        }
        return imagesList;
    }



    public void setImage(ImagesModel image){
        this.imageModel.setValue(image);
    }

    public MutableLiveData<ImagesModel> getImage(){
        return imageModel;
    }




}
