package com.duedderuesch.placed.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ApplicationExecutors {

    private final String TAG = "appExecutors";

    private final Executor mainThread;
    private final Executor backgroundThread;

    public Executor getMainThread(){
        return  mainThread;
    }

    public Executor getBackgroundThread(){
        return backgroundThread;
    }

    public ApplicationExecutors(){
        this.mainThread = new MainThreadExecutor();
        this.backgroundThread = Executors.newSingleThreadExecutor();
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(
                Looper.getMainLooper()
        );

        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
