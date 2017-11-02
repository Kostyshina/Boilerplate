package com.andersenlab.boilerplate.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.andersenlab.boilerplate.R;

import timber.log.Timber;

/**
 * Behavior class for collapsing/expanding views in CoordinatorLayout.
 */
//TODO class optimisation. saving collapsed/expanded state for the view.
public class ScrollAwareBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    private static final int DIRECTION_UP = 1;
    private static final int DIRECTION_DOWN = -1;

    @IntDef({DIRECTION_UP, DIRECTION_DOWN})
    public @interface ScrollingDirection {

    }

    /* Tracking direction of user motion */
    private @ScrollingDirection int scrollingDirection;
    /* Tracking last threshold crossed */
    private @ScrollingDirection int scrollTrigger;
    /* View is collapsed when you scrolling in that direction */
    private Direction scrollDirectionCollapse;
    /* Accumulated scroll distance */
    private int scrollDistance;
    /* Distance threshold to trigger animation */
    private int scrollThreshold;
    /* Indicates if view is showing or not */
    private boolean isExpanded = false;

    public ScrollAwareBehavior() {
        Timber.i("default constructor");
        scrollDirectionCollapse = Direction.BOTTOM;
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
                        Direction.BOTTOM.getValue())
        );


        Timber.i("scrollThreshold = " + scrollThreshold);
        Timber.i("scrollDirectionCollapse = " + scrollDirectionCollapse);

        a.recycle();
    }

    @Override
    public Parcelable onSaveInstanceState(CoordinatorLayout parent, V child) {
        Timber.i("onSaveInstanceState");
        return super.onSaveInstanceState(parent, child);
    }

    @Override
    public void onRestoreInstanceState(CoordinatorLayout parent, V child, Parcelable state) {
        Timber.i("onRestoreInstanceState");
        super.onRestoreInstanceState(parent, child, state);
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
        //Timber.i("onNestedPreScroll, dy = " + dy + ", consumed y = " + consumed[1]);
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        if (dy > 0 && scrollingDirection != DIRECTION_UP) {
            Timber.i("scroll down");
            setNewDirection(DIRECTION_UP);
        } else if (dy < 0 && scrollingDirection != DIRECTION_DOWN) {
            Timber.i("scroll up");
            setNewDirection(DIRECTION_DOWN);
        }
    }

    //Called after the scrolling child consumes the event, with amount consumed
    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                               @NonNull V child, @NonNull View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed,
                               int type) {
        //Timber.i("onNestedScroll");
        //Timber.i("scrollDirection = " + scrollingDirection + ", scrollTrigger = " + scrollTrigger);
        //Timber.i("dyConsumed = " + dyConsumed + ", dyUnconsumed = " + dyUnconsumed);
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        //Consumed distance is the actual distance traveled by the scrolling view
        scrollDistance += dyConsumed;
        //Timber.i("scrollDistance = " + scrollDistance);
        if (scrollDistance > scrollThreshold
                && scrollTrigger != DIRECTION_UP) {
            switchViewVisibility(child, DIRECTION_UP);
        } else if (scrollDistance < -scrollThreshold
                && scrollTrigger != DIRECTION_DOWN) {
            switchViewVisibility(child, DIRECTION_DOWN);
        }
    }

    private void setNewDirection(@ScrollingDirection int direction) {
        scrollingDirection = direction;
        scrollDistance = 0;
    }

    private void switchViewVisibility(V view, @ScrollingDirection int previousDirection) {
        scrollTrigger = previousDirection;
        Timber.i("switchViewVisibility");
        Timber.i("scrollDirectionCollapse = " + scrollDirectionCollapse + ", scrollTrigger = " + scrollTrigger);
        if (scrollDirectionCollapse == Direction.TOP) {
            if (scrollTrigger == DIRECTION_DOWN)
                hideViews(view);
            else if (scrollTrigger == DIRECTION_UP)
                showViews(view);
        } else if (scrollDirectionCollapse == Direction.BOTTOM) {
            if (scrollTrigger == DIRECTION_UP)
                hideViews(view);
            else if (scrollTrigger == DIRECTION_DOWN)
                showViews(view);
        }
    }

    private void hideViews(V view) {
        Timber.i("hideView, viewHeight = " + view.getHeight());
        isExpanded = false;
        view.animate().translationY(view.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews(V view) {
        Timber.i("showView, viewHeight = " + view.getHeight());
        isExpanded = true;
        view.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    /**
     * Enum to detect when hide or show view.
     * Contains values {@link Direction#LEFT}, {@link Direction#TOP}, {@link Direction#RIGHT} and
     * {@link Direction#BOTTOM} that are detecting to collapse view when moving accordingly
     * to the left, top, right or bottom. View will be shown when moving on opposite direction.
     */
    private enum Direction {
        LEFT(0), TOP(1), RIGHT(2), BOTTOM(3);

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
        private boolean isExpanded;

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.isExpanded = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeByte((byte) (this.isExpanded ? 1 : 0));
        }
    }
}
