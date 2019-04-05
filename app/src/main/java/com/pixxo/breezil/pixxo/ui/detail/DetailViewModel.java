package com.pixxo.breezil.pixxo.ui.detail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import com.pixxo.breezil.pixxo.model.ImagesModel;

import javax.inject.Inject;

public class DetailViewModel extends AndroidViewModel {

    private MutableLiveData<ImagesModel> imageModel = new MutableLiveData<>();

    @Inject
    public DetailViewModel( Application application) {
        super(application);
    }

    public void setImage(ImagesModel image){
        this.imageModel.setValue(image);
    }

    public MutableLiveData<ImagesModel> getImage(){
        return imageModel;
    }




}
