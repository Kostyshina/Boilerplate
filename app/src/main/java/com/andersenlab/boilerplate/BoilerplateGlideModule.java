package com.andersenlab.boilerplate;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
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
                .format(DecodeFormat.PREFER_RGB_565)
                .disallowHardwareConfig());
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

}

