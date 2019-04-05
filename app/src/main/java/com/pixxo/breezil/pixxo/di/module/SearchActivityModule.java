package com.pixxo.breezil.pixxo.di.module;

import com.pixxo.breezil.pixxo.ui.explore.ExploreActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SearchActivityModule {
    @ContributesAndroidInjector()
    abstract ExploreActivity contibuteExploreActivity();
}
