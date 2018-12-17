package com.example.breezil.pixxo.di.module;


import com.example.breezil.pixxo.ui.PhotoDetailFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class DetailFragmentModule {

    @ContributesAndroidInjector
    abstract PhotoDetailFragment contributePhotoDetailFragment();

}
