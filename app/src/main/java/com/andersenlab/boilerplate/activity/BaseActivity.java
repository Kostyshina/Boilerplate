package com.andersenlab.boilerplate.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.andersenlab.boilerplate.R;
import com.andersenlab.boilerplate.fragment.ImagesFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class BaseActivity extends AppCompatActivity implements
        FragmentManager.OnBackStackChangedListener, NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.base_root_layout) DrawerLayout drawerLayout;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @Nullable
    @BindView(R.id.toolbar) Toolbar toolbar;

    protected FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(R.layout.activity_base);
        FrameLayout activityContainer = getContainerLayout();
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        ButterKnife.bind(this);
        initNavDrawer();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            closeDrawer();
        } else
            super.onBackPressed();
    }

    @Override
    public void onBackStackChanged() {
        Timber.i("onBackStackChanged");
        Fragment fragment = fragmentManager.findFragmentById(getFragmentContainer());
        Timber.i("fragment = %s", fragment);
        if (fragment != null) {
            String tag = fragment.getTag();
            navigationView.getMenu().findItem(Integer.valueOf(tag)).setChecked(true);
        } else
            navigationView.setCheckedItem(R.id.item_navigation_default);
        onFragmentAdded(fragment);
    }

    /**
     * Indicates if this activity needs toolbar (default set true).
     * @return true - if toolbar is needed, false otherwise.
     */
    protected boolean useToolbar() {
        return true;
    }

    /**
     * Indicates if this activity uses NavigationDrawer (default set true).
     * @return true - if navigationDrawer is used, false otherwise.
     * @see DrawerLayout
     */
    protected boolean useDrawerToggle() {
        return true;
    }

    /**
     * Override this method if your activity use NavigationDrawer (default not set).
     * @return idRes for layout where to put fragments when navigate through NavigationView.
     */
    protected @IdRes int getFragmentContainer() {
        return -1;
    }

    /**
     * Override this method if your activity needs to override {@link #onFragmentAdded(Fragment)} to
     * update layout according to bundle keys (by default no extra bundle is set).
     * @return fragment which will be added to layout.
     */
    protected Fragment getFragmentWithBundle(Fragment fragment) {
        return fragment;
    }

    /**
     * Override this method if your activity needs some updates in when fragment is added to layout
     * (default not set).
     */
    protected void onFragmentAdded(Fragment currentFragment) {}

    /**
     * Override this method if your activity needs title (default not set).
     * @return toolbar title for current activity.
     */
    protected String getToolbarTitle() {
        return "";
    }

    private void setToolbarTitle(String title) {
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    private FrameLayout getContainerLayout() {
        return (FrameLayout) findViewById(R.id.activity_container);
    }

    private void closeDrawer() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        }
    }

    private void initToolbar() {
        if (toolbar != null) {
            if (useToolbar()) {
                setToolbarTitle(getToolbarTitle());
                setSupportActionBar(toolbar);
            } else
                toolbar.setVisibility(View.GONE);
        }
    }

    private void initNavDrawer() {
        initToolbar();
        setUpNavView();
    }

    private void setUpNavView() {
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        if (useDrawerToggle())
            drawerToggle.syncState();
        else {
            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayShowHomeEnabled(false);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        String tag = String.valueOf(item.getItemId());
        switch (item.getItemId()) {
            case R.id.item_navigation_database:
                fragment = ImagesFragment.newInstance(ImagesFragment.LoadingRepository.LOAD_FROM_REALM);
                break;
            case R.id.item_navigation_network:
                fragment = ImagesFragment.newInstance(ImagesFragment.LoadingRepository.LOAD_FROM_NETWORK);
                break;
            default:
                Toast.makeText(this, R.string.toast_feature_not_implemented, Toast.LENGTH_SHORT).show();
        }
        if (fragment != null) {
            if (fragmentManager.findFragmentByTag(tag) == null) {
                fragmentManager.beginTransaction()
                        .replace(getFragmentContainer(), getFragmentWithBundle(fragment), tag)
                        .addToBackStack(null)
                        .commit();
            }
        }
        closeDrawer();
        return false;
    }

    protected class AppBarDragCallback extends AppBarLayout.Behavior.DragCallback {
        @Override
        public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
            return false;
        }
    }
}
