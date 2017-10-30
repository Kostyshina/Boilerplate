package com.andersenlab.boilerplate.presenter;

import com.andersenlab.boilerplate.view.MvpView;

import timber.log.Timber;

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */

public class BasePresenter<T extends MvpView> implements Presenter<T> {

    private T mvpView;

    @Override
    public void attachView(T view) {
        Timber.i("attachView");
        mvpView = view;
    }

    @Override
    public boolean detachView() {
        Timber.i("detachView");
        mvpView = null;
        return true;
    }

    protected boolean isViewAttached() {
        return mvpView != null;
    }

    protected T getMvpView() {
        return mvpView;
    }
}
