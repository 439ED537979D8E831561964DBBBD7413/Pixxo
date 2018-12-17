package com.example.breezil.pixxo.di.module;


import com.example.breezil.pixxo.ui.EditImageActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class EditImageActivityModule {
    @ContributesAndroidInjector()
    abstract EditImageActivity contributeEditImageActivity();
}
