package com.andersenlab.boilerplate.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;

import com.andersenlab.boilerplate.R;
import com.andersenlab.boilerplate.customview.FloatingBottomSheet;

import butterknife.BindView;
import timber.log.Timber;

public class MainActivity extends BaseActivity {

    private static final String ADD_ITEM_BUNDLE = "com.andersenlab.boilerplate.activity.addItem";

    @BindView(R.id.app_bar_layout_main) AppBarLayout appBarLayout;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.fbs_main_add_item) FloatingBottomSheet addItem;
    @BindView(R.id.fragment_container_main) FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            onFragmentAdded(getSupportFragmentManager().findFragmentById(getFragmentContainer()));
        }

        CoordinatorLayout.LayoutParams appBarLayoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
        behavior.setDragCallback(new AppBarDragCallback());
        appBarLayoutParams.setBehavior(behavior);
        appBarLayout.setLayoutParams(appBarLayoutParams);

        addItem.setOnClickListener(this::onAddItemClicked);
    }

    @Override
    protected int getFragmentContainer() {
        return R.id.fragment_container_main;
    }

    @Override
    protected Fragment getFragmentWithBundle(Fragment fragment) {
        Bundle args = new Bundle();
        args.putBoolean(ADD_ITEM_BUNDLE, fragment instanceof OnAddItemClickListener);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onFragmentAdded(Fragment currentFragment) {
        Timber.i("onFragmentAdded");
        Bundle args;
        if (currentFragment != null && (args = currentFragment.getArguments()) != null) {
            boolean addItemNeeded = args.getBoolean(ADD_ITEM_BUNDLE, false);
            if (addItemNeeded) {
                Timber.i("display addItem button");
                addItem.setVisibility(View.VISIBLE);
                addItem.invalidate();
            } else
                addItem.setVisibility(View.INVISIBLE);
        } else
            addItem.setVisibility(View.INVISIBLE);
    }

    @Override
    public String getToolbarTitle() {
        return getString(R.string.app_name);
    }

    private void onAddItemClicked(View view) {
        Timber.i("Tap on add item button");
        Fragment currentFragment = fragmentManager.findFragmentById(getFragmentContainer());
        if (currentFragment instanceof OnAddItemClickListener) {
            ((OnAddItemClickListener) currentFragment).addItem();
        }
    }

    public interface OnAddItemClickListener {
        void addItem();
    }
}
