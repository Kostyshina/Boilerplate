package com.andersenlab.boilerplate.model.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Abstract builder for all API
 */

public abstract class RestApiBuilder<T> {

    private Retrofit retrofit;
    private T restApi;

    private void create() {
        if (restApi == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getBaseUrl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            restApi = retrofit.create(getApiClass());
        }
    }

    public T getRestApi() {
        create();
        return restApi;
    }

    protected abstract String getBaseUrl();
    protected abstract Class<T> getApiClass();
}
