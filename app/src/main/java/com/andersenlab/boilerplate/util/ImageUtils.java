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

    public void loadImage(Context context, String imageUrl, ImageView target) {
        if (context!= null && target != null) {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.color.colorPlaceholder)
                    .error(R.color.colorError)
                    .fallback(R.color.colorError);
            Glide.with(context)
                    .load(imageUrl)
                    .apply(options)
                    .into(target);
        }
    }

    public void clearView(Context context, ImageView view) {
        Glide.with(context).clear(view);
    }
}
