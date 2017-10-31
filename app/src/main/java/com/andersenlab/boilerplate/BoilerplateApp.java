package com.andersenlab.boilerplate;

import android.app.Application;
import android.os.StrictMode;

import com.andersenlab.boilerplate.model.Image;
import com.andersenlab.boilerplate.model.db.DatabaseHelper;
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

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        dbHelper.deleteAllImages();
        String[] images = new String[]{"Image 1", "Image 2", "Image 3", "Image 4",
                "Image 5", "Image 6", "Image 7", "Image 8", "Image 9", "Image 10",
                "Image 11", "Image 12", "Image 13", "Image 14", "Image 15", "Image 16"};
        for(String image : images) {
            dbHelper.addImage(new Image(null, image));
        }
    }
}
