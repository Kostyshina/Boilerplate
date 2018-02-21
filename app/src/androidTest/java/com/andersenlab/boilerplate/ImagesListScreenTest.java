package com.andersenlab.boilerplate;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.andersenlab.boilerplate.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;

import static android.support.test.internal.util.Checks.checkState;
import static org.hamcrest.CoreMatchers.not;

/**
 * Instrumental test that check adding item to recycler view on {@link MainActivity} screen.
 */

@RunWith(AndroidJUnit4.class)
public class ImagesListScreenTest {

    private static final String TAG = "ImagesListScreenTest";

    @Rule
    public ActivityTestRule<MainActivity> mainActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    // Convenience helper
    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Test
    public void addItemsToTheList() {
        Log.i(TAG, "Start adding items");
        int i = 0;
        boolean isItemAdded;
        do {
            isItemAdded = addImageItemToTheList();
            i++;
        } while(i < 17 && isItemAdded);
    }

    private boolean addImageItemToTheList() {
        // Initial items count
        int initialItemsCount = getListItemsCount();
        Log.i(TAG, "initialItemsCount = " + initialItemsCount);

        // Click on the add item button
        onView(withId(R.id.fbs_main_add_item)).perform(click());

        // Verify image is added
        int afterClickItemsCount = getListItemsCount();
        Log.i(TAG, "afterClickItemsCount = " + afterClickItemsCount);

        if (afterClickItemsCount == initialItemsCount) {
            onView(withText(R.string.main_no_items))
                    .inRoot(withDecorView(not(mainActivityRule.getActivity()
                            .getWindow().getDecorView())))
                    .check(matches(isDisplayed()));
            return false;
        } else {
            checkState(afterClickItemsCount > initialItemsCount,
                    "Item has not been added");
            checkState(afterClickItemsCount - initialItemsCount == 1,
                    "Only one item at a time should be added");

            // Verify image is displayed
            onView(withRecyclerView(R.id.rv_main_images)
                    .atPosition(afterClickItemsCount - 1)).check(matches(isDisplayed()));
            return true;
        }
    }

    private int getListItemsCount() {
        RecyclerView imagesRecyclerView = mainActivityRule.getActivity()
                .findViewById(R.id.rv_main_images);
        return imagesRecyclerView.getAdapter().getItemCount();
    }
}
