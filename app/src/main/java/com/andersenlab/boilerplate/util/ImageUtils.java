package com.andersenlab.boilerplate.util;

import android.widget.ImageView;

import com.andersenlab.boilerplate.R;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

public class ImageUtils {

    private static ImageUtils instance;

    private ImageUtils() {

    }

    public static synchronized ImageUtils getInstance() {
        if (instance == null)
            instance = new ImageUtils();

        return instance;
    }

    /**
     * Method that used for loading images into imageView.
     * Using {@link com.andersenlab.boilerplate.R.color#colorError} on error or fallback occurred
     * and {@link com.andersenlab.boilerplate.R.color#colorPlaceholder} as placeholder on loading.
     * @param glideRequest manager to perform Glide loading (keep in mind Glide lifecycle);
     * @param imageUrl Url of an image to load;
     * @param target ImageView to load image into.
     */
    public void loadImage(RequestManager glideRequest, String imageUrl, ImageView target) {
        if (target != null) {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.color.colorPlaceholder)
                    .error(R.color.colorError)
                    .fallback(R.color.colorError)
                    .dontTransform();
            glideRequest
                    .load(imageUrl)
                    .thumbnail(0.1f)
                    .apply(options)
                    .into(target);
        }
    }

    /**
     * Method that used for loading images afterwords.
     * @param glideRequest manager to perform Glide loading (keep in mind Glide lifecycle);
     * @param imageUrl Url of an image to load;
     * @return Instance of {@link RequestBuilder} to load image into.
     * @see ImageUtils#loadImage(RequestManager, String, ImageView)
     */
    public RequestBuilder preloadImage(RequestManager glideRequest, String imageUrl) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.color.colorPlaceholder)
                .error(R.color.colorError)
                .fallback(R.color.colorError)
                .dontTransform();
        return glideRequest
                .load(imageUrl)
                .thumbnail(0.1f)
                .apply(options);
    }

    public void clearView(RequestManager glideRequest, ImageView view) {
        glideRequest.clear(view);
    }
}
