package com.example.breezil.pixxo.di.module;


import com.example.breezil.pixxo.ui.saved_edit.SavedActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SavedActivityModule {
    @ContributesAndroidInjector( modules = SavedFragmentModule.class )
    abstract SavedActivity contributeSavedActivity();
}
