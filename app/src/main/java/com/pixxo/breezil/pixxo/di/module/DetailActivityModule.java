package com.pixxo.breezil.pixxo.di.module;


import com.pixxo.breezil.pixxo.ui.detail.DetailActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class DetailActivityModule {
    @ContributesAndroidInjector(modules = DetailFragmentModule.class)
    abstract DetailActivity contributeDetailActivity();
}

