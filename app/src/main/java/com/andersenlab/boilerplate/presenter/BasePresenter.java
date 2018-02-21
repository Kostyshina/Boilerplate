package com.andersenlab.boilerplate.presenter;

import com.andersenlab.boilerplate.view.MvpView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */

public abstract class BasePresenter<T extends MvpView> implements Presenter<T> {

    protected static final String EXCEPTION_MESSAGE_VIEW_NOT_ATTACHED =
            "Please call Presenter.attachView(MvpView) before" +
                    " requesting data to the Presenter";

    private T mvpView;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void attachView(T view) {
        Timber.i("attachView");
        mvpView = view;
        onViewAttached();
    }

    @Override
    public boolean detachView() {
        Timber.i("detachView");
        mvpView = null;
        onViewDetached();
        return true;
    }

    protected abstract void onViewAttached();

    protected abstract void onViewDetached();

    protected boolean isViewAttached() {
        return mvpView != null;
    }

    protected T getMvpView() {
        return mvpView;
    }

    protected void addToCompositeDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    protected void clearCompositeDisposible() {
        compositeDisposable.clear();
    }
}
