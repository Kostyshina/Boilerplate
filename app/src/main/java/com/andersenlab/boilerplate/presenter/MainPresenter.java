package com.andersenlab.boilerplate.presenter;

import android.content.Context;

import com.andersenlab.boilerplate.model.Image;
import com.andersenlab.boilerplate.model.db.DatabaseHelper;
import com.andersenlab.boilerplate.model.realm.RealmInteractor;
import com.andersenlab.boilerplate.view.MainMvpView;
import com.andersenlab.boilerplate.view.MvpView;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class MainPresenter extends BasePresenter<MainMvpView> {

    private List<Image> images;

    public void loadItemFromDb(Context context) {
        if (images == null) {
            Timber.i("initialize images");
            DatabaseHelper dbHelper =
                    DatabaseHelper.getInstance(context.getApplicationContext());
            images = new ArrayList<>(dbHelper.getAllImages());
        }
        loadItem();
    }

    public void loadItemFromRealm() {
        if (images == null) {
            Timber.i("initialize images");
            images = RealmInteractor.getInstance().getObjects(Image.class);
        }
        loadItem();
    }

    public void resetItems() {
        images = null;
    }

    private void loadItem() {
        if (isViewAttached() && images != null) {
            Timber.i("images count = " + images.size());

            if (images.isEmpty())
                getMvpView().showNoNewItem();
            else {
                getMvpView().showNewItem(images.remove(0));
            }
        } else
            getMvpView().showError(
                    new MvpView.MvpViewException(EXCEPTION_MESSAGE_VIEW_NOT_ATTACHED));
    }
}
