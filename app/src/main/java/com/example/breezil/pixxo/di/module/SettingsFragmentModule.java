package com.example.breezil.pixxo.di.module;

import com.example.breezil.pixxo.ui.PreferenceFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SettingsFragmentModule {
    @ContributesAndroidInjector
    abstract PreferenceFragment contributePreferenceFragment();
}
