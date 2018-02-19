package com.andersenlab.boilerplate.customview;

import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.view.MotionEvent;
import android.view.View;

import timber.log.Timber;

public class AppBarBehavior extends AppBarLayout.Behavior {

    private ScrollCallback callback;

    public interface ScrollCallback {
        boolean isScrollEnabled();
    }

    public AppBarBehavior(ScrollCallback callback) {
        if (callback == null) {
            this.callback = () -> false;
        } else
            this.callback = callback;
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        Timber.i("onInterceptTouchEvent, enabled = %b", callback.isScrollEnabled());
        return callback.isScrollEnabled() && super.onInterceptTouchEvent(parent, child, ev);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes, int type) {
        Timber.i("onStartNestedScroll, enabled = %b", callback.isScrollEnabled());
        return callback.isScrollEnabled() && super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type);
    }

    @Override
    public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child, @NonNull View target, float velocityX, float velocityY, boolean consumed) {
        Timber.i("onNestedFling, enabled = %b", callback.isScrollEnabled());
        return callback.isScrollEnabled() && super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }
}
