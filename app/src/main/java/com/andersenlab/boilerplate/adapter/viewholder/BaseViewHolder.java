package com.andersenlab.boilerplate.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.andersenlab.boilerplate.model.BaseModel;

/**
 * Base class for ViewHolders that contains abstract method {@link #bind(BaseModel)}
 * for binding model to view.
 */

public abstract class BaseViewHolder<T extends BaseModel> extends RecyclerView.ViewHolder {
    protected BaseViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * Abstract method for binding views by modelItems.
     * @param modelItem Instance of class, that extends {@link BaseModel} abstract class.
     */
    public abstract void bind(final T modelItem);
}
