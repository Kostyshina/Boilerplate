package com.andersenlab.boilerplate.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.andersenlab.boilerplate.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity {

    @BindView(R.id.base_root_layout) DrawerLayout drawerLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        if (drawerLayout != null && drawerLayout.isDrawerOpen(Gravity.START)) {
            closeDrawer();
        } else
            super.onBackPressed();
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
     * Override this method if your activity needs title (default not set).
     * @return toolbar title for current activity.
     */
    protected String getToolbarTitle() {
        return "";
    }

    private void setToolbarTitle(String title) {
        if (toolbar != null) {
            TextView titleView = toolbar.findViewById(R.id.toolbar_title);
            if (titleView != null)
                titleView.setText(title);
        }
    }

    private FrameLayout getContainerLayout() {
        return (FrameLayout) findViewById(R.id.activity_container);
    }

    private void closeDrawer() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        }
    }

    private void initToolbar() {
        if (toolbar != null) {
            if (useToolbar()) {
                setSupportActionBar(toolbar);
                setToolbarTitle(getToolbarTitle());
            } else
                toolbar.setVisibility(View.GONE);
        }
    }

    private void initNavDrawer() {
        initToolbar();
        setUpNavView();
    }

    private void setUpNavView() {
        //NavigationView navigationView = findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);
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
}
