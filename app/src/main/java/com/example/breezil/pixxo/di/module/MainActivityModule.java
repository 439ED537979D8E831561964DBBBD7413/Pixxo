package com.example.breezil.pixxo.di.module;

import com.example.breezil.pixxo.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class MainActivityModule {
    @ContributesAndroidInjector()
    abstract MainActivity contributeMainActivity();
}
