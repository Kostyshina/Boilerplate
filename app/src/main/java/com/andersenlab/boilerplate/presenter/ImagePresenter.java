package com.andersenlab.boilerplate.presenter;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.andersenlab.boilerplate.model.Image;
import com.andersenlab.boilerplate.model.db.DatabaseHelper;
import com.andersenlab.boilerplate.model.realm.RealmInteractor;
import com.andersenlab.boilerplate.view.ImageMvpView;
import com.andersenlab.boilerplate.view.MvpView;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Presenter to request images.
 */

public class ImagePresenter extends BasePresenter<ImageMvpView> implements Parcelable {

    public static final Creator<ImagePresenter> CREATOR = new Creator<ImagePresenter>() {
        @Override
        public ImagePresenter createFromParcel(Parcel source) {
            return new ImagePresenter(source);
        }

        @Override
        public ImagePresenter[] newArray(int i) {
            return new ImagePresenter[0];
        }
    };

    private List<Image> images;

    public ImagePresenter() {

    }

    private ImagePresenter(Parcel source) {
        int listSize = source.readInt();
        images = new ArrayList<>(listSize);
        source.readTypedList(images, Image.CREATOR);
    }

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

    public void loadItemThroughRetrofit() {
        //TODO need implementation
    }

    public void resetItems() {
        images = null;
    }

    private void loadItem() {
        if (isViewAttached() && images != null) {
            Timber.i("images count = %d", images.size());

            if (images.isEmpty())
                getMvpView().showNoNewImages();
            else {
                getMvpView().showNewImage(images.remove(0));
            }
        } else
            getMvpView().showError(
                    new MvpView.MvpViewException(EXCEPTION_MESSAGE_VIEW_NOT_ATTACHED));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(images != null ? images.size() : 0);
        dest.writeTypedList(images);
    }
}
