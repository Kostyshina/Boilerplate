package com.andersenlab.boilerplate.model;

import android.support.v4.util.LruCache;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Cache for loading observables
 */

public class CachedObservable {

    private static final CachedObservable INSTANCE = new CachedObservable();

    private LruCache<Class<?>, Observable<?>> observableCache = new LruCache<>(10);

    private CachedObservable() {

    }

    public static synchronized CachedObservable newInstance() {
        return INSTANCE;
    }

    public <T> Observable<T> getCachedObservable(Observable<T> currentObservable, Class<?> clazz) {
        Observable<T> cachedObservable;

        cachedObservable = (Observable<T>) observableCache.get(clazz);
        if (isEmptyObservable(clazz)) {
            Timber.i("New observable %s", clazz);
            cachedObservable = currentObservable
                    .observeOn(AndroidSchedulers.mainThread())
                    .cache()
                    .doAfterTerminate(() -> removeObservable(clazz));
            observableCache.put(clazz, cachedObservable);
        } else Timber.i("Cached observable %s", clazz);

        return cachedObservable;
    }

    public void removeObservable(Class<?> clazz) {
        Timber.i("Remove observable %s", clazz);
        observableCache.remove(clazz);
    }

    public boolean isEmptyObservable(Class<?> clazz) {
        return observableCache.get(clazz) == null;
    }
}
