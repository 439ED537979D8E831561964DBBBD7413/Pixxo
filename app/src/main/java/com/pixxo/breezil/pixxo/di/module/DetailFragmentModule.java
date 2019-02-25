package com.pixxo.breezil.pixxo.di.module;


import com.pixxo.breezil.pixxo.ui.detail.PhotoDetailFragment;
import com.pixxo.breezil.pixxo.ui.detail.TabletListFragment;
import com.pixxo.breezil.pixxo.ui.detail.TabletSearchListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class DetailFragmentModule {

    @ContributesAndroidInjector
    abstract PhotoDetailFragment contributePhotoDetailFragment();

    @ContributesAndroidInjector
    abstract TabletListFragment contributeTabletListFragment();

    @ContributesAndroidInjector
    abstract TabletSearchListFragment contributeTabletSearchListFragmentt();
}
