package com.andersenlab.boilerplate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andersenlab.boilerplate.R;
import com.andersenlab.boilerplate.adapter.viewholder.BaseViewHolder;
import com.andersenlab.boilerplate.model.Image;
import com.andersenlab.boilerplate.util.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * RecyclerView adapter for {@link Image} model items.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;
    private List<Image> items;

    public ImageAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<>();
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ImageViewHolder(view);
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
        super.onViewRecycled(holder);
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

    class ImageViewHolder extends BaseViewHolder<Image> {

        @BindView(R.id.iv_image_content) ImageView ivContent;

        ImageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void bind(Image imageItem) {
            ivContent.setContentDescription(imageItem.getContentDescription() != null ?
                    imageItem.getContentDescription() : imageItem.getImageUrl());
            ImageUtils.getInstance().loadImage(context, imageItem.getImageUrl(), ivContent);
        }

        void clearContent() {
            ImageUtils.getInstance().clearView(context, ivContent);
        }
    }
}
