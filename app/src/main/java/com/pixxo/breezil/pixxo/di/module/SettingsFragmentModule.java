package com.pixxo.breezil.pixxo.di.module;

import com.pixxo.breezil.pixxo.ui.settings.PreferenceFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SettingsFragmentModule {
    @ContributesAndroidInjector
    abstract PreferenceFragment contributePreferenceFragment();
}
