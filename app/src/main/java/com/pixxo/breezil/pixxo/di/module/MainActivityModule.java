package com.pixxo.breezil.pixxo.di.module;

import com.pixxo.breezil.pixxo.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class MainActivityModule {
    @ContributesAndroidInjector()
    abstract MainActivity contributeMainActivity();
}
