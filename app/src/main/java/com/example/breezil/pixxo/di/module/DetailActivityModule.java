package com.example.breezil.pixxo.di.module;


import com.example.breezil.pixxo.ui.DetailActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class DetailActivityModule {
    @ContributesAndroidInjector(modules = DetailFragmentModule.class)
    abstract DetailActivity contributeDetailActivity();
}

