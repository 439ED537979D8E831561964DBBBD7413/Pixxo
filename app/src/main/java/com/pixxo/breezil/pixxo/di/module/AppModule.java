package com.pixxo.breezil.pixxo.di.module;


import android.app.Application;
import android.arch.persistence.room.Room;

import com.pixxo.breezil.pixxo.api.ImagesApi;
import com.pixxo.breezil.pixxo.api.OkHttp;
import com.pixxo.breezil.pixxo.db.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.pixxo.breezil.pixxo.BuildConfig.BASE_URL;
import static com.pixxo.breezil.pixxo.utils.Constant.PIXXO_DB;

@Module(includes = ViewModelModule.class)
public class AppModule {
    @Singleton
    @Provides
    ImagesApi provideImagesApi() {
        OkHttp okHttp = new OkHttp();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttp.getClient())
                .build()
                .create(ImagesApi.class);
    }

    @Singleton
    @Provides
    AppDatabase provideDb(Application app) {
        return Room.databaseBuilder(app, AppDatabase.class, PIXXO_DB).build();
    }

    @Provides
    CompositeDisposable provideCompositeDisposable(){
        return new CompositeDisposable();
    }

}
