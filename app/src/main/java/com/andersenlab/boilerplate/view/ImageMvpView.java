package com.andersenlab.boilerplate.view;

import com.andersenlab.boilerplate.model.Image;

public interface ImageMvpView extends MvpView {
    void showNewImage(Image item);
    void showNoNewImages();
    void showError(MvpViewException exc);
}
