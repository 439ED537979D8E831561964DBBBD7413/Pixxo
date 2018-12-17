package com.example.breezil.pixxo.di.module;

import com.example.breezil.pixxo.ui.SearchDefaultFragment;
import com.example.breezil.pixxo.ui.SearchListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SearchFragmentModule {
    @ContributesAndroidInjector
    abstract SearchDefaultFragment contributeSearchDefaultFragment();

    @ContributesAndroidInjector
    abstract SearchListFragment contibuteSearchListFragment();
}
