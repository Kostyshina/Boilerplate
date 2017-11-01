package com.andersenlab.boilerplate.util;

import android.content.Context;
import android.widget.ImageView;

import com.andersenlab.boilerplate.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ImageUtils {

    private static ImageUtils instance;

    private ImageUtils() {

    }

    public static ImageUtils getInstance() {
        if (instance == null)
            instance = new ImageUtils();

        return instance;
    }

    /**
     * Method that used for loading images into imageView.
     * Using {@link com.andersenlab.boilerplate.R.color#colorError} on error or fallback occurred
     * and {@link com.andersenlab.boilerplate.R.color#colorPlaceholder} as placeholder on loading.
     * @param context Context for loading image (keep in mind Glide lifecycle);
     * @param imageUrl Url of an image to load;
     * @param target ImageView to load image into.
     */
    public void loadImage(Context context, String imageUrl, ImageView target) {
        if (context!= null && target != null) {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.color.colorPlaceholder)
                    .error(R.color.colorError)
                    .fallback(R.color.colorError)
                    .dontTransform()
                    .disallowHardwareConfig();
            Glide.with(context)
                    .load(imageUrl)
                    .thumbnail(0.1f)
                    .apply(options)
                    .into(target);
        }
    }

    public void clearView(Context context, ImageView view) {
        Glide.with(context).clear(view);
    }
}
