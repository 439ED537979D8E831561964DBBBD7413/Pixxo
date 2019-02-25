package com.pixxo.breezil.pixxo.di.module;

import com.pixxo.breezil.pixxo.ui.settings.SettingsActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SettingsActivityModule {
    @ContributesAndroidInjector(modules = SettingsFragmentModule.class )
    abstract SettingsActivity contributeSettingsActivity();
}
