package com.andersenlab.boilerplate.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andersenlab.boilerplate.R;
import com.andersenlab.boilerplate.adapter.viewholder.BaseViewHolder;
import com.andersenlab.boilerplate.model.Image;
import com.andersenlab.boilerplate.util.ImageUtils;
import com.bumptech.glide.ListPreloader.PreloadSizeProvider;
import com.bumptech.glide.ListPreloader.PreloadModelProvider;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.util.ViewPreloadSizeProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * RecyclerView adapter for {@link Image} model items.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>
        implements PreloadModelProvider<Image>{

    private RequestManager glideRequest;
    private List<Image> items;
    private ViewPreloadSizeProvider<Image> preloadSizeProvider;

    public ImageAdapter(RequestManager glideRequest) {
        this.glideRequest = glideRequest;
        this.items = new ArrayList<>();
        this.preloadSizeProvider = new ViewPreloadSizeProvider<>();
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ImageViewHolder holder = new ImageViewHolder(view);
        preloadSizeProvider.setView(holder.contentImage);

        return holder;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Image imageItem = items.get(position);
        holder.bind(imageItem);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onViewRecycled(ImageViewHolder holder) {
        holder.clearContent();
    }

    @NonNull
    @Override
    public List<Image> getPreloadItems(int position) {
        Image imageItem = items.get(position);
        if (imageItem == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(imageItem);
    }

    @Nullable
    @Override
    public RequestBuilder getPreloadRequestBuilder(Image imageItem) {
        return ImageUtils.getInstance().preloadImage(glideRequest, imageItem.getImageUrl());
    }

    public void setItems(List<Image> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
        Timber.i("setItems");
    }

    public void addItem(Image item) {
        this.items.add(item);
        notifyItemInserted(getItemCount());
        Timber.i("addItem");
    }

    public PreloadSizeProvider<Image> getPreloadSizeProvider() {
        return preloadSizeProvider;
    }

    class ImageViewHolder extends BaseViewHolder<Image> {

        @BindView(R.id.iv_image_content) ImageView contentImage;

        ImageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void bind(Image imageItem) {
            ImageUtils.getInstance().loadImage(glideRequest, imageItem.getImageUrl(), contentImage);
            contentImage.setContentDescription(imageItem.getContentDescription() != null ?
                    imageItem.getContentDescription() : imageItem.getImageUrl());
        }

        void clearContent() {
            ImageUtils.getInstance().clearView(glideRequest, contentImage);
        }
    }
}
