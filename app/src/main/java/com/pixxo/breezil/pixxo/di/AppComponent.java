package com.pixxo.breezil.pixxo.di;


import android.app.Application;

import com.pixxo.breezil.pixxo.PixxoApp;
import com.pixxo.breezil.pixxo.di.module.AppModule;
import com.pixxo.breezil.pixxo.di.module.DetailActivityModule;
import com.pixxo.breezil.pixxo.di.module.MainActivityModule;
import com.pixxo.breezil.pixxo.di.module.SavedActivityModule;
import com.pixxo.breezil.pixxo.di.module.SearchActivityModule;
import com.pixxo.breezil.pixxo.di.module.SettingsActivityModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        MainActivityModule.class,
        DetailActivityModule.class,
        SavedActivityModule.class,
        SearchActivityModule.class,
        SettingsActivityModule.class
})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
    void inject(PixxoApp pixxoApp);
}
