package com.example.breezil.pixxo.di;


import android.app.Application;

import com.example.breezil.pixxo.PixxoApp;
import com.example.breezil.pixxo.di.module.AppModule;
import com.example.breezil.pixxo.di.module.DetailActivityModule;
import com.example.breezil.pixxo.di.module.EditImageActivityModule;
import com.example.breezil.pixxo.di.module.MainActivityModule;
import com.example.breezil.pixxo.di.module.SavedActivityModule;
import com.example.breezil.pixxo.di.module.SearchActivityModule;

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
        EditImageActivityModule.class,
        SavedActivityModule.class,
        SearchActivityModule.class
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
