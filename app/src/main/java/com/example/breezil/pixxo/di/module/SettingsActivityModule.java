package com.example.breezil.pixxo.di.module;

import com.example.breezil.pixxo.ui.SettingsActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SettingsActivityModule {
    @ContributesAndroidInjector(modules = SettingsFragmentModule.class )
    abstract SettingsActivity contributeSettingsActivity();
}
