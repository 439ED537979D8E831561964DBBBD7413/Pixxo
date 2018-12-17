package com.example.breezil.pixxo.di.module;

import com.example.breezil.pixxo.ui.EditSavedFragment;
import com.example.breezil.pixxo.ui.SavedImageFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SavedFragmentModule {
    @ContributesAndroidInjector
    abstract SavedImageFragment contributeSavedImageFragment();

    @ContributesAndroidInjector
    abstract EditSavedFragment contributeEditSavedFragment();
}
