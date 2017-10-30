package com.andersenlab.boilerplate;

import android.app.Application;
import android.os.StrictMode;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * Application class.
 */

public class BoilerplateApp extends Application {

    private static BoilerplateApp sInstance;

    public static BoilerplateApp getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());

            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }

        if (!Fabric.isInitialized()) {
            Fabric.with(this, new Crashlytics());
        }
        if (sInstance == null) {
            sInstance = this;
        }
    }
}
