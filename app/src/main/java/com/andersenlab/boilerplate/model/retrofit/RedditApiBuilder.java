package com.andersenlab.boilerplate.model.retrofit;

/**
 * Concrete builder for {@link RedditApi}.
 */

public class RedditApiBuilder extends RestApiBuilder<RedditApi> {
    @Override
    protected String getBaseUrl() {
        return RedditApi.BASE_URL;
    }

    @Override
    protected Class<RedditApi> getApiClass() {
        return RedditApi.class;
    }
}
