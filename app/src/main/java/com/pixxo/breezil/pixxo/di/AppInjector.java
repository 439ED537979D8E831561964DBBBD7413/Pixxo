package com.pixxo.breezil.pixxo.di;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.pixxo.breezil.pixxo.PixxoApp;

import dagger.android.AndroidInjection;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;

public class AppInjector {
    private AppInjector() {}

    public static  void init(PixxoApp pixxoApp) {
        DaggerAppComponent.builder().application((Application)pixxoApp).build().inject(pixxoApp);
        pixxoApp.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            public void onActivityCreated( Activity activity, @Nullable Bundle savedInstanceState) {

                handleActivity(activity);
            }

            public void onActivityStarted(Activity activity) {
            }

            public void onActivityResumed(Activity activity) {
            }

            public void onActivityPaused(Activity activity) {
            }

            public void onActivityStopped(Activity activity) {
            }

            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

    private static  void handleActivity(Activity activity) {
        if (activity instanceof HasSupportFragmentInjector) {
            AndroidInjection.inject(activity);
        }

        if (activity instanceof FragmentActivity) {
            ((FragmentActivity)activity).getSupportFragmentManager()
                    .registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
                public void onFragmentCreated(FragmentManager fm,Fragment f, Bundle savedInstanceState) {

                    if (f instanceof Injectable) {
                        AndroidSupportInjection.inject(f);
                    }

                }
            }, true);
        }

    }


}
