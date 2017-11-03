package com.andersenlab.boilerplate.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.andersenlab.boilerplate.R;

import timber.log.Timber;

/**
 * Behavior class for collapsing/expanding views in CoordinatorLayout
 * on scrolling up or down.
 */

public class ScrollAwareBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    /* Tracking direction of user motion */
    private Direction scrollDirection;
    /* Tracking last threshold crossed */
    private Direction scrollTrigger;
    /* View is collapsed when you scrolling in that direction */
    private Direction scrollDirectionCollapse;
    /* Accumulated scroll distance (pixels) */
    private int scrollDistance;
    /* Distance threshold to trigger animation (pixels) */
    private int scrollThreshold;
    /* Indicates if view is showing or not */
    private boolean isExpanded = true;

    public ScrollAwareBehavior() {
        Timber.i("default constructor");
        scrollThreshold = 20;
        scrollDirectionCollapse = Direction.DOWN;
    }

    /**
     * Default constructor for inflating a FancyBehavior from layout.
     * Use it if you need to set attributes programmatically.
     *
     * @param context The {@link Context}.
     * @param attrs   The {@link AttributeSet}.
     */
    public ScrollAwareBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray actionBarAttr = context.getTheme()
                .obtainStyledAttributes(new int[]{R.attr.actionBarSize});
        //Use the standard action bar height
        int defaultThreshold = actionBarAttr.getDimensionPixelSize(0, 0);
        actionBarAttr.recycle();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScrollAwareBehavior);

        scrollThreshold = a.getDimensionPixelSize(
                R.styleable.ScrollAwareBehavior_behavior_collapse_threshold, defaultThreshold);
        scrollDirectionCollapse = Direction.getDirection(
                a.getInt(R.styleable.ScrollAwareBehavior_behavior_scroll_direction_collapse,
                        Direction.DOWN.getValue())
        );


        Timber.i("scrollThreshold = " + scrollThreshold);
        Timber.i("scrollDirectionCollapse = " + scrollDirectionCollapse);

        a.recycle();
    }

    @Override
    public Parcelable onSaveInstanceState(CoordinatorLayout parent, V child) {
        Timber.i("onSaveInstanceState");
        final Parcelable superState = super.onSaveInstanceState(parent, child);

        final SavedState ss = new SavedState(superState);

        ss.isExpanded = isExpanded;
        ss.scrollDistance = scrollDistance;
        ss.scrollDirection = scrollDirection;
        ss.scrollTrigger = scrollTrigger;

        return ss;
    }

    @Override
    public void onRestoreInstanceState(CoordinatorLayout parent, V child, Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(parent, child, state);
            return;
        }
        Timber.i("onRestoreInstanceState");
        final SavedState ss = (SavedState)state;
        super.onRestoreInstanceState(parent, child, ss.getSuperState());

        isExpanded = ss.isExpanded;
        scrollDistance = ss.scrollDistance;
        scrollDirection = ss.scrollDirection;
        scrollTrigger = ss.scrollTrigger;

        if (!isExpanded) {
            child.setVisibility(View.INVISIBLE);
        }
    }

    //Called before a nested scroll event. Return true to declare interest
    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull V child, @NonNull View directTargetChild,
                                       @NonNull View target, int axes, int type) {
        Timber.i("onStartNestedScroll");
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0 && type == ViewCompat.TYPE_TOUCH;
    }

    //Called before the scrolling child consumes the event
    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                  @NonNull V child, @NonNull View target, int dx, int dy,
                                  @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        if (dy != 0) {
            setNewDirection(dy > 0 ? Direction.DOWN : Direction.UP);
        }
    }

    //Called after the scrolling child consumes the event, with amount consumed
    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                               @NonNull V child, @NonNull View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed,
                               int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        //Consumed distance is the actual distance traveled by the scrolling view
        scrollDistance += dyConsumed;
        if (scrollDistance > scrollThreshold || scrollDistance < -scrollThreshold) {
            switchViewVisibility(child);
        }
    }

    private void setNewDirection(Direction direction) {
        if (scrollDirection != direction) {
            scrollDirection = direction;
            scrollDistance = 0;
        }
    }

    private void switchViewVisibility(V view) {
        if (scrollTrigger != scrollDirection) {
            scrollTrigger = scrollDirection;
            Timber.i("switchViewVisibility");
            Timber.i("scrollDirectionCollapse = " + scrollDirectionCollapse + ", scrollTrigger = " + scrollTrigger);
            if (scrollTrigger == scrollDirectionCollapse) {
                hideViews(view);
            } else {
                showViews(view);
            }
        }
    }

    private float calculateViewY(V view) {
        Timber.i("calculateViewY");
        float y = 0;
        if (!isExpanded) {
            int gravity = ((CoordinatorLayout.LayoutParams) view.getLayoutParams()).gravity;
            Timber.i("gravity = " + gravity);
            if (gravity == Gravity.BOTTOM || gravity == Gravity.TOP) {
                y = gravity == Gravity.BOTTOM ? view.getHeight() : -view.getHeight();
            }
        }
        Timber.i("y = " + y);
        return y;
    }

    private void hideViews(V view) {
        Timber.i("hideView, viewHeight = " + view.getHeight());
        isExpanded = false;
        view.animate()
                .translationY(calculateViewY(view))
                .setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews(V view) {
        Timber.i("showView, viewHeight = " + view.getHeight());
        if (view.getVisibility() != View.VISIBLE) {
            Timber.i("invisible view " + view.getY());
            if (view.getTranslationY() == 0) {
                view.setTranslationY(calculateViewY(view));
                view.setVisibility(View.VISIBLE);
            }
        }
        isExpanded = true;
        view.animate()
                .translationY(calculateViewY(view))
                .setInterpolator(new DecelerateInterpolator(2)).start();
    }

    /**
     * Enum to detect direction of moving.
     * Contains values {@link Direction#UP} and {@link Direction#DOWN} that are
     * detecting to collapse view when moving accordingly to the top or bottom.
     * View will be shown when moving on opposite direction.
     */
    private enum Direction {
        UP(1), DOWN(-1);

        private final int value;

        Direction(int value) {
            this.value = value;
        }

        public static Direction getDirection(int value) {
            for (Direction direction : Direction.values()) {
                if (direction.getValue() == value)
                    return direction;
            }
            return null;
        }

        public int getValue() {
            return value;
        }
    }

    private static class SavedState extends View.BaseSavedState {

        private Direction scrollDirection;
        private Direction scrollTrigger;
        private int scrollDistance;
        private boolean isExpanded;

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel source) {
                        return new SavedState(source);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel source) {
            super(source);
            this.scrollDirection = (Direction) source.readSerializable();
            this.scrollTrigger = (Direction) source.readSerializable();
            this.scrollDistance = source.readInt();
            this.isExpanded = source.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeSerializable(this.scrollDirection);
            dest.writeSerializable(this.scrollTrigger);
            dest.writeInt(this.scrollDistance);
            dest.writeByte((byte) (this.isExpanded ? 1 : 0));
        }
    }
}
