package com.andersenlab.boilerplate.activity;

import android.os.Bundle;
import android.view.View;

import com.andersenlab.boilerplate.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_main_add_item)
    public void addItem(View view) {
        Timber.i("Tap on add item button");
    }

    @Override
    protected boolean useDrawerToggle() {
        return false;
    }

    @Override
    public String getToolbarTitle() {
        return getString(R.string.app_name);
    }
}
