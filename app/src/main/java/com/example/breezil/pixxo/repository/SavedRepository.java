package com.example.breezil.pixxo.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.breezil.pixxo.db.AppDatabase;
import com.example.breezil.pixxo.db.SavedDao;
import com.example.breezil.pixxo.model.SavedImageModel;

import java.util.List;

import javax.inject.Inject;

public class SavedRepository {
    private SavedDao savedDao;
    private LiveData<List<SavedImageModel>> allSaved;

    @Inject
    public SavedRepository(Application application){
        AppDatabase database = AppDatabase.getAppDatabase(application);
        savedDao = database.savedDao();
        allSaved = savedDao.getAllSaved();
    }

    public void insert(SavedImageModel savedImageModel){
        new InsertSaved(savedDao).execute(savedImageModel);
    }
    public void delete(SavedImageModel savedImageModel){
        new DeleteSaved(savedDao).execute(savedImageModel);
    }
    public void deleteAllSaved(){
        new DeleteAllSaved(savedDao).execute();
    }

    public LiveData<List<SavedImageModel>> getAllSaved(){
        return allSaved;
    }

    private static class InsertSaved extends AsyncTask<SavedImageModel, Void, Void>{
        private SavedDao savedDao;

        public InsertSaved(SavedDao savedDao) {
            this.savedDao = savedDao;
        }

        @Override
        protected Void doInBackground(SavedImageModel... savedImageModels) {
            savedDao.insert(savedImageModels[0]);
            return null;
        }
    }

    private static class DeleteSaved extends AsyncTask<SavedImageModel, Void, Void>{
        private SavedDao savedDao;

        public DeleteSaved(SavedDao savedDao) {
            this.savedDao = savedDao;
        }

        @Override
        protected Void doInBackground(SavedImageModel... savedImageModels) {
            savedDao.delete(savedImageModels[0]);
            return null;
        }
    }

    private static class DeleteAllSaved extends AsyncTask<Void, Void,Void>{
        private SavedDao savedDao;

        public DeleteAllSaved(SavedDao savedDao) {
            this.savedDao = savedDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            savedDao.deleteAllSaved();
            return null;
        }
    }

}
