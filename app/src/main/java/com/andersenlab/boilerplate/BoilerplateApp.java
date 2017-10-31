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

    private static BoilerplateApp instance;

    public static BoilerplateApp getInstance() {
        return instance;
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
        if (instance == null) {
            instance = this;
        }

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        dbHelper.deleteAllImages();
        String[] images = getResources().getStringArray(R.array.image_items);
        for (String image : images) {
            dbHelper.addImage(new Image(null, image));
        }
    }
}
