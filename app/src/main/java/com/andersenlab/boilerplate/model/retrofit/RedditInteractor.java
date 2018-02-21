package com.andersenlab.boilerplate.model.retrofit;

import com.andersenlab.boilerplate.BoilerplateApp;
import com.andersenlab.boilerplate.model.retrofit.imagemodel.Child;
import com.andersenlab.boilerplate.model.retrofit.imagemodel.Data;
import com.andersenlab.boilerplate.model.retrofit.imagemodel.Preview;
import com.andersenlab.boilerplate.model.retrofit.imagemodel.RedditImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Class-interactor with {@link RedditApi}.
 */

public class RedditInteractor {

    private static RedditInteractor instance;
    private RedditApi redditApi;
    private RedditImage prevRedditImage;

    private RedditInteractor() {
        redditApi = BoilerplateApp.getInstance().getRedditApi();
    }

    public static synchronized RedditInteractor getInstance() {
        if (instance == null) {
            instance = new RedditInteractor();
        }
        return instance;
    }

    /**
     * Method that requests images from {@link RedditApi}.
     * @param imagesCount the count of images you want to get.
     * @return Observable of {@link RedditImage} object.
     */
    public Observable<RedditImage> getRedditImage(int imagesCount) {
        Timber.i("getRedditImage");
        Observable<RedditImage> redditImageObservable = redditApi.getImages(imagesCount);
        if (prevRedditImage != null) {
            Data data = prevRedditImage.getData();
            if (data != null && data.getAfter() != null) {
                redditImageObservable = redditApi.getImages(imagesCount, data.getAfter());
            }
        }
        return redditImageObservable
                .subscribeOn(Schedulers.io())
                .doOnNext(redditImage -> prevRedditImage = redditImage);
    }

    public List<Child> mapChildList(RedditImage redditImage) {
        List<Child> childList = null;
        if (redditImage != null && redditImage.getData() != null) {
            Data data = redditImage.getData();
            childList = data.getChildren();
        }
        if (childList == null)
            childList = Collections.emptyList();
        Timber.i("mapChildList, size = %d", childList.size());
        return childList;
    }

    /**
     * Method to get images urls from Reddit response.
     * @param redditImage response of {@link RedditApi#getImages(int)}.
     * @return List of images urls.
     */
    public List<String> mapUrlsList(RedditImage redditImage) {
        List<String> urlsList = new ArrayList<>();
        List<Child> childList = mapChildList(redditImage);
        for(Child child : childList) {
            if (child.getData() != null && child.getData().getPreview() != null) {
                Preview preview = child.getData().getPreview();
                if (preview.getImages() != null && preview.getImages().get(0) != null) {
                    String url = preview.getImages().get(0).getSource().getUrl();
                    // to correct incorrect url format
                    url = url.replace("&amp;", "&");
                    urlsList.add(url);
                }
            }
        }
        Timber.i("mapUrlsList, size = %d", urlsList.size());
        return urlsList;
    }
}
