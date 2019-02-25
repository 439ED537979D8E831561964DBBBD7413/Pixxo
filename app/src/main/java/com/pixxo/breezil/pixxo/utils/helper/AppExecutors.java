package com.pixxo.breezil.pixxo.utils.helper;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * More from here Architecture Guide "https://developer.android.com/arch"
 * Helper code from googlesamples/android-architecture-components:.
 * "https://github.com/breel93/android-architecture-components/blob/master/GithubBrowserSample/app/src/main/java/com/android/pixxo/github/AppExecutors.kt"
 *
 */

@Singleton
public class AppExecutors {

    private final Executor networkIO;
    private final Executor diskIO;
    private final Executor mainIO;

    @Inject
    public AppExecutors() {
        this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(5));
    }

    public AppExecutors(Executor diskIO, Executor networkIO) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainIO = new MainThreadExecutor();
    }

    static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

    public Executor networkIO() {
        return networkIO;
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor mainIO() {
        return mainIO;
    }
}