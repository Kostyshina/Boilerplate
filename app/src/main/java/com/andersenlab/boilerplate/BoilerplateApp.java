package com.andersenlab.boilerplate;

import android.app.Application;
import android.os.StrictMode;

import com.andersenlab.boilerplate.model.Image;
import com.andersenlab.boilerplate.model.db.DatabaseHelper;
import com.andersenlab.boilerplate.model.realm.RealmInteractor;
import com.andersenlab.boilerplate.model.retrofit.RedditApi;
import com.andersenlab.boilerplate.model.retrofit.RedditApiBuilder;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

/**
 * Application class.
 */

public class BoilerplateApp extends Application {

    private static BoilerplateApp instance;

    private RedditApi redditApi;

    public static BoilerplateApp getInstance() {
        return instance;
    }

    public RedditApi getRedditApi() {
        return redditApi;
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

        redditApi = new RedditApiBuilder().getRestApi();

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("boilerplate.realm").build();
        Realm.setDefaultConfiguration(config);

        if (instance == null) {
            instance = this;
        }

        String[] images = getResources().getStringArray(R.array.image_items);
        /*DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        dbHelper.deleteAllImages();
        for (String image : images) {
            dbHelper.addImage(new Image(null, image));
        }*/

        RealmInteractor interactor = RealmInteractor.getInstance();
        if (!interactor.hasObjects(Image.class)) {
            int i = 0;
            for (String imageUrl : images) {
                i++;
                final Image image = new Image(null, imageUrl);
                image.setId(i);
                interactor.addObject(image);
            }
        }
    }
}
