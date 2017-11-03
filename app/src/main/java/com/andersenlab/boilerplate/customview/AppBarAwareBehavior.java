package com.andersenlab.boilerplate.customview;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import timber.log.Timber;

/**
 * Behavior class for collapsing/expanding views in CoordinatorLayout
 * depended on toolbar showing/hiding state.
 */

public class AppBarAwareBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    private float childY = -1;

    public AppBarAwareBehavior() {

    }

    public AppBarAwareBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, V child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, V child, View dependency) {
        if (childY == -1) {
            childY = child.getY();
        }
        float translationY = childY - child.getHeight() * dependency.getY() / dependency.getHeight();
        child.setY(translationY);
        return true;
    }
}
