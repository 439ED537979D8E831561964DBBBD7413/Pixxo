package com.pixxo.breezil.pixxo.di.module;

import com.pixxo.breezil.pixxo.ui.saved_edit.EditSavedFragment;
import com.pixxo.breezil.pixxo.ui.saved_edit.SavedImageFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SavedFragmentModule {
    @ContributesAndroidInjector
    abstract SavedImageFragment contributeSavedImageFragment();

    @ContributesAndroidInjector
    abstract EditSavedFragment contributeEditSavedFragment();
}
