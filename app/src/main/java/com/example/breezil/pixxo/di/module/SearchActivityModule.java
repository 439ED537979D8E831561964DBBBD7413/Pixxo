package com.example.breezil.pixxo.di.module;

import com.example.breezil.pixxo.ui.ExploreActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SearchActivityModule {
    @ContributesAndroidInjector ( modules = SearchFragmentModule.class )
    abstract ExploreActivity contibuteExploreActivity();
}
