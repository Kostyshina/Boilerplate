package com.andersenlab.boilerplate.model.retrofit;

import com.andersenlab.boilerplate.model.retrofit.imagemodel.RedditImage;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * API to get responses from reddit.com.
 */

public interface RedditApi {
    String BASE_URL = "https://www.reddit.com/";

    @GET("r/EarthPorn/new.json")
    Observable<RedditImage> getImages(@Query("limit") int count);
}
