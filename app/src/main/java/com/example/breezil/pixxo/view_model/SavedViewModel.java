package com.example.breezil.pixxo.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.breezil.pixxo.db.AppDatabase;
import com.example.breezil.pixxo.model.SavedImageModel;
import com.example.breezil.pixxo.repository.SavedRepository;

import java.util.List;

import javax.inject.Inject;

public class SavedViewModel extends AndroidViewModel {
    private SavedRepository savedRepository;
    private LiveData<List<SavedImageModel>> savedList;
    private AppDatabase appDatabase;
    private LiveData<SavedImageModel> savedImageModel;

    @Inject
    public SavedViewModel(@NonNull Application application) {
        super(application);

        savedRepository = new SavedRepository(application);
        savedList = savedRepository.getAllSaved();
        appDatabase = AppDatabase.getAppDatabase(this.getApplication());
    }

    public void insert(SavedImageModel savedImageModel){
        savedRepository.insert(savedImageModel);
    }
    public void delete(SavedImageModel savedImageModel){
        savedRepository.delete(savedImageModel);
    }
    public void deleteAll(){
        savedRepository.deleteAllSaved();
    }
    public LiveData<List<SavedImageModel>> getSavedList(){
        return savedList;
    }
    public LiveData<SavedImageModel> getSavedById(int savedId){
        savedImageModel = appDatabase.savedDao().getSaveById(savedId);
        return savedImageModel;
    }
}
