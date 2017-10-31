package com.andersenlab.boilerplate;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

/**
 * Default Glide module to apply. Used for GlideApp.with() requests.
 */

@GlideModule
public class BoilerplateGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDefaultRequestOptions(new RequestOptions()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .disallowHardwareConfig());
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
    }

}

