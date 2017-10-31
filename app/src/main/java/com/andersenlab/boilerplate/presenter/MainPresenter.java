package com.andersenlab.boilerplate.presenter;

import com.andersenlab.boilerplate.BoilerplateApp;
import com.andersenlab.boilerplate.model.Image;
import com.andersenlab.boilerplate.model.db.DatabaseHelper;
import com.andersenlab.boilerplate.view.MainMvpView;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class MainPresenter extends BasePresenter<MainMvpView> {

    private List<Image> images;

    public void loadItem() {
        if (isViewAttached()) {
            if (images == null) {
                Timber.i("initialize images");
                DatabaseHelper dbHelper = DatabaseHelper.getInstance(BoilerplateApp.getInstance());
                images = new ArrayList<>(dbHelper.getAllImages());
            }

            Timber.i("images count = " + images.size());

            if (images.isEmpty())
                getMvpView().showEmpty();
            else {
                getMvpView().showNewItem(images.remove(0));
            }
        } else
            getMvpView().showError();
    }

    public void resetItems() {
        images = null;
    }
}
