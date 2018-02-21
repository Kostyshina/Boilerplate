package com.andersenlab.boilerplate.presenter;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.andersenlab.boilerplate.model.CachedObservable;
import com.andersenlab.boilerplate.model.Image;
import com.andersenlab.boilerplate.model.db.DatabaseHelper;
import com.andersenlab.boilerplate.model.realm.RealmInteractor;
import com.andersenlab.boilerplate.model.retrofit.RedditInteractor;
import com.andersenlab.boilerplate.view.ImageMvpView;
import com.andersenlab.boilerplate.view.MvpView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import timber.log.Timber;

/**
 * Presenter to request images.
 */

public class ImagePresenter extends BasePresenter<ImageMvpView> implements Parcelable {

    private static final String EXCEPTION_MESSAGE_NO_IMAGES_LOADED =
            "No images can be received from repository";

    public static final Parcelable.Creator<ImagePresenter> CREATOR =
            new Parcelable.Creator<ImagePresenter>() {
                @Override
                public ImagePresenter createFromParcel(Parcel source) {
                    return new ImagePresenter(source);
                }

                @Override
                public ImagePresenter[] newArray(int size) {
                    return new ImagePresenter[size];
                }
    };

    private List<Image> images;
    private CachedObservable cachedObservable;
    private boolean isLoading;

    public ImagePresenter() {
        cachedObservable = CachedObservable.newInstance();
    }

    private ImagePresenter(Parcel source) {
        isLoading = source.readByte() != 0;
        int listSize = source.readInt();
        images = new ArrayList<>(listSize);
        source.readTypedList(images, Image.CREATOR);
        cachedObservable = CachedObservable.newInstance();
    }

    public boolean isLoading() {
        return isLoading;
    }

    @Override
    protected void onViewAttached() {
        Timber.i("onViewAttached");
        if (isLoading && !cachedObservable.isEmptyObservable(Image.class))
            addToCompositeDisposable(getImageObservable()
                    .subscribe(images::add, throwable -> loadItem(), this::loadItem));
    }

    @Override
    protected void onViewDetached() {
        Timber.i("onViewDetached");
        clearCompositeDisposible();
    }

    public void loadItemFromDb(Context context) {
        if (images == null) {
            Timber.i("initialize images, DB");
            isLoading = true;
            DatabaseHelper dbHelper =
                    DatabaseHelper.getInstance(context.getApplicationContext());
            images = new ArrayList<>(dbHelper.getAllImages());
        }
        loadItem();
    }

    public void loadItemFromRealm() {
        if (images == null) {
            Timber.i("initialize images, REALM");
            isLoading = true;
            images = RealmInteractor.getInstance().getObjects(Image.class);
        }
        loadItem();
    }

    public void loadItemThroughRetrofit() {
        if (images == null || images.isEmpty()) {
            Timber.i("initialize images, NETWORK");
            isLoading = true;
            addToCompositeDisposable(getImageObservable()
                    .subscribe(image -> {
                        if (images == null)
                            images = new ArrayList<>();
                        images.add(image);
                    }, throwable -> {
                        images = null;
                        loadItem();
                    }, this::loadItem));
        } else
            loadItem();
    }

    private Observable<Image> getImageObservable() {
        Timber.i("getImageObservable()");
        RedditInteractor redditInteractor = RedditInteractor.getInstance();
        return cachedObservable.getCachedObservable(redditInteractor.getRedditImage(2)
                .flatMap(redditImage -> Observable.fromIterable(redditInteractor.mapUrlsList(redditImage)))
                .map(url -> {
                    Timber.i("url = %s", url);
                    Image image = new Image();
                    image.setId(1);
                    image.setImageUrl(url);
                    image.setContentDescription(url);
                    return image;
                }), Image.class);
    }

    public void resetItems() {
        images = null;
    }

    private void loadItem() {
        Timber.i("loadItem()");
        isLoading = false;

        if (!isViewAttached()) {
            Timber.e(new MvpView.MvpViewException(EXCEPTION_MESSAGE_VIEW_NOT_ATTACHED));
        } else if (images != null) {
            Timber.i("images count = %d", images.size());

            if (images.isEmpty())
                getMvpView().showNoNewImages();
            else {
                getMvpView().showNewImage(images.remove(0));
            }
        } else
            getMvpView().showError(
                    new MvpView.MvpViewException(EXCEPTION_MESSAGE_NO_IMAGES_LOADED));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeByte((byte)(isLoading ? 1 : 0));
        dest.writeInt(images != null ? images.size() : 0);
        dest.writeTypedList(images);
    }
}
