package com.andersenlab.boilerplate.view;

import com.andersenlab.boilerplate.model.Image;

public interface MainMvpView extends MvpView {
    void showNewItem(Image item);
    void showError();
}
