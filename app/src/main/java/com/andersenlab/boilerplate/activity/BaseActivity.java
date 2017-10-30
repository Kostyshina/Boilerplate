package com.andersenlab.boilerplate.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.andersenlab.boilerplate.R;

/**
 * Base class for all activities.
 */

public class BaseActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);

        drawerLayout = findViewById(R.id.base_root_layout);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        FrameLayout activityContainer = getContainerLayout();
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        initNavDrawer();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(Gravity.START)) {
            closeDrawer();
        } else
            super.onBackPressed();
    }

    protected boolean useToolbar()
    {
        return true;
    }

    protected boolean useDrawerToggle() {
        return true;
    }

    public String getToolbarTitle(){
        return "";
    }

    private FrameLayout getContainerLayout() {
        return (FrameLayout) drawerLayout.findViewById(R.id.activity_container);
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

    private void setToolbarTitle(String title) {
        if (toolbar != null) {
            TextView titleView = toolbar.findViewById(R.id.toolbar_title);
            if (titleView != null)
                titleView.setText(title);
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

        if(useDrawerToggle())
            drawerToggle.syncState();
        else {
            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayShowHomeEnabled(false);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }
}
